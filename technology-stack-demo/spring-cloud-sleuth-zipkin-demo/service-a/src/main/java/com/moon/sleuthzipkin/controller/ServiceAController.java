package com.moon.sleuthzipkin.controller;

import com.moon.sleuthzipkin.agent.ServiceBAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-08 18:41
 * @description
 */
@RestController
public class ServiceAController {

    @Autowired
    private ServiceBAgent serviceBAgent;

    @GetMapping("service1")
    public String service1() {
        String serviceb = serviceBAgent.service();
        return "service-a-service1" + "--->" + serviceb;
    }

    @GetMapping("service2")
    public String service2() {
        return "service-a-service2";
    }

}
