package com.gitegg.service.system.component;

import com.gitegg.service.system.service.IRoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 容器启动完成加载资源权限数据到缓存
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class InitResourceRolesCacheRunner implements CommandLineRunner {

    private final IRoleResourceService roleResourceService;

    @Override
    public void run(String... args) {

        log.info("InitResourceRolesCacheRunner running");
        // 初始化系统权限和角色的关系
        roleResourceService.initResourceRoles();

    }
}

