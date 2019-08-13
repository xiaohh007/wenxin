package com.wenxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WxRegistry {
    public static void main(String[] args) {
        SpringApplication.run(WxRegistry.class, args);
    }
}
