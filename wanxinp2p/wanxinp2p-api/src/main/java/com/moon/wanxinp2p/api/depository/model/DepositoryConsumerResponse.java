package com.moon.wanxinp2p.api.depository.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 开户返回参数信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-28 22:21
 * @description
 */
@Data
public class DepositoryConsumerResponse extends DepositoryBaseResponse {
    private static final long serialVersionUID = -8497030469733828972L;

    @ApiModelProperty("银行代码")
    private String bankCode;

    @ApiModelProperty("银行名称")
    private String bankName;

}
