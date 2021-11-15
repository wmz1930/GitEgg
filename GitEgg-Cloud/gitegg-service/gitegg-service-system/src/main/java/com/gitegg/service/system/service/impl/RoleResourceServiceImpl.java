package com.gitegg.service.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.service.system.dto.UpdateRoleResourceDTO;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.entity.Role;
import com.gitegg.service.system.entity.RoleResource;
import com.gitegg.service.system.mapper.RoleResourceMapper;
import com.gitegg.service.system.service.IResourceService;
import com.gitegg.service.system.service.IRoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gitegg
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource>
        implements IRoleResourceService {

    /**
     * 是否开启租户模式
     */
    @Value(("${tenant.enable}"))
    private Boolean enable;

    private final IResourceService resourceService;

    private final RedisTemplate redisTemplate;

    @Override
//    @Cacheable(value = "resources", key = "'role_id_'.concat(#roleId)")
    public List<Resource> queryResourceByRoleId(Long roleId) {
        QueryWrapper<RoleResource> ew = new QueryWrapper<>();
        ew.eq("role_id", roleId);
        List<RoleResource> roleResourceList = this.list(ew);
        if (!CollectionUtils.isEmpty(roleResourceList)) {
            List<Long> resourceIds = new ArrayList<>();
            for (RoleResource roleResource : roleResourceList) {
                resourceIds.add(roleResource.getResourceId());
            }
            QueryWrapper<Resource> ewResource = new QueryWrapper<>();
            ewResource.in("id", resourceIds);
            List<Resource> resourceList = resourceService.list(ewResource);
            return resourceList;
        } else {
            return null;
        }
    }

    @Override
//    @CacheEvict(value = "resources", allEntries = true)
    public boolean updateList(UpdateRoleResourceDTO updateRoleResource) {
        List<RoleResource> addList = updateRoleResource.getAddResources();
        if (!CollectionUtils.isEmpty(addList)) {
            this.saveBatch(addList);
        }
        List<RoleResource> delList = updateRoleResource.getDelResources();
        if (!CollectionUtils.isEmpty(delList)) {
            List<Long> resIdList = new ArrayList<>();
            for (RoleResource rr : delList) {
                resIdList.add(rr.getResourceId());
            }
            Long roleId = updateRoleResource.getRoleId();
            QueryWrapper<RoleResource> ewResource = new QueryWrapper<>();
            ewResource.eq("role_id", roleId).in("resource_id", resIdList);
            this.remove(ewResource);
        }
        //重新初始化角色和权限的对应关系
        this.initResourceRoles();
        return true;
    }

    @Override
    public void initResourceRoles() {
        // 查询系统角色和权限的关系
        List<Resource> resourceList = resourceService.queryResourceRoles();
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
            // roleKey -> ROLE_{roleKey}
            List<String> roleKeys = Optional.ofNullable(resource.getRoles()).orElse(new ArrayList<>()).stream().map(Role::getRoleKey)
                    .distinct().map(roleKey -> AuthConstant.AUTHORITY_PREFIX + roleKey).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(roleKeys)) {
                resourceRolesMap.put(resource.getResourceUrl(), roleKeys);
            }
        });
        redisTemplate.opsForHash().putAll(key, resourceRolesMap);
    }
}
