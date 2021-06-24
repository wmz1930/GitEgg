package com.gitegg.service.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitegg.service.system.entity.Resource;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author gitegg
 * @since 2018-05-19
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 查询用户权限资源
     * @param userId
     * @return
     */
    List<Resource> queryResourceByUserId(Long userId);

    /**
     * queryResourceTreeProc
     * 
     * @Title: queryResourceTreeProc
     * @Description: 查询登陆用户的许可权限(使用存储过程递归查询所有权限树信息)
     * @param parentId
     * @return List<Resource>
     */
    List<Resource> queryResourceTreeProc(Long parentId);

    /**
     * 查询拥有权限资源的角色 使用@InterceptorIgnore注解
     * 
     * @param
     * @return
     */
    @InterceptorIgnore
    List<Resource> queryResourceRoleIds();
}
