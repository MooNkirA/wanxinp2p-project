package com.moon.wanxinp2p.api.search;

import com.moon.wanxinp2p.api.search.model.ProjectQueryParamsDTO;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.PageVO;
import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 内容检索服务API
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-10 23:24
 * @description
 */
public interface ContentSearchApi {

    /**
     * 检索标的
     *
     * @param projectQueryParamsDTO
     * @return
     */
    RestResponse<PageVO<ProjectDTO>> queryProjectIndex(ProjectQueryParamsDTO projectQueryParamsDTO,
                                                       Integer pageNo, Integer pageSize, String sortBy, String order);

}
