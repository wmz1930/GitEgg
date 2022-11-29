package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.dto.QueryUserResourceDTO;
import com.gitegg.service.system.entity.Resource;

import java.util.List;

/**
 * @author gitegg
 */
public interface IResourceService extends IService<Resource> {

    /**
     * 查询资源权限列表
     * @param wrapper
     * @return
     */
    List<Resource> selectResourceList(QueryWrapper<Resource> wrapper);

    /**
     * 查询用户菜单
     * @param userId
     * @return
     */
    List<Resource> queryMenuTreeByUserId(Long userId);

    /**
     * 查询用户菜单列表
     * @param queryUserResourceDTO
     * @return
     */
    List<Resource> queryResourceListByUserId(QueryUserResourceDTO queryUserResourceDTO);

    /**
     * 查询资源权限列表
     * @param resource
     * @return
     */
    List<Resource> queryResourceByParentId(Resource resource);

    /**
     * 创建资源权限
     * @param resource
     * @return
     */
    boolean createResource(Resource resource);

    /**
     * 更新资源权限
     * @param resource
     * @return
     */
    boolean updateResource(Resource resource);
    
    /**
     * 修改资源权限状态
     * @param resourceId
     * @param status
     * @return
     */
    boolean updateResourceStatus(Long resourceId, Integer status);

    /**
     * 删除资源权限
     * @param resourceId
     * @return
     */
    boolean deleteResource(Long resourceId);

    /**
     * 查询资源权限列表
     * @param
     * @return
     */
    List<Resource> queryResourceRoles();

    /**
     * 查询资源权限当前最大id
     * @param
     * @return
     */
    Long queryResourceMaxId();
}
