package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.dto.CreateUserDTO;
import com.gitegg.service.system.dto.QueryUserDTO;
import com.gitegg.service.system.dto.UpdateUserDTO;
import com.gitegg.service.system.entity.User;
import com.gitegg.service.system.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: IUserService
 * @Description: 用户相关操接口
 * @author gitegg
 * @date
 */
public interface IUserService extends IService<User> {

    /**
     * 创建用户，成功后将id和对象返回
     * 
     * @param user
     * @param
     * @return
     */
    CreateUserDTO createUser(CreateUserDTO user);

    /**
     * 更新用户
     * 、     * @param user
     * @param
     * @return
     */
    boolean updateUser(UpdateUserDTO user);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户
     * @param userIds
     * @return
     */
    boolean batchDeleteUser(List<Long> userIds);

    /**
     * 根据用户名查询用户
     * 
     * @param userAccount 用户账号
     * @return 用户
     */
    User queryUserByAccount(String userAccount);
    
    /**
     * 查询已存在的用户，用户名、昵称、邮箱、手机号有任一重复即视为用户已存在，真实姓名是可以重复的。
     * @param user
     * @return
     */
    public Boolean checkUserExist(User user);

    /**
     * 分页查询用户
     * @param page
     * @param user
     * @return
     */
    Page<UserInfo> selectUserList(Page<UserInfo> page, QueryUserDTO user);

    /**
     * 查询用户详细信息
     * @param user
     * @return
     */
    UserInfo queryUserInfo(User user);
}
