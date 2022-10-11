package com.gitegg.service.extension;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * gitegg-extension 启动类
 * @author GitEgg
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gitegg")
@ComponentScan(basePackages = "com.gitegg")
@MapperScan(basePackages  = {"com.gitegg.**.mapper.**"})
@SpringBootApplication
public class GitEggExtensionApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggExtensionApplication.class,args);
    }

}
