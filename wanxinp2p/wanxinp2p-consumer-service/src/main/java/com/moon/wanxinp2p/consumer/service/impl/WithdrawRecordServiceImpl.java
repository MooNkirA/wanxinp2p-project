package com.moon.wanxinp2p.consumer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.BankCardDTO;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.depository.model.DepositoryWithdrawResponse;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.api.depository.model.WithdrawRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.common.enums.CodePrefixCode;
import com.moon.wanxinp2p.common.enums.CommonErrorCode;
import com.moon.wanxinp2p.common.enums.DepositoryReturnCode;
import com.moon.wanxinp2p.common.enums.StatusCode;
import com.moon.wanxinp2p.common.exception.BusinessException;
import com.moon.wanxinp2p.common.util.CodeNoUtil;
import com.moon.wanxinp2p.consumer.agent.DepositoryAgentApiAgent;
import com.moon.wanxinp2p.consumer.common.enums.ConsumerErrorCode;
import com.moon.wanxinp2p.consumer.common.util.SecurityUtil;
import com.moon.wanxinp2p.consumer.entity.WithdrawRecord;
import com.moon.wanxinp2p.consumer.mapper.WithdrawRecordMapper;
import com.moon.wanxinp2p.consumer.service.BankCardService;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import com.moon.wanxinp2p.consumer.service.WithdrawRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值记录业务层实现
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-04 12:16
 * @description
 */
@Service
public class WithdrawRecordServiceImpl extends ServiceImpl<WithdrawRecordMapper, WithdrawRecord> implements WithdrawRecordService {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private DepositoryAgentApiAgent depositoryAgentApiAgent;

    /**
     * 生成提现请求数据
     *
     * @param amount      提现金额
     * @param callbackUrl 回调地址
     * @return
     */
    @Override
    public RestResponse<GatewayRequest> createWithdrawRecord(String amount, String callbackUrl) {
        // 1. 使用工具类，从请求域中获取到用户手机号
        ConsumerDTO consumer = consumerService.getByMobile(SecurityUtil.getUser().getMobile());
        // 判断当前用户是否已经开户，根据用户手机号查询用户表
        if (consumer == null) {
            // 用户不存在
            throw new BusinessException(CommonErrorCode.E_140101);
        }
        // 判断 isBindCard（是否绑定银行卡）是否为1
        if (consumer.getIsBindCard() != 1) {
            // 已经绑卡
            throw new BusinessException(ConsumerErrorCode.E_140152);
        }

        // TODO: 目前只对金额做简单的校验，不够全面
        if (StringUtils.isEmpty(amount) || new BigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ConsumerErrorCode.E_140134);
        }

        // 2. 接收用户填写的提现数据
        WithdrawRecord withdrawRecord = new WithdrawRecord();
        withdrawRecord.setConsumerId(consumer.getId());
        withdrawRecord.setUserNo(consumer.getUserNo());
        withdrawRecord.setAmount(new BigDecimal(amount));
        withdrawRecord.setCreateDate(LocalDateTime.now());
        // TODO: 平台佣金，暂时为了方便，硬编码数值，后面需要修改为读取apollo配置，或者从交易服务中提供一个相应的查询接口。
        withdrawRecord.setCommission(new BigDecimal("0.15"));
        // 生成请求流程方法
        String requestNo = CodeNoUtil.getNo(CodePrefixCode.CODE_REQUEST_PREFIX);
        withdrawRecord.setRequestNo(requestNo);
        // 设置状态为 2-未同步
        withdrawRecord.setCallbackStatus(StatusCode.STATUS_OUT.getCode());

        // 3. 用户中心保存提现信息
        save(withdrawRecord);

        // 4. 请求存管代理生成签名数据
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        BeanUtils.copyProperties(withdrawRecord, withdrawRequest);
        // 查询银行卡信息
        BankCardDTO bankCardDTO = bankCardService.getByConsumerId(consumer.getId());
        withdrawRequest.setCardNumber(bankCardDTO.getCardNumber());
        withdrawRequest.setMobile(bankCardDTO.getMobile());
        // 设置回调地址
        withdrawRequest.setCallbackURL(callbackUrl);

        // 5. 将签名数据返回给前端
        return depositoryAgentApiAgent.createWithdrawRecord(withdrawRequest);
    }

    /**
     * 更新提现结果
     *
     * @param response
     * @return
     */
    @Override
    public Boolean modifyResult(DepositoryWithdrawResponse response) {
        // 根据请求号（requestNo）查询充值数据
        WithdrawRecord withdrawRecord = getOne(Wrappers.<WithdrawRecord>lambdaQuery()
                .eq(WithdrawRecord::getRequestNo, response.getRequestNo()));

        if (withdrawRecord != null) {
            // 根据响应码来判断状态 成功时 status=1；失败时 status=2
            int status = DepositoryReturnCode.RETURN_CODE_00000.getCode().equals(response.getRespCode())
                    ? StatusCode.STATUS_IN.getCode() : StatusCode.STATUS_FAIL.getCode();
            // 更新充值记录 CALLBACK_STATUS 字段状态
            withdrawRecord.setCallbackStatus(status);
            return updateById(withdrawRecord);
        }

        return false;
    }
}
