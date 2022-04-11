package com.moon.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-10 14:51
 * @description
 */
@SpringBootApplication
@EnableFeignClients
public class ServiceABootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ServiceABootstrap.class, args);
    }

}
