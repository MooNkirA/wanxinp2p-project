package com.moon.wanxinp2p.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.BankCardDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.DepositoryConsumerResponse;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CodePrefixCode;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import com.moon.wanxinp2p.common.enums.DepositoryReturnCode;
import com.moon.wanxinp2p.common.enums.StatusCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.CodeNoUtil;
import com.moon.wanxinp2p.consumer.agent.AccountApiAgent;
import com.moon.wanxinp2p.consumer.agent.DepositoryAgentApiAgent;
import com.moon.wanxinp2p.consumer.common.enums.ConsumerErrorCode;
import com.moon.wanxinp2p.consumer.entity.BankCard;
import com.moon.wanxinp2p.consumer.entity.Consumer;
import com.moon.wanxinp2p.consumer.mapper.ConsumerMapper;
import com.moon.wanxinp2p.consumer.service.BankCardService;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户业务层接口实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-20 18:07
 * @description
 */
@Service
@Slf4j
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService {

    // 注入 feign 服务调用接口
    @Autowired
    private AccountApiAgent accountApiAgent;

    @Autowired
    private BankCardService bankCardService;
    // 注入 feign 存管代理服务接口
    @Autowired
    private DepositoryAgentApiAgent depositoryAgentApiAgent;

    /**
     * 检测用户是否存在
     *
     * @param mobile
     * @return
     */
    @Override
    public Integer checkMobile(String mobile) {
        return getByMobile(mobile) == null ? 0 : 1;
    }

    /**
     * 用户注册
     *
     * @param consumerRegisterDTO
     * @return
     */
    @Override
    // 在此方法上需要标识 @Hmily 注解，指定成功提交与失败回滚的方法
    @Hmily(confirmMethod = "confirmRegister", cancelMethod = "cancelRegister")
    public void register(ConsumerRegisterDTO consumerRegisterDTO) {
        // 判断用户是否已注册
        if (checkMobile(consumerRegisterDTO.getMobile()) == 1) {
            // 已注册则抛出业务异常
            throw new BusinessException(ConsumerErrorCode.E_140107);
        }

        Consumer consumer = new Consumer();
        BeanUtils.copyProperties(consumerRegisterDTO, consumer);
        consumer.setUsername(CodeNoUtil.getNo(CodePrefixCode.CODE_NO_PREFIX));
        consumerRegisterDTO.setUsername(consumer.getUsername());
        consumer.setUserNo(CodeNoUtil.getNo(CodePrefixCode.CODE_REQUEST_PREFIX));
        consumer.setIsBindCard(0);

        // 保存用户
        save(consumer);

        // 创建统一认证服务的调用参数
        AccountRegisterDTO dto = new AccountRegisterDTO();
        BeanUtils.copyProperties(consumerRegisterDTO, dto);
        // 远程调用
        RestResponse<AccountDTO> result = accountApiAgent.register(dto);
        if (CommonErrorCode.SUCCESS.getCode() != result.getCode()) {
            // 调用失败，抛出业务异常
            throw new BusinessException(ConsumerErrorCode.E_140106);
        }
    }

    /**
     * 成功确认方法，在 try 阶段成功后执行。
     * 注意：Try、Confirm、Cancel 的方法参数必须保持一致。
     *
     * @param consumerRegisterDTO
     */
    public void confirmRegister(ConsumerRegisterDTO consumerRegisterDTO) {
        log.info("execute confirmRegister");
    }

    /**
     * 失败回滚方法，在 try 阶段出现异常后执行。
     * 注意：Try、Confirm、Cancel 的方法参数必须保持一致。
     *
     * @param consumerRegisterDTO
     */
    public void cancelRegister(ConsumerRegisterDTO consumerRegisterDTO) {
        log.info("execute cancelRegister");
        // 异常回滚，删除原来新增的记录即可
        remove(Wrappers.<Consumer>lambdaQuery().eq(Consumer::getMobile, consumerRegisterDTO.getMobile()));
    }

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile 手机号
     * @return
     */
    private ConsumerDTO getByMobile(String mobile) {
        // 根据手机号查询
        Consumer consumer = this.getOne(new QueryWrapper<Consumer>().lambda().eq(Consumer::getMobile, mobile));

        if (consumer != null) {
            // 转 dto
            ConsumerDTO dto = new ConsumerDTO();
            BeanUtils.copyProperties(consumer, dto);
            return dto;
        }

        return null;
    }

    /**
     * 生成开户数据
     *
     * @param consumerRequest
     * @return
     */
    @Override
    @Transactional
    public RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest) {
        // 1.判断当前用户是否已经开户，根据用户手机号查询用户表
        ConsumerDTO consumerDTO = getByMobile(consumerRequest.getMobile());

        if (consumerDTO == null) {
            // 用户不存在
            throw new BusinessException(ConsumerErrorCode.E_140101);
        }

        // 判断 isBindCard（是否绑定银行卡）是否为1
        if (consumerDTO.getIsBindCard() == 1) {
            // 已经绑卡
            throw new BusinessException(ConsumerErrorCode.E_140105);
        }

        // 2.判断提交过来的银行卡是否已被绑定
        BankCardDTO bankCardDTO = bankCardService.getByCardNumber(consumerRequest.getCardNumber());
        if (bankCardDTO != null && StatusCode.STATUS_IN.getCode().compareTo(bankCardDTO.getStatus()) == 0) {
            throw new BusinessException(ConsumerErrorCode.E_140151);
        }

        // 3.更新用户的信息
        consumerRequest.setId(consumerDTO.getId());
        //产生请求流水号和用户编号
        consumerRequest.setUserNo(CodeNoUtil.getNo(CodePrefixCode.CODE_CONSUMER_PREFIX));
        consumerRequest.setRequestNo(CodeNoUtil.getNo(CodePrefixCode.CODE_REQUEST_PREFIX));
        //设置查询条件和需要更新的数据
        UpdateWrapper<Consumer> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Consumer::getMobile, consumerDTO.getMobile());
        updateWrapper.lambda().set(Consumer::getUserNo, consumerRequest.getUserNo());
        updateWrapper.lambda().set(Consumer::getRequestNo, consumerRequest.getRequestNo());
        updateWrapper.lambda().set(Consumer::getFullname, consumerRequest.getFullname());
        updateWrapper.lambda().set(Consumer::getIdNumber, consumerRequest.getIdNumber());
        updateWrapper.lambda().set(Consumer::getAuthList, "ALL");
        update(updateWrapper);

        // 4.保存银行卡信息
        BankCard bankCard = new BankCard();
        bankCard.setConsumerId(consumerDTO.getId());
        bankCard.setBankCode(consumerRequest.getBankCode());
        bankCard.setCardNumber(consumerRequest.getCardNumber());
        bankCard.setMobile(consumerRequest.getMobile());
        bankCard.setStatus(StatusCode.STATUS_OUT.getCode());
        // 根据用户id查询银行卡信息
        BankCardDTO existBankCard = bankCardService.getByConsumerId(bankCard.getConsumerId());
        if (existBankCard != null) {
            bankCard.setId(existBankCard.getId());
        }
        // 新增或更新银行卡信息
        bankCardService.saveOrUpdate(bankCard);

        // 5.准备数据，发起远程调用，把数据发到存管代理服务
        return depositoryAgentApiAgent.createConsumer(consumerRequest);
    }

    /**
     * 更新开户结果
     *
     * @param response
     * @return
     */
    @Override
    @Transactional
    public Boolean modifyResult(DepositoryConsumerResponse response) {
        // 根据响应码来判断状态 成功时 status=1；失败时 status=2
        int status = DepositoryReturnCode.RETURN_CODE_00000.getCode().equals(response.getRespCode())
                ? StatusCode.STATUS_IN.getCode() : StatusCode.STATUS_FAIL.getCode();

        // 根据请求号（requestNo）查询用户信息
        Consumer consumer = getOne(Wrappers.<Consumer>lambdaQuery().eq(Consumer::getRequestNo, response.getRequestNo()));

        if (consumer != null) {
            Long consumerId = consumer.getId();

            // 更新用户信息状态 （consumer 表的 STATUS 和 IS_BIND_CARD）
            this.update(Wrappers.<Consumer>lambdaUpdate()
                    .eq(Consumer::getId, consumerId)
                    .set(Consumer::getIsBindCard, status)
                    .set(Consumer::getStatus, status)
            );

            // 更新银行卡信息状态
            return bankCardService.update(Wrappers.<BankCard>lambdaUpdate()
                    .eq(BankCard::getConsumerId, consumerId)
                    .set(BankCard::getBankCode, response.getBankCode())
                    .set(BankCard::getBankName, response.getBankName())
                    .set(BankCard::getStatus, status)
            );
        }

        return false;
    }
}
