package com.gitegg.service.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.service.system.dto.UpdateRoleResourceDTO;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.entity.Role;
import com.gitegg.service.system.entity.RoleResource;
import com.gitegg.service.system.mapper.RoleResourceMapper;
import com.gitegg.service.system.service.IResourceService;
import com.gitegg.service.system.service.IRoleResourceService;
import com.gitegg.service.system.service.IRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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

    private IRoleService roleService;

    @Autowired
    public void setFieldService(@Lazy IRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
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
    public boolean updateList(UpdateRoleResourceDTO updateRoleResource) {
        List<RoleResource> addList = updateRoleResource.getAddResources();
        if (!CollectionUtils.isEmpty(addList)) {
            this.saveBatch(addList);
            this.genRoleResources(addList, true);
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
            this.genRoleResources(delList, false);
        }
        return true;
    }

    /**
     * 执行角色权限初始化
     */
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

    /**
     * 更新资源权限的缓存
     */
    @Override
    public void updateResourceRoles(Resource oldResource, Resource newResource) {
        String redisRoleKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY;
        // 判断是否开启了租户模式，如果开启了，那么按租户分类的方式获取角色权限
        if (enable) {
            redisRoleKey += oldResource.getTenantId();
        } else {
            redisRoleKey = AuthConstant.RESOURCE_ROLES_KEY;
        }
        // 缓存取资源权限角色关系列表
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(redisRoleKey);
        List<String> roleKeys = (List<String>) resourceRolesMap.get(oldResource.getResourceUrl());
        resourceRolesMap.put(newResource.getResourceUrl(), roleKeys);
        resourceRolesMap.remove(oldResource.getResourceUrl());
        redisTemplate.opsForHash().putAll(redisRoleKey, resourceRolesMap);
    }

    /**
     * 删除资源权限的缓存
     */
    @Override
    public void removeResourceRoles(Resource resource) {
        String redisRoleKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY;
        // 判断是否开启了租户模式，如果开启了，那么按租户分类的方式获取角色权限
        if (enable) {
            redisRoleKey += resource.getTenantId();
        } else {
            redisRoleKey = AuthConstant.RESOURCE_ROLES_KEY;
        }
        // 缓存取资源权限角色关系列表
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(redisRoleKey);
        resourceRolesMap.remove(resource.getResourceUrl());
        redisTemplate.opsForHash().putAll(redisRoleKey, resourceRolesMap);
    }

    /**
     * 批量删除资源权限的缓存
     */
    @Override
    public void removeBatchResourceRoles(List<Resource> resources) {
        if (!CollectionUtils.isEmpty(resources))
        {
            String redisRoleKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY;
            // 判断是否开启了租户模式，如果开启了，那么按租户分类的方式获取角色权限
            if (enable) {
                redisRoleKey += resources.get(GitEggConstant.Number.ZERO).getTenantId();
            } else {
                redisRoleKey = AuthConstant.RESOURCE_ROLES_KEY;
            }
            // 缓存取资源权限角色关系列表
            Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(redisRoleKey);
            for (Resource resource: resources)
            {
                resourceRolesMap.remove(resource.getResourceUrl());
            }
            redisTemplate.opsForHash().putAll(redisRoleKey, resourceRolesMap);
        }
    }

    /**
     * 批量删除资源权限的角色缓存
     */
    @Override
    public void removeBatchRoles(List<Role> roles) {
        if (!CollectionUtils.isEmpty(roles))
        {
            String redisRoleKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY;
            // 判断是否开启了租户模式，如果开启了，那么按租户分类的方式获取角色权限
            if (enable) {
                redisRoleKey += roles.get(GitEggConstant.Number.ZERO).getTenantId();
            } else {
                redisRoleKey = AuthConstant.RESOURCE_ROLES_KEY;
            }
            // 缓存取资源权限角色关系列表
            Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(redisRoleKey);
            Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
            List<String> roleKeyList = roles.stream().map(Role::getRoleKey)
                    .distinct().map(roleKey -> AuthConstant.AUTHORITY_PREFIX + roleKey).collect(Collectors.toList());
            while (iterator.hasNext()) {
                String resourceUrl = (String) iterator.next();
                List<String> roleKeys = (List<String>) resourceRolesMap.get(resourceUrl);
                roleKeys.removeAll(roleKeyList);
                resourceRolesMap.put(resourceUrl, roleKeys);
            }
            redisTemplate.opsForHash().putAll(redisRoleKey, resourceRolesMap);
        }
    }

    /**
     * 新增或删除角色的权限缓存
     */
    @Override
    public void genRoleResources(List<RoleResource> roleResourceList, boolean addFlag) {
        String redisRoleKey = AuthConstant.TENANT_RESOURCE_ROLES_KEY;
        List<Long> resourceIdList = roleResourceList.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
        List<Resource> resources = resourceService.listByIds(resourceIdList);

        Long roleId = roleResourceList.get(GitEggConstant.Number.ZERO).getRoleId();
        Role role = roleService.getById(roleId);
        String roleKey = AuthConstant.AUTHORITY_PREFIX + role.getRoleKey();

        if (!CollectionUtils.isEmpty(resources))
        {
            // 判断是否开启了租户模式，如果开启了，那么按租户分类的方式获取角色权限
            if (enable) {
                redisRoleKey += resources.get(GitEggConstant.Number.ZERO).getTenantId();
            } else {
                redisRoleKey = AuthConstant.RESOURCE_ROLES_KEY;
            }
            // 缓存取资源权限角色关系列表
            Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(redisRoleKey);

            for (Resource resource : resources)
            {
                List<String> roleKeys = (List<String>) resourceRolesMap.get(resource.getResourceUrl());
                if (CollectionUtils.isEmpty(roleKeys))
                {
                    roleKeys = new ArrayList<>();
                }
                // 新增数据权限
                if (addFlag && !roleKeys.contains(roleKey))
                {
                    roleKeys.add(roleKey);
                    resourceRolesMap.put(resource.getResourceUrl(), roleKeys);
                }
                // 删除数据权限
                else if (!addFlag && roleKeys.contains(roleKey))
                {
                    roleKeys.remove(roleKey);
                    resourceRolesMap.put(resource.getResourceUrl(), roleKeys);
                }
            }
            redisTemplate.opsForHash().putAll(redisRoleKey, resourceRolesMap);
        }
    }
}
