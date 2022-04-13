package com.moon.sleuthzipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-08 18:39
 * @description
 */
@SpringCloudApplication
@EnableFeignClients
public class ServiceB {

    public static void main(String[] args) {
        SpringApplication.run(ServiceB.class, args);
    }

}
