package com.gitegg.service.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.service.system.dto.UpdateRoleResourceDTO;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.entity.RoleResource;
import com.gitegg.service.system.mapper.RoleResourceMapper;
import com.gitegg.service.system.service.IResourceService;
import com.gitegg.service.system.service.IRoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gitegg
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource>
        implements IRoleResourceService {

    private final IResourceService resourceService;

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
        return true;
    }
}
