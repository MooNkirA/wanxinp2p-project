package com.moon.wanxinp2p.common.constants;

/**
 * P2P 项目消息模块相关常量
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-27 21:21
 * @description
 */
public final class P2PMqConstants {

    /**
     * 启动还款消息主题
     */
    public final static String TOPIC_START_REPAYMENT = "TP_START_REPAYMENT";
    /**
     * 确认还款消息主题
     */
    public final static String TOPIC_CONFIRM_REPAYMENT = "TP_CONFIRM_REPAYMENT";
    /**
     * 存管代理服务充值结果通知主题
     */
    public final static String TOPIC_GATEWAY_NOTIFY_RECHARGE = "TP_GATEWAY_NOTIFY_AGENT:RECHARGE";
    /**
     * 存管代理服务提现结果通知主题
     */
    public final static String TOPIC_GATEWAY_NOTIFY_WITHDRAW = "TP_GATEWAY_NOTIFY_AGENT:WITHDRAW";

    /**
     * 确认还款事务生产者分组
     */
    public final static String TX_PRODUCER_GROUP_CONFIRM_REPAYMENT = "PID_CONFIRM_REPAYMENT";

    private P2PMqConstants() {
    }

}
