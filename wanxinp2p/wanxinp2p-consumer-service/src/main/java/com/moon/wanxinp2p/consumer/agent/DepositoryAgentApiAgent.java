package com.moon.wanxinp2p.consumer.agent;

import com.moon.wanxinp2p.api.consumer.model.ConsumerRequest;
import com.moon.wanxinp2p.api.depository.model.GatewayRequest;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 存管代理服务 Feign 代理接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-27 11:42
 * @description
 */
@FeignClient("depository-agent-service") // 服务 id
public interface DepositoryAgentApiAgent {

    /**
     * 生成开户数据
     *
     * @param consumerRequest
     * @return
     */
    @PostMapping("/depository-agent/l/consumers")
    RestResponse<GatewayRequest> createConsumer(@RequestBody ConsumerRequest consumerRequest);

}