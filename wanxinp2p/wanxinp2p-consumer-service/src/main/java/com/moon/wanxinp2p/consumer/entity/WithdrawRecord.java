package com.moon.wanxinp2p.consumer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现记录表
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-04 15:09
 * @description
 */
@Data
@TableName("withdraw_record")
public class WithdrawRecord implements Serializable {
    private static final long serialVersionUID = 3897130651803018968L;
    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 用户标识
     */
    @TableField("CONSUMER_ID")
    private Long consumerId;

    /**
     * 用户编码,生成唯一,用户在存管系统标识
     */
    @TableField("USER_NO")
    private String userNo;

    /**
     * 金额
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 平台佣金
     */
    @TableField("COMMISSION")
    private BigDecimal commission;

    /**
     * 触发时间
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;

    /**
     * 请求流水号
     */
    @TableField("REQUEST_NO")
    private String requestNo;

    /**
     * 回调状态
     */
    @TableField("CALLBACK_STATUS")
    private Integer callbackStatus;

}
