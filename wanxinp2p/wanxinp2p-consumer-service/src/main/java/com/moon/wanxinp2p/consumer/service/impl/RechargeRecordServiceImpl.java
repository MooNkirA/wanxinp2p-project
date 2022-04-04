package com.moon.wanxinp2p.consumer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxinp2p.api.consumer.model.ConsumerDTO;
import com.moon.wanxinp2p.api.depository.model.DepositoryRechargeResponse;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.api.depository.model.RechargeRequest;
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
import com.moon.wanxinp2p.consumer.entity.RechargeRecord;
import com.moon.wanxinp2p.consumer.mapper.RechargeRecordMapper;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import com.moon.wanxinp2p.consumer.service.RechargeRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class RechargeRecordServiceImpl extends ServiceImpl<RechargeRecordMapper, RechargeRecord> implements RechargeRecordService {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private DepositoryAgentApiAgent depositoryAgentApiAgent;

    /**
     * 生成充值请求数据
     *
     * @param amount      充值金额
     * @param callbackURL 回调地址
     * @return
     */
    @Transactional
    @Override
    public RestResponse<GatewayRequest> createRechargeRecord(String amount, String callbackURL) {
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
            throw new BusinessException(ConsumerErrorCode.E_140133);
        }

        // 2. 接收用户填写的充值数据
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setConsumerId(consumer.getId());
        rechargeRecord.setUserNo(consumer.getUserNo());
        rechargeRecord.setAmount(new BigDecimal(amount));
        rechargeRecord.setCreateDate(LocalDateTime.now());
        // 生成请求流程方法
        String requestNo = CodeNoUtil.getNo(CodePrefixCode.CODE_REQUEST_PREFIX);
        rechargeRecord.setRequestNo(requestNo);
        // 设置状态为 2-未同步
        rechargeRecord.setCallbackStatus(StatusCode.STATUS_OUT.getCode());

        // 3. 用户中心保存充值信息
        save(rechargeRecord);

        // 4. 准备数据，发起远程调用，请求存管代理生成签名数据
        RechargeRequest rechargeRequest = new RechargeRequest();
        BeanUtils.copyProperties(rechargeRecord, rechargeRequest);
        rechargeRequest.setCallbackUrl(callbackURL); // 设置回调地址

        // 5. 将签名数据返回给前端
        return depositoryAgentApiAgent.createRechargeRecord(rechargeRequest);
    }

    /**
     * 更新充值结果
     *
     * @param response
     * @return
     */
    @Override
    public Boolean modifyResult(DepositoryRechargeResponse response) {
        // 根据响应码来判断状态 成功时 status=1；失败时 status=2
        int status = DepositoryReturnCode.RETURN_CODE_00000.getCode().equals(response.getRespCode())
                ? StatusCode.STATUS_IN.getCode() : StatusCode.STATUS_FAIL.getCode();

        // 根据请求号（requestNo）查询充值数据
        RechargeRecord rechargeRecord = getOne(Wrappers.<RechargeRecord>lambdaQuery().eq(RechargeRecord::getRequestNo, response.getRequestNo()));

        if (rechargeRecord != null) {
            // 更新充值记录 CALLBACK_STATUS 字段状态
            rechargeRecord.setCallbackStatus(status);
            return updateById(rechargeRecord);
        }

        return false;
    }

}
