package com.gitegg.service.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.service.system.entity.Resource;
import com.gitegg.service.system.mapper.ResourceMapper;
import com.gitegg.service.system.service.IResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public boolean createResource(Resource resource) {
        String resourceKey = resource.getResourceKey();
        QueryWrapper<Resource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq("resource_key", resourceKey);
        int count = this.count(resourceQueryWrapper);
        if (count > 0) {
            throw new BusinessException("资源标识已存在");
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
        boolean result = this.updateById(resource);
        return result;
    }

    @Override
    public boolean deleteResource(Long resourceId) {
        QueryWrapper<Resource> wpd = new QueryWrapper<Resource>();
        wpd.and(e -> e.eq("id", resourceId).or().eq("parent_id", resourceId));
        boolean result = this.remove(wpd);
        return result;
    }

    @Override
    public List<Resource> selectResourceList(QueryWrapper<Resource> wrapper) {
        List<Resource> list = this.list(wrapper);
        return list;
    }

    @Override
    public List<Resource> queryMenuTreeByUserId(Long userId) {
        List<Resource> resourceList = resourceMapper.queryResourceByUserId(userId);
        Map<Long, Resource> resourceMap = new HashMap<>();
        List<Resource> menus = this.assembleResourceTree(resourceList,resourceMap);
        return menus;
    }

    @Override
    public List<String> queryMenuListByUserId(Long userId) {
        List<Resource> resourceList = resourceMapper.queryResourceByUserId(userId);
        if (!StringUtils.isEmpty(resourceList))
        {
            return resourceList.stream().map(Resource::getResourceKey).collect(Collectors.toList());
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<Resource> queryResourceByParentId(Long parentId) {
        List<Resource> resourceList = resourceMapper.queryResourceTreeProc(parentId);
        Map<Long, Resource> resourceMap = new HashMap<>();
        List<Resource> menus = this.assembleResourceTree(resourceList,resourceMap);
        return menus;
    }

    @Override
    public List<Resource> queryResourceRoleIds() {
        List<Resource> resourceList = resourceMapper.queryResourceRoleIds();
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
