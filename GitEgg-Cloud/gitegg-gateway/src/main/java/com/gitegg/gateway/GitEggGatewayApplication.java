package com.gitegg.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@ComponentScan(basePackages = "com.gitegg")
@SpringBootApplication
@EnableCaching
public class GitEggGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitEggGatewayApplication.class,args);
    }
}
