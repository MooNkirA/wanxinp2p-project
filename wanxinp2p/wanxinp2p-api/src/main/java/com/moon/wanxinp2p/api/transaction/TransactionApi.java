package com.moon.wanxinp2p.api.transaction;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
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

}
