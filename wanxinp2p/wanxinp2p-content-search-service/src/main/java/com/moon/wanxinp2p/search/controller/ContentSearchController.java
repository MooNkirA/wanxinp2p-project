package com.moon.wanxinp2p.search.controller;

import com.moon.wanxinp2p.api.search.ContentSearchApi;
import com.moon.wanxinp2p.api.search.model.ProjectQueryParamsDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.PageVO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import com.moon.wanxinp2p.search.service.ProjectIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检索服务API
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-11 15:41
 * @description
 */
@Api(value = "检索服务", tags = "ContentSearch", description = "检索服务API")
@RestController
public class ContentSearchController implements ContentSearchApi {

    @Autowired
    private ProjectIndexService projectIndexService;

    /**
     * 检索标的
     *
     * @param projectQueryParamsDTO
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param order
     * @return
     */
    @ApiOperation("检索标的")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectQueryParamsDTO", value = "标的检索参数", required = true, dataType = "ProjectQueryParamsDTO", paramType = "body"),
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sortBy", value = "排序字段", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "顺序", dataType = "String", paramType = "query")})
    @PostMapping(value = "/l/projects/indexes/q")
    @Override
    public RestResponse<PageVO<ProjectDTO>> queryProjectIndex(@RequestBody ProjectQueryParamsDTO projectQueryParamsDTO,
                                                              @RequestParam Integer pageNo, @RequestParam Integer pageSize,
                                                              @RequestParam(required = false) String sortBy,
                                                              @RequestParam(required = false) String order) {
        return RestResponse.success(projectIndexService.queryProjectIndex(projectQueryParamsDTO, pageNo, pageSize, sortBy, order));
    }
}
