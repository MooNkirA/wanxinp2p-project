package com.moon.wanxinp2p.repayment.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.wanxinp2p.api.repayment.model.RepaymentRequest;
import com.moon.wanxinp2p.common.constants.P2PMqConstants;
import com.moon.wanxinp2p.repayment.entity.RepaymentPlan;
import com.moon.wanxinp2p.repayment.service.RepaymentService;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 确认还款消息消费监听器，向存管代理服务发送确认还款请求
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-29 10:07
 * @description
 */
@Component
@RocketMQMessageListener(topic = P2PMqConstants.TOPIC_CONFIRM_REPAYMENT, consumerGroup = "CID_CONFIRM_REPAYMENT")
@Log4j2
public class ConfirmRepaymentConsumer implements RocketMQListener<String> {

    @Autowired
    private RepaymentService repaymentService;

    @Override
    public void onMessage(String message) {
        log.info("确认还款消息监听器开始执行....");
        // 1.解析消息
        JSONObject jsonObject = JSON.parseObject(message);
        RepaymentPlan repaymentPlan = JSONObject.parseObject(jsonObject.getString("repaymentPlan"), RepaymentPlan.class);
        RepaymentRequest repaymentRequest = JSONObject.parseObject(jsonObject.getString("repaymentRequest"), RepaymentRequest.class);

        // 2.执行本地业务，身存管代理发送确认还款请求
        repaymentService.invokeConfirmRepayment(repaymentPlan, repaymentRequest);
    }
}
