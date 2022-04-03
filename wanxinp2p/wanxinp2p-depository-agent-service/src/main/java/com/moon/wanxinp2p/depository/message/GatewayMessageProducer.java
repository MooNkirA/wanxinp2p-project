package com.moon.wanxinp2p.depository.message;

import com.moon.wanxinp2p.api.depository.model.DepositoryConsumerResponse;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 存管代理服务异步通知消息生产者
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-28 22:18
 * @description
 */
@Component
public class GatewayMessageProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 用户开户结果异步消息发送
     *
     * @param response
     */
    public void personalRegister(DepositoryConsumerResponse response) {
        rocketMQTemplate.convertAndSend("TP_GATEWAY_NOTIFY_AGENT:PERSONAL_REGISTER", response);
    }

}
