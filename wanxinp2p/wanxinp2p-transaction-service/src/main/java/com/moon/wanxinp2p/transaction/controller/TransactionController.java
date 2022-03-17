package com.moon.wanxinp2p.transaction.controller;

import com.moon.wanxinp2p.api.transaction.TransactionApi;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectInvestDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import com.moon.wanxinp2p.api.transaction.model.TenderDTO;
import com.moon.wanxinp2p.api.transaction.model.TenderOverviewDTO;
import com.moon.wanxinp2p.common.domain.PageVO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.transaction.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
     * 检索标的信息
     *
     * @param projectQueryDTO 封装查询条件
     * @param order           排序的方式
     * @param pageNo          当前页
     * @param pageSize        每页大小
     * @param sortBy          排序的字段
     * @return
     */
    @ApiOperation("检索标的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectQueryDTO", value = "标的信息查询对象", required = true,
                    dataType = "ProjectQueryDTO", paramType = "body"),
            @ApiImplicitParam(name = "order", value = "顺序", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段", required = true,
                    dataType = "string", paramType = "query")})
    @PostMapping("/projects/q")
    @Override
    public RestResponse<PageVO<ProjectDTO>> queryProjects(@RequestBody ProjectQueryDTO projectQueryDTO, String order,
                                                          Integer pageNo, Integer pageSize, String sortBy) {
        return RestResponse.success(projectService.queryProjectsByQueryDTO(projectQueryDTO, order, pageNo, pageSize, sortBy));
    }

    /**
     * 管理员审核标的信息
     *
     * @param id
     * @param approveStatus
     * @return
     */
    @ApiOperation("管理员审核标的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标的id", required = true, dataType = "long", paramType = "path"),
            @ApiImplicitParam(name = "approveStatus", value = "审批状态", required = true, dataType = "ref", paramType = "path")
    })
    @PutMapping("/m/projects/{id}/projectStatus/{approveStatus}")
    @Override
    public RestResponse<String> projectsApprovalStatus(@PathVariable("id") Long id,
                                                       @PathVariable("approveStatus") String approveStatus) {
        return RestResponse.success(projectService.projectsApprovalStatus(id, approveStatus));
    }

    /**
     * 标的信息快速检索
     *
     * @param projectQueryDTO
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param order
     * @return
     */
    @ApiOperation("从ES检索标的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectQueryDTO", value = "标的信息条件对象", required = true, dataType = "ProjectQueryDTO", paramType = "body"),
            @ApiImplicitParam(name = "order", value = "顺序", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段", dataType = "string", paramType = "query")})
    @PostMapping("/projects/indexes/q")
    @Override
    public RestResponse<PageVO<ProjectDTO>> searchProjects(@RequestBody ProjectQueryDTO projectQueryDTO,
                                                           Integer pageNo, Integer pageSize, String sortBy, String order) {
        return RestResponse.success(projectService.queryProjects(projectQueryDTO, order, pageNo, pageSize, sortBy));
    }

    /**
     * 通过ids获取多个标的
     *
     * @param ids
     * @return
     */
    @ApiOperation("通过ids获取多个标的")
    @GetMapping("/projects/{ids}")
    @Override
    public RestResponse<List<ProjectDTO>> queryProjectsIds(@PathVariable String ids) {
        return RestResponse.success(projectService.queryProjectsIds(ids));
    }

    /**
     * 根据标的id查询投标记录
     *
     * @param id 标的id
     * @return
     */
    @ApiOperation("根据标的id查询投标记录")
    @GetMapping("/tenders/projects/{id}")
    @Override
    public RestResponse<List<TenderOverviewDTO>> queryTendersByProjectId(@PathVariable Long id) {
        return RestResponse.success(projectService.queryTendersByProjectId(id));
    }

    /**
     * 用户投标
     *
     * @param projectInvestDTO 投标信息
     * @return
     */
    @ApiOperation("用户投标")
    @ApiImplicitParam(name = "projectInvestDTO", value = "投标信息", required = true,
            dataType = "ProjectInvestDTO", paramType = "body")
    @PostMapping("/my/tenders")
    @Override
    public RestResponse<TenderDTO> createTender(@RequestBody ProjectInvestDTO projectInvestDTO) {
        return RestResponse.success(projectService.createTender(projectInvestDTO));
    }
}
