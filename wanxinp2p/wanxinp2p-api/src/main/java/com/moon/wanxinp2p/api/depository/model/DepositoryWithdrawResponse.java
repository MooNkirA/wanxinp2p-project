package com.moon.wanxinp2p.api.depository.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现返回参数信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-28 22:21
 * @description
 */
@Data
public class DepositoryWithdrawResponse extends DepositoryBaseResponse {
    private static final long serialVersionUID = -843089688089364212L;

    @ApiModelProperty("交易状态")
    private String transactionStatus;

}
