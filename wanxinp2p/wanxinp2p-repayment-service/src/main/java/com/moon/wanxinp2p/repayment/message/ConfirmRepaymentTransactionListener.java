package com.moon.wanxinp2p.repayment.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.wanxinp2p.api.repayment.model.RepaymentRequest;
import com.moon.wanxinp2p.common.constants.P2PMqConstants;
import com.moon.wanxinp2p.repayment.entity.RepaymentPlan;
import com.moon.wanxinp2p.repayment.mapper.PlanMapper;
import com.moon.wanxinp2p.repayment.service.RepaymentService;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 确认还款事务消息监听类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-28 16:19
 * @description
 */
@Component
@RocketMQTransactionListener(txProducerGroup = P2PMqConstants.TX_PRODUCER_GROUP_CONFIRM_REPAYMENT)
@Log4j2
public class ConfirmRepaymentTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    private RepaymentService repaymentService;

    @Autowired
    private PlanMapper planMapper;

    /**
     * 执行本地事务，即修改还款计划、还款明细、应收明细等状态
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.info("确认还款执行本地事务");
        // 1. 解析消息
        JSONObject jsonObject = JSON.parseObject(new String((byte[]) msg.getPayload()));
        RepaymentPlan repaymentPlan = JSONObject.parseObject(jsonObject.getString("repaymentPlan"), RepaymentPlan.class);
        RepaymentRequest repaymentRequest = JSONObject.parseObject(jsonObject.getString("repaymentRequest"), RepaymentRequest.class);
        // 2. 执行本地事务，并返回结果
        return repaymentService.confirmRepayment(repaymentPlan, repaymentRequest) ?
                RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }

    /**
     * 执行事务回查
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("确认还款执行事务回查");
        // 1.解析消息
        JSONObject jsonObject = JSON.parseObject(new String((byte[]) msg.getPayload()));
        RepaymentPlan repaymentPlan = JSONObject.parseObject(jsonObject.getString("repaymentPlan"), RepaymentPlan.class);

        // 2.回查事务状态，
        RepaymentPlan repaymentPlanDb = planMapper.selectById(repaymentPlan.getId());

        // 3.返回结果，根据查询出来的还款计划的状态来来判断是否新增成功
        if (repaymentPlanDb != null && "1".equals(repaymentPlanDb.getRepaymentStatus())) {
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

}
