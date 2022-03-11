package com.moon.wanxinp2p.transaction.service;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import com.moon.wanxinp2p.common.domain.PageVO;

import java.util.List;

/**
 * 交易中心 标的服务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-05 17:13
 * @description
 */
public interface ProjectService {

    /**
     * 创建标的
     *
     * @param projectDTO
     * @return ProjectDTO
     */
    ProjectDTO createProject(ProjectDTO projectDTO);

    /**
     * 根据分页条件检索标的信息
     *
     * @param projectQueryDTO 封装查询条件
     * @param order           排序的方式
     * @param pageNo          当前页
     * @param pageSize        每页大小
     * @param sortBy          排序的字段
     * @return
     */
    PageVO<ProjectDTO> queryProjectsByQueryDTO(ProjectQueryDTO projectQueryDTO, String order,
                                               Integer pageNo, Integer pageSize, String sortBy);

    /**
     * 管理员审核标的信息
     *
     * @param id
     * @param approveStatus
     * @return
     */
    String projectsApprovalStatus(Long id, String approveStatus);

    /**
     * 标的信息检索
     *
     * @param projectQueryDTO
     * @param order
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    PageVO<ProjectDTO> queryProjects(ProjectQueryDTO projectQueryDTO, String order,
                                     Integer pageNo, Integer pageSize, String sortBy);

    /**
     * 通过ids获取多个标的
     *
     * @param ids 多个标的id字符串，不同id之间使用逗号分隔
     * @return
     */
    List<ProjectDTO> queryProjectsIds(String ids);

}
