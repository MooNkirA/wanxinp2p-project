package com.moon.wanxinp2p.api.transaction;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import com.moon.wanxinp2p.api.transaction.model.TenderOverviewDTO;
import com.moon.wanxinp2p.common.domain.PageVO;
import com.moon.wanxinp2p.common.domain.RestResponse;

import java.util.List;

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

    /**
     * 管理员审核标的信息
     *
     * @param id
     * @param approveStatus
     * @return
     */
    RestResponse<String> projectsApprovalStatus(Long id, String approveStatus);

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
    RestResponse<PageVO<ProjectDTO>> searchProjects(ProjectQueryDTO projectQueryDTO,
                                                    Integer pageNo, Integer pageSize, String sortBy, String order);

    /**
     * 通过ids获取多个标的
     *
     * @param ids 多个标的id，使用逗号分隔的字符串
     * @return
     */
    RestResponse<List<ProjectDTO>> queryProjectsIds(String ids);

    /**
     * 根据标的id查询投标记录
     *
     * @param id 标的id
     * @return
     */
    RestResponse<List<TenderOverviewDTO>> queryTendersByProjectId(Long id);
}
