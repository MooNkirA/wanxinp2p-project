package com.moon.wanxinp2p.api.repayment;

import com.moon.wanxinp2p.api.repayment.model.ProjectWithTendersDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 还款服务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-18 16:46
 * @description
 */
public interface RepaymentApi {

    /**
     * 启动还款
     *
     * @param projectWithTendersDTO
     * @return
     */
    RestResponse<String> startRepayment(ProjectWithTendersDTO projectWithTendersDTO);

}
