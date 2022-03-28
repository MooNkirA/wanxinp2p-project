package com.moon.wanxinp2p.api.transaction.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 标的信息传输实体类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-05 16:46
 * @description
 */
@Data
public class ProjectDTO {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 发标人用户标识
     */
    private Long consumerId;

    /**
     * 发标人用户编码
     */
    private String userNo;

    /**
     * 标的编码
     */
    private String projectNo;

    /**
     * 标的名称
     */
    private String name;

    /**
     * 标的描述
     */
    private String description;

    /**
     * 标的类型
     */
    private String type;

    /**
     * 标的期限(单位:天)
     */
    private Integer period;

    /**
     * 年化利率(投资人视图)
     */
    private BigDecimal annualRate;

    /**
     * 年化利率(借款人视图)
     */
    private BigDecimal borrowerAnnualRate;

    /**
     * 年化利率(平台佣金，利差)
     */
    private BigDecimal commissionAnnualRate;

    /**
     * 还款方式
     */
    private String repaymentWay;

    /**
     * 募集金额
     */
    private BigDecimal amount;

    /**
     * 标的状态
     */
    private String projectStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 可用状态
     */
    private Integer status;

    /**
     * 是否是债权出让标
     */
    private Integer isAssignment;

    /**
     * 请求流水号
     */
    private String requestNo;

    /**
     * 剩余额度
     */
    private BigDecimal remainingAmount;

    /**
     * 风险等级, 目前默认B
     */
    private String risk = "B";

    /**
     * 出借人数
     */
    private Integer tenderCount;
}
