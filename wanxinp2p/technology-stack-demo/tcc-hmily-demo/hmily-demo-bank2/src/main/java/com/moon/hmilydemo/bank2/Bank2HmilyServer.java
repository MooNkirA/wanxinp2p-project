package com.moon.hmilydemo.bank2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-25 9:16
 * @description
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class, scanBasePackages = {"com.moon.hmilydemo.bank2", "org.dromara.hmily"})
@EnableDiscoveryClient
public class Bank2HmilyServer {

    public static void main(String[] args) {
        SpringApplication.run(Bank2HmilyServer.class, args);
    }

}
