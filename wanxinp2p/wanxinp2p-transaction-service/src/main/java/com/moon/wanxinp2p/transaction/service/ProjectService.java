package com.moon.wanxinp2p.transaction.service;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;

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

}
