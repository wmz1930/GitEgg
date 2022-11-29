package com.gitegg.service.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.system.dto.QueryRoleDTO;
import com.gitegg.service.system.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author gitegg
 * @since 2018-05-19
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询角色列表
     * @param page
     * @param role
     * @return
     */
    Page<Role> selectRoleList(Page<Role> page, @Param("role") QueryRoleDTO role);
    
    /**
     * 查询角色列表
     * @param role
     * @return
     */
    List<Role> selectRoleList(@Param("role") QueryRoleDTO role);
}
