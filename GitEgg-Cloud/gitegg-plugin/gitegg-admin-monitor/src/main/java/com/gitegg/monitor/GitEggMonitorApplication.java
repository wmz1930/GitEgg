package com.gitegg.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author GitEgg
 */
@EnableAdminServer
@SpringBootApplication
@RefreshScope
public class GitEggMonitorApplication {
    
    public static void main(String[] args)
    {
        SpringApplication.run(GitEggMonitorApplication.class, args);
    }
    
}