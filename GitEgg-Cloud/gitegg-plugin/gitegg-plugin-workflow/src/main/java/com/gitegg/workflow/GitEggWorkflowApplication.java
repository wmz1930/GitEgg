package com.gitegg.workflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author GitEgg
 */
@EnableFeignClients(basePackages = "com.gitegg")
@ComponentScan(basePackages = "com.gitegg")
@MapperScan(basePackages  = {"com.gitegg.**.mapper.**"})
@EnableDiscoveryClient
@EnableCaching
@RefreshScope
@SpringBootApplication
public class GitEggWorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggWorkflowApplication.class, args);
    }

}