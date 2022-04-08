package com.moon.sleuthdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 测试请求控制层
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-07 22:12
 * @description
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("service1")
    public String node1() {
        log.info("sleuth2_service1");
        String sleuth1_service2 =
                restTemplate.getForObject("http://localhost:58011/sleuth1/service2",String.class);
        return "sleuth2_service1--->"+sleuth1_service2 ;
    }

}