package com.moon.shardingjdbcdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-03-03 11:26
 * @description
 */
@SpringBootApplication
@MapperScan("com.moon.shardingjdbcdemo.mapper")
public class ShardingJdbcDemoApp {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcDemoApp.class, args);
    }

}
