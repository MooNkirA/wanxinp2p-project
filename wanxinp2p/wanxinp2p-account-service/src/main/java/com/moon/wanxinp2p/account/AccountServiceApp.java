package com.moon.wanxinp2p.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-02-18 10:54
 * @description
 */
@SpringBootApplication(
        exclude = MongoAutoConfiguration.class,
        // 因为本工程的扫描的包路径不包含全局的异常处理类所在的包，所以这里需要增加包扫描的路径范围，扫描接口common工程所在包
        // 再增加扫描 hmily 的包路径
        scanBasePackages = {"org.dromara.hmily", "com.moon.wanxinp2p"}
)
@EnableDiscoveryClient
public class AccountServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApp.class, args);
    }

}
