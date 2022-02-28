package com.moon.wanxinp2p.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
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

}