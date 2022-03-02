package com.moon.wanxindepository.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求参数信息基类
 */
@Data
public class BaseRequest {

    @ApiModelProperty("请求业务流水号")
    private String requestNo;

    @ApiModelProperty(value = "应用编码")
    private String appCode;
}
