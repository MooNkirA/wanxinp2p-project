package com.moon.wanxinp2p.repayment.message;

import com.alibaba.fastjson.JSONObject;
import com.moon.wanxinp2p.api.repayment.model.RepaymentRequest;
import com.moon.wanxinp2p.common.constants.P2PMqConstants;
import com.moon.wanxinp2p.repayment.entity.RepaymentPlan;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 确认还款事务消息生产者
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-27 21:32
 * @description
 */
@Component
public class RepaymentProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void confirmRepayment(RepaymentPlan repaymentPlan, RepaymentRequest repaymentRequest) {
        // 1.构造消息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("repaymentPlan", repaymentPlan);
        jsonObject.put("repaymentRequest", repaymentRequest);

        Message<String> msg = MessageBuilder.withPayload(jsonObject.toJSONString()).build();

        // 2.发送消息
        rocketMQTemplate.sendMessageInTransaction(P2PMqConstants.TX_PRODUCER_GROUP_CONFIRM_REPAYMENT,
                P2PMqConstants.TOPIC_CONFIRM_REPAYMENT, msg, null);
    }

}
