package com.moon.wanxinp2p.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

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
// 因为本工程的扫描的包路径不包含全局的异常处理类所在的包，所以这里需要增加包扫描的路径范围
@ComponentScan({"com.moon.wanxinp2p"}) // 扫描接口，common工程所在包
public class AccountServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApp.class, args);
    }

}
