package com.gitegg.service.system.component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.service.IResourceService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 容器启动完成加载资源权限数据到缓存
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class InitResourceRolesCacheRunner implements CommandLineRunner {

    private final RedisTemplate redisTemplate;

    private final IResourceService resourceService;

    /**
     * 是否开启租户模式
     */
    @Value(("${tenant.enable}"))
    private Boolean enable;

    @Override
    public void run(String... args) {

        log.info("InitResourceRolesCacheRunner running");

        // 查询系统角色和权限的关系
        List<Resource> resourceList = resourceService.queryResourceRoleIds();

        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            Map<Long, List<Resource>> resourceListMap =
                resourceList.stream().collect(Collectors.groupingBy(Resource::getTenantId));
            resourceListMap.forEach((key, value) -> {
                String redisKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY + key;
                redisTemplate.delete(redisKey);
                addRoleResource(redisKey, value);
            });
        } else {
            redisTemplate.delete(AuthConstant.RESOURCE_ROLES_KEY);
            addRoleResource(AuthConstant.RESOURCE_ROLES_KEY, resourceList);
        }
    }

    private void addRoleResource(String key, List<Resource> resourceList) {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        Optional.ofNullable(resourceList).orElse(new ArrayList<>()).forEach(resource -> {
            // roleId -> ROLE_{roleId}
            List<String> roles = Optional.ofNullable(resource.getRoleIds()).orElse(new ArrayList<>()).stream()
                .map(roleId -> AuthConstant.AUTHORITY_PREFIX + roleId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(roles)) {
                resourceRolesMap.put(resource.getResourceUrl(), roles);
            }
        });
        redisTemplate.opsForHash().putAll(key, resourceRolesMap);
    }
}

