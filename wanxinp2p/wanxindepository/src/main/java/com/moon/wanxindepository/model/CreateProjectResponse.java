package com.moon.wanxindepository.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 创建标的返回数据
 */
@Data
@ApiModel(value = "CreateProjectResponse", description = "创建标的返回数据")
public class CreateProjectResponse extends BaseResponse {
    public CreateProjectResponse(String requestNo) {
        super(requestNo);
    }
}
