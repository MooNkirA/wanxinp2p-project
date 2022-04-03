package com.moon.wanxinp2p.consumer.message;

import com.alibaba.fastjson.JSON;
import com.moon.wanxinp2p.api.depository.model.DepositoryConsumerResponse;
import com.moon.wanxinp2p.common.constants.ServiceNameConstants;
import com.moon.wanxinp2p.consumer.service.ConsumerService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RocketMQ 消息监听者。接收存管代理服务发送的开户结果通知消息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-01 9:58
 * @description
 */
@Component
public class GatewayNotifyConsumer {

    @Autowired
    private ConsumerService consumerService;

    /*
     * 从 apollo 中心注入 rocketmq 相关配置。
     *  但值得注意的是，RocketMQ 的消费者是在当前类的构造方法中初始化。
     *  而因为 spring 依赖注入机制是，对象会先实例化（执行构造方法），此时内存存在对象的引用，
     *  然后再对使用 @Value、@Autowired 等注解的属性进行依赖注入。
     *  所以当类被创建（构造方法执行，RocketMQ 消费者已经开始初始化），此时 @Value 注解标识的属性还没有依赖注入，属性值都是 null
     *  但创建消费者需要使用这两个属性，就会报出值为 null 的错误。
     *
     *  解决这个问题只需要将构造方法改成有参构造，有形参使用 @Value 注解，
     *  此时 spring 实例化对象就会先到容器中查找相应的对象或者值，再执行构造方法实例化对象
     */
    /*@Value("${rocketmq.consumer.group}")
    private String consumerGroup;
    @Value("${rocketmq.name-server}")
    private String mqNameServer;*/

    /*
     * 在构造方法中，初始化 RocketMQ 的监听器
     */
    public GatewayNotifyConsumer(@Value("${rocketmq.consumer.group}") String consumerGroup,
                                 @Value("${rocketmq.name-server}") String mqNameServer) throws MQClientException {
        /* 使用 RocketMQ 原生的 API 方式创建消息消费者 */
        // 1. 创建消费者，并且为其指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        // 2. 为消费者设置 NameServer 的地址
        consumer.setNamesrvAddr(mqNameServer);
        /*
         * 3. 指定消费者订阅的主题和标签
         *  String topic            消息主题名称
         *  String subExpression    消息标签名称（`*`代表所有标签）
         */
        consumer.subscribe("TP_GATEWAY_NOTIFY_AGENT", "*");
        // 4. 指定消费的顺序，此处指定从最后一个开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 5. 注册监听器。设置一个回调函数，并在函数中编写接收到消息之后的处理方法
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            // 处理获取到的消息
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 消费逻辑
                try {
                    // 获取列表中第一个元素，是消息对象实例
                    Message message = msgs.get(0);
                    // 获取消息主题
                    String topic = message.getTopic();
                    // 获取消息标签
                    String tags = message.getTags();
                    // 获取消息体
                    String body = new String(message.getBody(), StandardCharsets.UTF_8);
                    System.out.println("接收到消息主题 ===> " + topic);
                    System.out.println("接收到消息标签 ===> " + tags);
                    System.out.println("接收到消息主体 ===> " + body);

                    // 判断是否用户开户注册
                    if (ServiceNameConstants.NAME_PERSONAL_REGISTER.equals(tags)) {
                        DepositoryConsumerResponse response = JSON.parseObject(body, DepositoryConsumerResponse.class);
                        // 调用用户更新状态业务方法
                        consumerService.modifyResult(response);
                    }
                    // ...其他的消息类型判断与处理
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                // 返回消费成功状态
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 5. 启动消费者
        consumer.start();
    }
}
