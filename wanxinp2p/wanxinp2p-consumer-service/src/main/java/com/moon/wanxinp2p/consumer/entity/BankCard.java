package com.moon.wanxinp2p.consumer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * bank_card 表，用户绑定银行卡信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 11:34
 * @description
 */
@Data
@TableName("bank_card")
public class BankCard implements Serializable {
    private static final long serialVersionUID = -2819439011850024960L;

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
     * 银行编码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 银行名称
     */
    @TableField("BANK_NAME")
    private String bankName;

    /**
     * 银行卡号
     */
    @TableField("CARD_NUMBER")
    private String cardNumber;

    /**
     * 银行预留手机号
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 可用状态
     */
    @TableField("STATUS")
    private Integer status;

}
