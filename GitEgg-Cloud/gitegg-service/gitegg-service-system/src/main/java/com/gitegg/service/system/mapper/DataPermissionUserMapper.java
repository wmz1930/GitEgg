package com.gitegg.service.system.mapper;

import com.gitegg.service.system.dto.QueryUserDTO;
import com.gitegg.service.system.entity.DataPermissionUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.gitegg.service.system.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.gitegg.service.system.dto.DataPermissionUserDTO;
import com.gitegg.service.system.dto.QueryDataPermissionUserDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
public interface DataPermissionUserMapper extends BaseMapper<DataPermissionUser> {

    /**
    * 查询列表
    * @param page
    * @param dataPermissionUserDTO
    * @return
    */
    Page<DataPermissionUserDTO> queryDataPermissionUserList(Page<DataPermissionUserDTO> page, @Param("dataPermissionUser") QueryDataPermissionUserDTO dataPermissionUserDTO);

    /**
    * 查询信息
    * @param dataPermissionUserDTO
    * @return
    */
    DataPermissionUserDTO queryDataPermissionUser(@Param("dataPermissionUser") QueryDataPermissionUserDTO dataPermissionUserDTO);


    /**
     * 分页查询机构权限下的用户列表
     * @param page
     * @param user
     * @return
     */
    Page<UserInfo> selectOrganizationUserList(Page<UserInfo> page, @Param("user") QueryUserDTO user);
}
