package com.moon.wanxinp2p.api.repayment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 还款请求信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-27 21:35
 * @description
 */
@Data
@ApiModel(value = "RepaymentRequest", description = "还款请求信息")
public class RepaymentRequest {

    @ApiModelProperty("请求流水号")
    private String requestNo;

    @ApiModelProperty("预处理业务流水号")
    private String preRequestNo;

    @ApiModelProperty("标的编码")
    private String projectNo;

    @ApiModelProperty("平台佣金 -- 具体金额")
    private BigDecimal commission;

    @ApiModelProperty("放款明细")
    private List<RepaymentDetailRequest> details;

    @ApiModelProperty("业务id")
    private Long id;

    @ApiModelProperty("还款总额")
    private BigDecimal amount;
}
