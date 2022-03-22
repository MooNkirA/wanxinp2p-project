package com.moon.ensuredemo.bank2.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.ensuredemo.bank2.model.AccountChangeEvent;
import com.moon.ensuredemo.bank2.service.AccountInfoService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "topic_ensure_transfer", consumerGroup =
        "consumer_ensure_transfer")
public class EnsureMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private AccountInfoService accountInfoService;

    @Override
    public void onMessage(String message) {
        System.out.println("消费消息：" + message);
        //1.解析消息
        final JSONObject jsonObject = JSON.parseObject(message);
        AccountChangeEvent accountChangeEvent =
                JSONObject.parseObject(jsonObject.getString("accountChange"), AccountChangeEvent.
                        class);
        //2.执行本地事务
        accountChangeEvent.setAccountNo("2");
        accountInfoService.updateAccountBalance(accountChangeEvent);
    }
}
