package com.moon.wanxinp2p.repayment.service;

import com.moon.wanxinp2p.api.repayment.model.ProjectWithTendersDTO;

/**
 * 还款业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-19 22:04
 * @description
 */
public interface RepaymentService {

    /**
     * 启动还款
     *
     * @param projectWithTendersDTO
     * @return
     */
    String startRepayment(ProjectWithTendersDTO projectWithTendersDTO);

}
