package com.moon.wanxindepository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.wanxindepository.common.constant.StatusCode;
import com.moon.wanxindepository.common.domain.BusinessException;
import com.moon.wanxindepository.common.domain.LocalReturnCode;
import com.moon.wanxindepository.common.domain.RemoteReturnCode;
import com.moon.wanxindepository.entity.DepositoryBankCard;
import com.moon.wanxindepository.entity.RechargeDetails;
import com.moon.wanxindepository.mapper.RechargeDetailsMapper;
import com.moon.wanxindepository.message.GatewayMessageProducer;
import com.moon.wanxindepository.model.RechargeRequest;
import com.moon.wanxindepository.model.RechargeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 充值记录表 服务实现类
 */
@Slf4j
@Service
public class RechargeDetailsServiceImpl extends ServiceImpl<RechargeDetailsMapper, RechargeDetails>
        implements RechargeDetailsService {

    @Autowired
    private BalanceDetailsService balanceDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private GatewayMessageProducer producer;

    @Autowired
    private RequestDetailsService requestDetailsService;

    @Override
    public RechargeResponse recharge(RechargeRequest rechargeRequest) {
        String requestNo = rechargeRequest.getRequestNo();
        RechargeResponse response = new RechargeResponse();
        response.setRequestNo(requestNo);

        //校验交易密码
        if (!userService.verifyPassword(rechargeRequest.getUserNo(), rechargeRequest.getPassword())) {
            throw new BusinessException(LocalReturnCode.E_200302.getDesc());
        }

        //保存充值记录
        RechargeDetails rechargeDetails = new RechargeDetails();
        BeanUtils.copyProperties(rechargeRequest, rechargeDetails);
        rechargeDetails.setStatus(StatusCode.STATUS_OUT.getCode());
        save(rechargeDetails);

        try {
            //扣除用户实体银行卡余额
            DepositoryBankCard depositoryBankCard = userService.getDepositoryBankCardByUserNo(rechargeRequest.getUserNo());
            bankCardService.decreaseBalance(depositoryBankCard.getCardNumber(), rechargeRequest.getAmount());

            //更新用户在P2P平台可用余额信息
            balanceDetailsService.recharge(rechargeRequest);

            //更新充值记录结果
            rechargeDetails.setStatus(StatusCode.STATUS_IN.getCode());
            updateById(rechargeDetails);

            //更新处理结果
            response.setSuccess();
            requestDetailsService.modifyGatewayByRequestNo(response);

            // 产生充值成功消息
            producer.recharge(rechargeRequest.getAppCode(), response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            //更新充值记录结果
            rechargeDetails.setStatus(StatusCode.STATUS_FAIL.getCode());
            updateById(rechargeDetails);

            //更新处理结果
            response.setFailure();
            response.setRespMsg(e.getMessage());
            requestDetailsService.modifyGatewayByRequestNo(response);

            // 产生充值失败消息
            producer.recharge(rechargeRequest.getAppCode(), response);
            throw new BusinessException(response.getRequestNo(), RemoteReturnCode.EXCEPTION, e.getMessage(), e);
        }
        return response;
    }
}
