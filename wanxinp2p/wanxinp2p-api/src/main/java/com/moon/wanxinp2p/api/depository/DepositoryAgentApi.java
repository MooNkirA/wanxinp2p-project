package com.moon.wanxinp2p.api.depository;

import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;

/**
 * 银行存管系统代理服务API
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-26 22:46
 * @description
 */
public interface DepositoryAgentApi {

    /**
     * 开通存管账户
     *
     * @param consumerRequest 开户信息
     * @return
     */
    RestResponse<GatewayRequest> createConsumer(ConsumerRequest consumerRequest);

}
