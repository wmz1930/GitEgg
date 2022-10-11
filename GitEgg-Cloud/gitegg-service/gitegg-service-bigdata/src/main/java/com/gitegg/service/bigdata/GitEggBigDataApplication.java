package com.gitegg.service.bigdata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * gitegg-bigdata 启动类
 * @author gitegg
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gitegg")
@ComponentScan(basePackages = "com.gitegg")
@MapperScan(basePackages  = {"com.gitegg.**.mapper.**"})
@SpringBootApplication
public class GitEggBigDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggBigDataApplication.class,args);
    }

}
