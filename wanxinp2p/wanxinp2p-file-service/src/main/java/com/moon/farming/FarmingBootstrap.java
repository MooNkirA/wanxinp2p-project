package com.moon.farming;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.moon.farming")
public class FarmingBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(FarmingBootstrap.class, args);
    }

}
