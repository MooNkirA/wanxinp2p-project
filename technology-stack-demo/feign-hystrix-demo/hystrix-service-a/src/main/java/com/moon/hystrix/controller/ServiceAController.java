package com.moon.hystrix.controller;

import com.moon.hystrix.agent.ServiceBAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试请求层
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-10 15:39
 * @description
 */
@RestController
public class ServiceAController {

    @Autowired
    private ServiceBAgent serviceBAgent;

    @GetMapping("service")
    public String service(){
        return serviceBAgent.service();
    }
}
