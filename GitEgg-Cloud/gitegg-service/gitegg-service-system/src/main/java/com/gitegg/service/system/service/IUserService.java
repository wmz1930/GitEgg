package com.gitegg.service.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitegg.service.system.bo.UserExportBO;
import com.gitegg.service.system.dto.CreateUserDTO;
import com.gitegg.service.system.dto.QueryUserDTO;
import com.gitegg.service.system.dto.UpdateUserDTO;
import com.gitegg.service.system.entity.User;
import com.gitegg.service.system.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

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
     * 重置用户密码
     * @param userId
     * @return
     */
    boolean resetUserPassword(Long userId);
    
    /**
     * 修改用户状态
     * @param userId
     * @param status
     * @return
     */
    boolean updateUserStatus(Long userId, Integer status);

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
    Boolean checkUserExist(User user);

    /**
     * 分页查询用户
     * @param page
     * @param user
     * @return
     */
    Page<UserInfo> queryUserPage(Page<UserInfo> page, QueryUserDTO user);
    
    /**
     * 批量查询用户信息
     * @param user
     * @return
     */
    List<UserInfo> queryUserList(QueryUserDTO user);

    /**
     * 查询用户详细信息
     * @param user
     * @return
     */
    UserInfo queryUserInfo(User user);
    
    /**
     * 导出用户列表
     * @param user
     * @return
     */
    List<UserExportBO> exportUserList(QueryUserDTO user);
    
    /**
     * 导入用户列表
     * @param file
     * @return
     */
    boolean importUserList(MultipartFile file);
}
