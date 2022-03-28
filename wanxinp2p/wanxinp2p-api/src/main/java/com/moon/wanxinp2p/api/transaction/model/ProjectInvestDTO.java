package com.moon.wanxinp2p.api.transaction.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 投标信息
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-14 22:10
 * @description
 */
@Data
@ApiModel(value = "ProjectInvestDTO", description = "用户投标信息")
public class ProjectInvestDTO {

    @ApiModelProperty("标的标识")
    private Long id;

    @ApiModelProperty("投标金额")
    private String amount;

}