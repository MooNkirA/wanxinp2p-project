package com.moon.wanxindepository.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新标的状态返回信息
 */
@Data
@ApiModel(value = "ModifyProjectResponse", description = "更新标的状态返回信息")
public class ModifyProjectResponse extends BaseResponse {

    @ApiModelProperty("标的状态")
    private String projectStatus;

    public ModifyProjectResponse(String requestNo) {
        super(requestNo);
    }
}
