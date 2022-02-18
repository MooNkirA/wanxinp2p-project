package com.moon.wanxinp2p.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 10:54
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AccountServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApp.class, args);
    }

}
