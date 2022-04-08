package com.moon.sleuthdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 启动类
 *
 * @author MooNkirA
 * @version 1.0
 * @date 2022-04-07 22:12
 * @description
 */
@SpringBootApplication
public class SleuthDemo1Application {

    public static void main(String[] args) {
        SpringApplication.run(SleuthDemo1Application.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
