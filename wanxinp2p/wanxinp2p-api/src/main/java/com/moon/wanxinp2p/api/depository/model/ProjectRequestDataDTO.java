package com.moon.wanxinp2p.api.depository.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行存管系统创建标的reqData参数实体
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-08 9:07
 * @description
 */
@Data
public class ProjectRequestDataDTO implements Serializable {
    private static final long serialVersionUID = -3703792846330385051L;

    /**
     * 请求流水号
     */
    private String requestNo;
    /**
     * 平台用户编号
     */
    private String userNo;
    /**
     * 标的号
     */
    private String projectNo;
    /**
     * 标的金额
     */
    private BigDecimal projectAmount;
    /**
     * 标的名称
     */
    private String projectName;
    /**
     * 标的描述
     */
    private String projectDesc;
    /**
     * 标的产品类型
     */
    private String projectType;
    /**
     * 标的期限
     */
    private Integer projectPeriod;
    /**
     * 年化利率( 5%只传5 )
     */
    private BigDecimal annualRate;
    /**
     * 还款方式
     */
    private String repaymentWay;
    /**
     * 是否是债权出让标
     */
    private Integer isAssignment;
}
