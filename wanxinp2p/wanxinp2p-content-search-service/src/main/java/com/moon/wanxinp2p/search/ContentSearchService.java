package com.moon.wanxinp2p.search;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.moon.wanxinp2p.search", "com.moon.wanxinp2p.common"})
public class ContentSearchService {

    public static void main(String[] args) {
        SpringApplication.run(ContentSearchService.class, args);
    }

}
