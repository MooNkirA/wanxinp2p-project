package com.moon.ensuredemo.bank1.message;

import com.alibaba.fastjson.JSONObject;
import com.moon.ensuredemo.bank1.model.AccountChangeEvent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送转账消息
 */
@Component
public class BankMessageProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendAccountChangeEvent(AccountChangeEvent accountChangeEvent) {
        // 构造消息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChange", accountChangeEvent);
        // 转成 json 字符串
        Message<String> msg = MessageBuilder.withPayload(jsonObject.toJSONString()).build();

        // 发送消息
        rocketMQTemplate.sendMessageInTransaction("producer_ensure_transfer", "topic_ensure_transfer", msg, null);
    }
}
