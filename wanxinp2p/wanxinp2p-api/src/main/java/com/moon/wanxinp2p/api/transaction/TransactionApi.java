package com.moon.wanxinp2p.api.transaction;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import com.moon.wanxinp2p.common.domain.PageVO;
import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 交易中心服务API
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-05 16:43
 * @description
 */
public interface TransactionApi {

    /**
     * 借款人发标（新增标的）
     *
     * @param projectDTO
     * @return
     */
    RestResponse<ProjectDTO> createProject(ProjectDTO projectDTO);

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
    RestResponse<PageVO<ProjectDTO>> queryProjects(ProjectQueryDTO projectQueryDTO, String order,
                                                   Integer pageNo, Integer pageSize, String sortBy);

}
