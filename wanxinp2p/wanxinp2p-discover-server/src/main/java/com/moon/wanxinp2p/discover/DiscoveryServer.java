
package com.moon.wanxinp2p.discover;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServer {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServer.class, args);

	}
}
