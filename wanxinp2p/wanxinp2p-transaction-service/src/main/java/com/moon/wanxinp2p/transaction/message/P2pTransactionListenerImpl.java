package com.moon.wanxinp2p.transaction.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.moon.wanxinp2p.common.enums.ProjectCode;
import com.moon.wanxinp2p.transaction.entity.Project;
import com.moon.wanxinp2p.transaction.mapper.ProjectMapper;
import com.moon.wanxinp2p.transaction.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * RocketMQ 事务消息监听器
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-24 22:31
 * @description
 */
@Component
// txProducerGroup 属性指定消息的分组（注：与 P2pTransactionProducer 类中发送方法的分组名称一致）
@RocketMQTransactionListener(txProducerGroup = "PID_START_REPAYMENT")
@Slf4j
public class P2pTransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 执行本地事务
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        // 1. 解析消息
        final JSONObject jsonObject = JSON.parseObject(new String((byte[]) msg.getPayload()));
        Project project = JSONObject.parseObject(jsonObject.getString("project"), Project.class);

        // 2. 执行本地事务
        boolean result = projectService.updateProjectStatusAndStartRepayment(project);

        // 3. 返回执行结果
        return result ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }

    /**
     * 执行事务回查
     *
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("P2pTransactionListenerImpl 执行事务回查");
        // 1. 解析消息
        final JSONObject jsonObject = JSON.parseObject(new String((byte[]) msg.getPayload()));
        Project project = JSONObject.parseObject(jsonObject.getString("project"), Project.class);

        // 2. 查询标的状态
        Project projectDb = projectMapper.selectById(project.getId());

        // 3. 返回执行结果（如标的状态是还款中，则代表事务成功执行）
        return ProjectCode.REPAYING.getCode().equals(projectDb.getProjectStatus()) ?
                RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }
}
