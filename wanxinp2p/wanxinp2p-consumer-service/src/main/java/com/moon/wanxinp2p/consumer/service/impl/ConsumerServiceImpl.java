package com.moon.wanxinp2p.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.account.model.AccountDTO;
import com.moon.wanxinp2p.api.account.model.AccountRegisterDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CodePrefixCode;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.CodeNoUtil;
import com.moon.wanxinp2p.consumer.agent.AccountApiAgent;
import com.moon.wanxinp2p.consumer.common.enums.ConsumerErrorCode;
import com.moon.wanxinp2p.consumer.entity.Consumer;
import com.moon.wanxinp2p.consumer.mapper.ConsumerMapper;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
