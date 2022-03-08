package com.moon.wanxinp2p.transaction.agent;

import com.moon.wanxinp2p.api.transaction.model.ProjectDTO;
import com.moon.wanxinp2p.common.domain.RestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 存管代理服务 feign 远程调用接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-07 21:51
 * @description
 */
@FeignClient(value = "depository-agent-service")
public interface DepositoryAgentApiAgent {

    @PostMapping(value = "/depository-agent/l/createProject")
    RestResponse<String> createProject(@RequestBody ProjectDTO projectDTO);

}
