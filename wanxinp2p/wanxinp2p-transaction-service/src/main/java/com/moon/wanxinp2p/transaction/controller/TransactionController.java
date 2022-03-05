package com.moon.wanxinp2p.transaction.controller;

import com.moon.wanxinp2p.api.transaction.TransactionApi;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.transaction.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易服务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-05 22:19
 * @description
 */
@Api(value = "交易中心服务", tags = "transaction")
@RestController
public class TransactionController implements TransactionApi {

    @Autowired
    private ProjectService projectService;

    /**
     * 借款人发标（新增标的）
     *
     * @param projectDTO
     * @return
     */
    @ApiOperation("借款人发标")
    @ApiImplicitParam(name = "project", value = "标的信息",
            required = true, dataType = "Project", paramType = "body")
    @PostMapping("/my/projects")
    @Override
    public RestResponse<ProjectDTO> createProject(ProjectDTO projectDTO) {
        return RestResponse.success(projectService.createProject(projectDTO));
    }

}
