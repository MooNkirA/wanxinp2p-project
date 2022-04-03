package com.moon.wanxinp2p.search.service;

import com.moon.wanxinp2p.api.search.model.ProjectQueryParamsDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.PageVO;

/**
 * 标的检索业务层接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-11 15:36
 * @description
 */
public interface ProjectIndexService {

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
    PageVO<ProjectDTO> queryProjectIndex(ProjectQueryParamsDTO projectQueryParamsDTO,
                                         Integer pageNo, Integer pageSize, String sortBy, String order);

}
