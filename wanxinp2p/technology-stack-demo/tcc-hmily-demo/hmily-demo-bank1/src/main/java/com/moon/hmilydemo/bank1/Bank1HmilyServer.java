package com.moon.hmilydemo.bank1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:16
 * @description
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class, scanBasePackages = {"com.moon.hmilydemo.bank1", "org.dromara.hmily"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.moon.hmilydemo.bank1.feignClient"})
public class Bank1HmilyServer {

    public static void main(String[] args) {
        SpringApplication.run(Bank1HmilyServer.class, args);
    }

}
