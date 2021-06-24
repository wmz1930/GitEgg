package com.gitegg.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.gitegg")
@ComponentScan(basePackages = "com.gitegg")
@EnableDiscoveryClient
@SpringBootApplication
@EnableCaching
public class GitEggOAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitEggOAuthApplication.class);
    }
}
