package com.gitegg.service.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.service.system.dto.QueryUserResourceDTO;
import com.gitegg.service.system.entity.Organization;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.enums.ResourceEnum;
import com.gitegg.service.system.mapper.ResourceMapper;
import com.gitegg.service.system.service.IResourceService;
import com.gitegg.service.system.service.IRoleResourceService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gitegg
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    private final ResourceMapper resourceMapper;

    /**
     * 解决循环依赖问题
     */
    private IRoleResourceService roleResourceService;

    @Autowired
    public void setRoleResourceService(@Lazy IRoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }

    @Override
    public boolean createResource(Resource resource) {
        String resourceKey = resource.getResourceKey();
        QueryWrapper<Resource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq("resource_key", resourceKey);
        int count = this.count(resourceQueryWrapper);
        if (count > 0) {
            throw new BusinessException("资源标识已存在");
        }

        if(null != resource.getParentId() && resource.getParentId().longValue() != GitEggConstant.PARENT_ID.longValue())
        {
            Resource resourceParent = this.getById(resource.getParentId());
            String parentAncestors = resourceParent.getAncestors();
            resource.setAncestors(parentAncestors + StrUtil.COMMA + resource.getParentId());
        }
        else
        {
            resource.setAncestors(GitEggConstant.PARENT_ID.toString());
        }

        boolean result = this.save(resource);
        return result;
    }

    @Override
    public boolean updateResource(Resource resource) {
        String resourceKey = resource.getResourceKey();
        QueryWrapper<Resource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq("resource_key", resourceKey).ne("id", resource.getId());
        int count = this.count(resourceQueryWrapper);
        if (count > 0) {
            throw new BusinessException("资源标识已存在");
        }

        //判断是否修改了父级资源ID，如果改了，那么需要修改本资源及本资源下所有的ancestors字段
        Resource resourceOld = this.getById(resource.getId());
        if (null != resourceOld && null != resourceOld.getParentId()
                && null != resource.getParentId() && resourceOld.getParentId().longValue() != resource.getParentId().longValue())
        {
            Resource resourceParentNew = this.getById(resource.getParentId());
            //新的父级组合
            String ancestorsNew;
            if (null != resourceParentNew) {
                ancestorsNew = resourceParentNew.getAncestors() + StrUtil.COMMA + resource.getParentId();
            }
            else
            {
                ancestorsNew = GitEggConstant.PARENT_ID.toString();
            }
            //设置组织新的父级组合
            resource.setAncestors(ancestorsNew);
            //旧的父级组合
            String ancestorsOld = resourceOld.getAncestors();
            Resource resourceParent = new Resource();
            resourceParent.setParentId(resource.getId());
            //只查子节点
            resourceParent.setIsLeaf(GitEggConstant.Number.ONE);
            List<Resource> resourceChildrenList = resourceMapper.selectResourceChildren(resourceParent);
            if (!CollectionUtils.isEmpty(resourceChildrenList)) {
                resourceChildrenList = resourceChildrenList.stream().map(res -> {res.setAncestors(res.getAncestors().replaceFirst(ancestorsOld, ancestorsNew)); return res;}).collect(Collectors.toList());
            }
            this.updateBatchById(resourceChildrenList);
        }
        boolean result = this.updateById(resource);

        //更新权限资源后，重新更新缓存内容
        roleResourceService.initResourceRoles();

        return result;
    }

    @Override
    public boolean deleteResource(Long resourceId) {
        boolean result = false;
        if (null == resourceId)
        {
            throw new BusinessException("请选择要删除的资源");
        }
        Resource resourceParent = new Resource();
        resourceParent.setParentId(resourceId);
        List<Resource> resourceChildrenList = resourceMapper.selectResourceChildren(resourceParent);
        if (!CollectionUtils.isEmpty(resourceChildrenList))
        {
            List<Long> resourceIds = resourceChildrenList.stream().map(Resource::getId).collect(Collectors.toList());
            result = removeByIds(resourceIds);

            //更新权限资源后，重新更新缓存内容
            roleResourceService.initResourceRoles();
        }
        return result;
    }

    @Override
    public List<Resource> selectResourceList(QueryWrapper<Resource> wrapper) {
        List<Resource> list = this.list(wrapper);
        return list;
    }

    @Override
    public List<Resource> queryMenuTreeByUserId(Long userId) {
        QueryUserResourceDTO queryUserResourceDTO = new QueryUserResourceDTO();
        queryUserResourceDTO.setUserId(userId);
        queryUserResourceDTO.setResourceTypeList(Lists.newArrayList(ResourceEnum.MENU.getCode()));
        List<Resource> resourceList = resourceMapper.queryResourceByUserId(queryUserResourceDTO);
        Map<Long, Resource> resourceMap = new HashMap<>();
        List<Resource> menus = this.assembleResourceTree(resourceList,resourceMap);
        return menus;
    }

    @Override
    public List<Resource> queryResourceListByUserId(QueryUserResourceDTO queryUserResourceDTO) {
        List<Resource> resourceList = resourceMapper.queryResourceByUserId(queryUserResourceDTO);
        return resourceList;
    }

    @Override
    public List<Resource> queryResourceByParentId(Long parentId) {
        List<Resource> resourceList;
        try {
            if (null == parentId) {
                parentId = GitEggConstant.PARENT_ID;
            }
            Resource resourceParent = new Resource();
            resourceParent.setParentId(parentId);
            List<Resource> resources = resourceMapper.selectResourceChildren(resourceParent);
            Map<Long, Resource> resourceMap = new HashMap<>();
            resourceList = this.assembleResourceTree(resources,resourceMap);
        } catch (Exception e) {
            log.error("查询资源树失败:", e);
            throw new BusinessException("查询资源树失败");
        }
        return resourceList;
    }

    @Override
    public List<Resource> queryResourceRoles() {
        List<Resource> resourceList = resourceMapper.queryResourceRoles();
        return resourceList;
    }

    /**
     * 组装子父级目录
     * @param resourceList
     * @param resourceMap
     * @return
     */
    private List<Resource> assembleResourceTree(List<Resource> resourceList, Map<Long, Resource> resourceMap)
    {
        List<Resource> menus = new ArrayList<>();
        for (Resource resource : resourceList) {
            resourceMap.put(resource.getId(), resource);
        }
        for (Resource resource : resourceList) {
            Long treePId = resource.getParentId();
            Resource resourceTree = resourceMap.get(treePId);
            if (null != resourceTree && !resource.equals(resourceTree)) {
                List<Resource> nodes = resourceTree.getChildren();
                if (null == nodes) {
                    nodes = new ArrayList<>();
                    resourceTree.setChildren(nodes);
                }
                nodes.add(resource);
            } else {
                menus.add(resource);
            }
        }
        return menus;
    }
}
