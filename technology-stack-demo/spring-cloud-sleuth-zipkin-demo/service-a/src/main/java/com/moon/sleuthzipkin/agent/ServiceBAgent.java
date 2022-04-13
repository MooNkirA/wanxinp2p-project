package com.moon.sleuthzipkin.agent;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 服务B远程调用代理
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-08 18:41
 * @description
 */
@FeignClient(name = "sleuth-with-zipkin-service-b")
public interface ServiceBAgent {

    @GetMapping("/service-b/service")
    String service();

}
