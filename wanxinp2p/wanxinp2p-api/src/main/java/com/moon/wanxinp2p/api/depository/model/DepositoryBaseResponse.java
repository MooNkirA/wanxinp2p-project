package com.moon.wanxinp2p.api.depository.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 银行存管系统服务返回参数信息基类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-28 22:21
 * @description
 */
@Data
@Accessors(chain = true)
public class DepositoryBaseResponse implements Serializable {
    private static final long serialVersionUID = -7246876227576512200L;

    @ApiModelProperty("返回码")
    private String respCode;

    @ApiModelProperty("描述信息")
    private String respMsg;

    @ApiModelProperty("交易状态")
    private Integer status;

    @ApiModelProperty("请求流水号")
    private String requestNo;

}
