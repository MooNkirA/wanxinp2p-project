package com.moon.wanxinp2p.transaction;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.moon.wanxinp2p.common", "com.moon.wanxinp2p.transaction"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.moon.wanxinp2p.transaction.agent"})
public class TransactionService {

    public static void main(String[] args) {
        SpringApplication.run(TransactionService.class, args);
    }

}

