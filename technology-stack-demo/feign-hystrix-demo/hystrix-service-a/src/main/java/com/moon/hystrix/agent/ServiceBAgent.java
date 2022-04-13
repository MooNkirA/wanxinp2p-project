package com.moon.hystrix.agent;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 服务B远程调用代理
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-10 14:53
 * @description
 */
@FeignClient(name = "feign-hystrix-service-b", fallbackFactory = ServiceBAgentHystrix.class)
public interface ServiceBAgent {

    @GetMapping("/service-b/service")
    String service();

}
