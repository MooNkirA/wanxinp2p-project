package com.moon.wanxinp2p.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.moon.wanxinp2p.consumer.agent"})
@ComponentScan(basePackages = {"com.moon.wanxinp2p.common", "com.moon.wanxinp2p.consumer"}) // 扫描接口，common工程所在包
public class ConsumerService {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerService.class, args);
    }

}