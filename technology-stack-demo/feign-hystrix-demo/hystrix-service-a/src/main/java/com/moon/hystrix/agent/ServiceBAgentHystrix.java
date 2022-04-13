package com.moon.hystrix.agent;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 服务B 远程调用 fegin 接口的降级实现类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-10 14:57
 * @description
 */
@Component
public class ServiceBAgentHystrix implements FallbackFactory<ServiceBAgent> {

    /**
     * 服务降级时调用的方法
     *
     * @param cause
     * @return
     */
    @Override
    public ServiceBAgent create(Throwable cause) {
        return new ServiceBAgent() {
            @Override
            public String service() {
                System.out.println(cause.getMessage()); // 记录日志
                // 实际项目中，此处应该是一堆业务逻辑
                return "service-b 熔断...";
            }
        };
    }

}
