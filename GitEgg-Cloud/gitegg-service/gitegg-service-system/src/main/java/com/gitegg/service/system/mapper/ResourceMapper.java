package com.gitegg.service.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitegg.service.system.dto.QueryUserResourceDTO;
import com.gitegg.service.system.entity.Resource;
import org.apache.ibatis.annotations.Param;

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
     * @param queryUserResourceDTO
     * @return
     */
    List<Resource> queryResourceByUserId(@Param("userResource") QueryUserResourceDTO queryUserResourceDTO);

    /**
     *
     * 查询所有资源
     * @param resourceParent
     * @return
     */
    List<Resource> selectResourceChildren(@Param("resource") Resource resourceParent);

    /**
     * 查询拥有权限资源的角色 使用@InterceptorIgnore注解
     * 
     * @param
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Resource> queryResourceRoles();
}
