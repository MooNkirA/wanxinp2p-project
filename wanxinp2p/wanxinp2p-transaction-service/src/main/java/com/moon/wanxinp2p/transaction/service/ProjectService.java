package com.moon.wanxinp2p.transaction.service;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectQueryDTO;
import com.moon.wanxinp2p.common.domain.PageVO;

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

}
