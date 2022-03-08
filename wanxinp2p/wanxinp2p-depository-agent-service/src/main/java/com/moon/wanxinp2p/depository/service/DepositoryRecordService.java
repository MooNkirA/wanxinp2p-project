package com.moon.wanxinp2p.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.DepositoryBaseResponse;
import com.moon.wanxinp2p.api.depository.model.DepositoryResponseDTO;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.depository.entity.DepositoryRecord;

/**
 * 存管记录业务接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 23:06
 * @description
 */
public interface DepositoryRecordService extends IService<DepositoryRecord> {

    /**
     * 开通存管账户
     *
     * @param consumerRequest 开户信息
     * @return
     */
    GatewayRequest createConsumer(ConsumerRequest consumerRequest);


    /**
     * 根据请求流水号更新请求状态
     *
     * @param requestNo
     * @param requestsStatus
     * @return
     */
    Boolean modifyRequestStatus(String requestNo, Integer requestsStatus);

    /**
     * 保存标的
     *
     * @param projectDTO
     * @return
     */
    DepositoryResponseDTO<DepositoryBaseResponse> createProject(ProjectDTO projectDTO);
}
