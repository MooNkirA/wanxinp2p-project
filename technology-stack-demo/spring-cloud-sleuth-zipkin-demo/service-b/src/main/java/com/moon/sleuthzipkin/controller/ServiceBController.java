package com.moon.sleuthzipkin.controller;

import com.moon.sleuthzipkin.agent.ServiceAAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-08 18:59
 * @description
 */
@RestController
public class ServiceBController {

    @Autowired
    private ServiceAAgent serviceAAgent;

    @GetMapping("service")
    public String service() {
        String service2 = serviceAAgent.service2();
        // int a = 10 / 0; // 模拟异常
        return "service-b-service" + "--->" + service2;
    }

}
