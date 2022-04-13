package com.moon.hystrix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试请求层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-10 15:28
 * @description
 */
@RestController
public class ServiceBController {

    @GetMapping("service")
    public String service() {
        return "Service B is running";
    }

}
