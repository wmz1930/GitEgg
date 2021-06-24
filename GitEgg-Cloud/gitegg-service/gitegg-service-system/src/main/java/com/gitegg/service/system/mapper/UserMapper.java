package com.gitegg.service.system.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.service.system.dto.QueryUserDTO;
import com.gitegg.service.system.entity.User;
import com.gitegg.service.system.entity.UserInfo;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author GitEgg
 * @since 2018-05-19
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户列表
     * @param page
     * @param user
     * @return
     */
    Page<UserInfo> selectUserList(Page<UserInfo> page, @Param("user") QueryUserDTO user);

    /**
     * 查询用户信息
     * @param user
     * @return
     */
    UserInfo queryUserInfo(@Param("user") User user);
}
