package com.gitegg.code.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * gitegg-code-generator 启动类
 * @author GitEgg
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gitegg")
@ComponentScan(basePackages = "com.gitegg")
@MapperScan(basePackages  = {"com.gitegg.**.mapper.**"})
@SpringBootApplication
@RefreshScope
public class GitEggCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggCodeApplication.class,args);
    }

}
