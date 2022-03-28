package com.moon.wanxinp2p.api.depository.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 银行存管系统返回str, 转换的json对象
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-07 23:26
 * @description
 */
@Data
public class DepositoryResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = 6239079119416764759L;

    /**
     * 业务数据报文，JSON格式
     */
    @ApiModelProperty("业务数据报文，JSON格式")
    private T respData;
    /**
     * 签名
     */
    @ApiModelProperty("签名数据")
    private String signature;

}
