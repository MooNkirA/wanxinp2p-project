package com.moon.wanxinp2p.repayment.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.wanxinp2p.api.repayment.model.ProjectWithTendersDTO;
import com.moon.wanxinp2p.common.constants.P2PMqConstants;
import com.moon.wanxinp2p.repayment.service.RepaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 启动还款消息监听器
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-25 19:52
 * @description
 */
@Component
@RocketMQMessageListener(topic = P2PMqConstants.TOPIC_START_REPAYMENT, consumerGroup = "CID_START_REPAYMENT")
@Slf4j
public class StartRepaymentMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private RepaymentService repaymentService;

    @Override
    public void onMessage(String msg) {
        log.info("StartRepaymentMessageConsumer 接收到消息: {}", msg);
        // 1.解析消息
        final JSONObject jsonObject = JSON.parseObject(msg);
        ProjectWithTendersDTO projectWithTendersDTO =
                JSONObject.parseObject(jsonObject.getString("projectWithTendersDTO"), ProjectWithTendersDTO.class);

        // 2.调用业务层执行本地事务
        repaymentService.startRepayment(projectWithTendersDTO);
    }
}
