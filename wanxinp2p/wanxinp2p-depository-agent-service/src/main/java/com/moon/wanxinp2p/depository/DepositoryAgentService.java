package com.moon.wanxinp2p.depository;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(
        scanBasePackages = {"com.moon.wanxinp2p.depository", "com.moon.wanxinp2p.common"},
        exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)
@EnableDiscoveryClient
@EnableTransactionManagement
public class DepositoryAgentService {

    public static void main(String[] args) {
        SpringApplication.run(DepositoryAgentService.class, args);
    }

}
