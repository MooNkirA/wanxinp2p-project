package com.moon.wanxindepository.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 放款确认请求返回信息
 */
@Data
@ApiModel(value = "ConfirmRepaymentResponse", description = "放款确认请求返回信息")
public class ConfirmRepaymentResponse extends BaseResponse {

}
