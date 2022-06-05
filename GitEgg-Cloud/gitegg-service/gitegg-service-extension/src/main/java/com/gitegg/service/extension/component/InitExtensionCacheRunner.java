package com.gitegg.service.extension.component;

import com.gitegg.service.extension.justauth.service.IJustAuthConfigService;
import com.gitegg.service.extension.justauth.service.IJustAuthSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 容器启动完成加载资源权限数据到缓存
 * @author GitEgg
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class InitExtensionCacheRunner implements CommandLineRunner {
    
    private final IJustAuthConfigService justAuthConfigService;
    
    private final IJustAuthSourceService justAuthSourceService;

    @Override
    public void run(String... args) {

        log.info("InitExtensionCacheRunner running");
    
    
        // 初始化第三方登录主配置
        justAuthConfigService.initJustAuthConfigList();

        // 初始化第三方登录 第三方配置
        justAuthSourceService.initJustAuthSourceList();


    }
}

