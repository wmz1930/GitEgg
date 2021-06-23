package com.gitegg.service.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * gitegg-system 启动类
 */
@ComponentScan(basePackages = "com.gitegg")
@SpringBootApplication
public class GitEggSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitEggSystemApplication.class,args);
    }

}
