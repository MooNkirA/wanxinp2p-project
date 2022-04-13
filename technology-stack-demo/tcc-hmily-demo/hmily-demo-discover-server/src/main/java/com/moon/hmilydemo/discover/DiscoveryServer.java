package com.moon.hmilydemo.discover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-24 16:56
 * @description
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServer {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServer.class, args);
    }

}
