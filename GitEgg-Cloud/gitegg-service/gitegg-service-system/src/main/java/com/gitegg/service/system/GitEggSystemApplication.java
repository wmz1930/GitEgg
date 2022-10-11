package com.gitegg.service.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * gitegg-system 启动类
 * @author gitegg
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gitegg")
@ComponentScan(basePackages = "com.gitegg")
@MapperScan(basePackages  = {"com.gitegg.**.mapper.**"})
@SpringBootApplication
public class GitEggSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggSystemApplication.class,args);
    }

}
