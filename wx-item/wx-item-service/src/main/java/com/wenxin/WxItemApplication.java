package com.wenxin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.wenxin.item.mapper")
public class WxItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(WxItemApplication.class, args);
    }
}
