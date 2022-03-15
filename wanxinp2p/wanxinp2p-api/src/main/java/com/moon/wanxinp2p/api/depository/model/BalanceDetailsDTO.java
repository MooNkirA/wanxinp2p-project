package com.moon.wanxinp2p.api.depository.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-14 23:05
 * @description
 */
@Data
@ApiModel(value = "BalanceChangeDTO", description = "用户余额信息")
public class BalanceDetailsDTO implements Serializable {

    private static final long serialVersionUID = -1067638799868173167L;
    @ApiModelProperty(value = "用户标识")
    private Long consumerId;

    @ApiModelProperty(value = "用户编码,生成唯一,用户在存管系统标识")
    private String userNo;

    @ApiModelProperty(value = "账户变动类型.1.增加.2.减少.3.冻结.4解冻")
    private Integer changeType;

    @ApiModelProperty(value = "冻结金额")
    private BigDecimal freezeAmount;

    @ApiModelProperty(value = "可用余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "应用编码")
    private String appCode;

    @ApiModelProperty(value = "账户变动时间")
    private LocalDateTime createDate;

}
