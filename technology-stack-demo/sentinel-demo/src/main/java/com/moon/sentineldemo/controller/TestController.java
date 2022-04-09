package com.moon.sentineldemo.controller;

import com.moon.sentineldemo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试请求控制层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-09 16:07
 * @description
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("hello")
    public String hello() {
        return testService.hello();
    }

}

