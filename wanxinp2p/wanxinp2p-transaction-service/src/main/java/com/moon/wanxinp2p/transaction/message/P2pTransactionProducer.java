package com.moon.wanxinp2p.transaction.message;

import com.alibaba.fastjson.JSONObject;
import com.moon.wanxinp2p.api.repayment.model.ProjectWithTendersDTO;
import com.moon.wanxinp2p.common.constants.P2PMqConstants;
import com.moon.wanxinp2p.transaction.entity.Project;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 交易中心发送还款消息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-23 22:58
 * @description
 */
@Component
public class P2pTransactionProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void updateProjectStatusAndStartRepayment(Project project, ProjectWithTendersDTO projectWithTendersDTO) {
        // 1.构造消息
        JSONObject object = new JSONObject();
        object.put("project", project);
        object.put("projectWithTendersDTO", projectWithTendersDTO);
        // 将标的信息与标的还款信息保存到消息体中
        Message<String> msg = MessageBuilder.withPayload(object.toJSONString()).build();

        // 2.发送消息
        rocketMQTemplate.sendMessageInTransaction("PID_START_REPAYMENT", P2PMqConstants.TOPIC_START_REPAYMENT, msg, null);
    }

}
