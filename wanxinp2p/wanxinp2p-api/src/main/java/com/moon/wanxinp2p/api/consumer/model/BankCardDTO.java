package com.moon.wanxinp2p.api.consumer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 银行卡信息传输
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 11:53
 * @description
 */
@Data
@ApiModel(value = "BankCardDTO", description = "银行卡信息")
public class BankCardDTO {

    @ApiModelProperty("标识")
    private Long id;

    @ApiModelProperty("用户标识")
    private Long consumerId;

    @ApiModelProperty("用户实名")
    private String fullName;

    @ApiModelProperty("银行编码")
    private String bankCode;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行卡号")
    private String cardNumber;

    @ApiModelProperty("银行预留手机号")
    private String mobile;

    @ApiModelProperty("可用状态")
    private Integer status;

}
