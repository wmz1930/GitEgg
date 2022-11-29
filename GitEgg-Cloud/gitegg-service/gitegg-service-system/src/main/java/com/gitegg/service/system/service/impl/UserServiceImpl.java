package com.gitegg.service.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.mybatis.enums.DataPermissionTypeEnum;
import com.gitegg.service.system.bo.UserExportBO;
import com.gitegg.service.system.bo.UserImportBO;
import com.gitegg.service.system.dto.*;
import com.gitegg.service.system.entity.*;
import com.gitegg.service.system.enums.ResourceEnum;
import com.gitegg.service.system.mapper.UserMapper;
import com.gitegg.service.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: UserServiceImpl
 * @Description: 用户相关操作接口实现类
 * @author gitegg
 * @date 2018年5月18日 下午3:20:30
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;

    private final IUserRoleService userRoleService;

    private final IOrganizationUserService organizationUserService;

    private final IDataPermissionUserService dataPermissionUserService;

    private final IResourceService resourceService;

    private final RedisTemplate redisTemplate;

    @Value("${system.defaultPwd}")
    private String defaultPwd;
    
    @Value("${system.defaultRoleId}")
    private Long defaultRoleId;
    
    @Value("${system.defaultOrgId}")
    private Long defaultOrgId;
    
    @Value("${system.defaultUserStatus}")
    private int defaultUserStatus;
    
    @Value("${system.defaultPwdChangeFirst}")
    private boolean defaultPwdChangeFirst;
    
    /**
     * 分页查询用户
     * @param user
     * @return
     */
    @Override
    public Page<UserInfo> queryUserPage(Page<UserInfo> page, QueryUserDTO user) {
        Page<UserInfo> pageUserInfo = userMapper.queryUserPage(page, user);
        return pageUserInfo;
    }
    
    /**
     * 批量查询用户
     * @param user
     * @return
     */
    @Override
    public List<UserInfo> queryUserList( QueryUserDTO user) {
        List<UserInfo> userInfoList = userMapper.queryUserList(user);
        return userInfoList;
    }
    
    /**
     * 导出用户列表
     * @param user
     * @return
     */
    @Override
    public List<UserExportBO> exportUserList(QueryUserDTO user) {
        List<UserExportBO> userExportList = new ArrayList<>();
        List<UserInfo> userInfoList = this.queryUserList(user);
        if (!CollectionUtils.isEmpty(userInfoList))
        {
            for (UserInfo userInfo : userInfoList) {
                UserExportBO userExportBO = BeanCopierUtils.copyByClass(userInfo, UserExportBO.class);
                userExportList.add(userExportBO);
            }
        }
        return userExportList;
    }
    
    /**
     * 导入用户列表
     * @param file
     * @return
     */
    @Override
    public boolean importUserList(MultipartFile file) {
        boolean importSuccess = false;
        try {
            List<UserImportBO> userImportList = EasyExcel.read(file.getInputStream(), UserImportBO.class, null).sheet().doReadSync();
            if (!CollectionUtils.isEmpty(userImportList))
            {
                List<User> userList = new ArrayList<>();
                userImportList.stream().forEach(userImportBO-> {
                    userList.add(BeanCopierUtils.copyByClass(userImportBO, User.class));
                });
                importSuccess = this.saveBatch(userList);
            }
        } catch (IOException e) {
            log.error("批量导入用户数据时发生错误:{}", e);
            throw new BusinessException("批量导入失败:" + e);
        }
        return importSuccess;
    }
    
    /**
     * 新增用户，成功后将id和对象返回
     * @param user
     * @return
     */
    @Override
    public CreateUserDTO createUser(CreateUserDTO user) {
        
        User userEntity = BeanCopierUtils.copyByClass(user, User.class);
        // 查询已存在的用户，用户名、昵称、邮箱、手机号有任一重复即视为用户已存在，真实姓名是可以重复的。
        List<User> userList = userMapper.queryExistUser(userEntity);
        if (!CollectionUtils.isEmpty(userList)) {
            throw new BusinessException("账号已经存在");
        }

        // 如果为空时设置默认角色
        Long roleId = user.getRoleId();
        List<Long> roleIds = user.getRoleIds();
        if (null == roleId && CollectionUtils.isEmpty(roleIds)) {
            // 默认值，改成配置
            roleId = defaultRoleId;
        }
    
        // 设置默认状态
        if (null == userEntity.getStatus())
        {
            userEntity.setStatus(defaultUserStatus);
        }
        
        // 处理前端传过来的省市区
        userEntity = resolveAreas(userEntity, user.getAreas());
        
        String pwd = userEntity.getPassword();
        if (StringUtils.isEmpty(pwd)) {
            // 默认密码，配置文件配置
            pwd = defaultPwd;
            // 初次登录需要修改密码
           if (defaultPwdChangeFirst)
           {
               userEntity.setStatus( GitEggConstant.UserStatus.NOT_ACTIVE );
           }
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + userEntity.getAccount() +  DigestUtils.md5DigestAsHex(pwd.getBytes()));
        userEntity.setPassword(cryptPwd);
        // 保存用户
        boolean result = this.save(userEntity);
        if (result) {
            
            //保存用户和组织机构的关系
            Long organizationId = null == user.getOrganizationId()? defaultOrgId : user.getOrganizationId();
            OrganizationUser organizationUser = new OrganizationUser();
            organizationUser.setUserId(userEntity.getId());
            organizationUser.setOrganizationId(organizationId);
            organizationUserService.save(organizationUser);

            //默认增加用户所在机构数据权限值，但是否有操作权限还是会根据资源权限判断
            DataPermissionUser dataPermission = new DataPermissionUser();
            dataPermission.setUserId(userEntity.getId());
            dataPermission.setOrganizationId(organizationId);
            dataPermissionUserService.save(dataPermission);

            //保存用户多个角色信息
            List<UserRole> userRoleList = new ArrayList<>();
            if(!CollectionUtils.isEmpty(roleIds))
            {
                for (Long role : roleIds)
                {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(userEntity.getId());
                    userRole.setRoleId(role);
                    userRoleList.add(userRole);
                }
                userRoleService.saveBatch(userRoleList);
            }
    
            //保存用户单个角色信息
            if(null != roleId)
            {
                UserRole userRole = new UserRole();
                userRole.setUserId(userEntity.getId());
                userRole.setRoleId(roleId);
                userRoleService.save(userRole);
            }
            user.setId(userEntity.getId());
        }
        return user;
    }
    
    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(UpdateUserDTO user) {
        
        User userEntity = BeanCopierUtils.copyByClass(user, User.class);
        // 查询已存在的用户，用户名、昵称、邮箱、手机号有任一重复即视为用户已存在，真实姓名是可以重复的。
        List<User> userList = userMapper.queryExistUser(userEntity);
        if (!CollectionUtils.isEmpty(userList)) {
            throw new BusinessException("账号已经存在");
        }
    
        // 处理前端传过来的省市区
        userEntity = resolveAreas(userEntity, user.getAreas());

        String pwd = userEntity.getPassword();
        User oldInfo = this.getById(userEntity.getId());
        
        if (oldInfo == null)
        {
            throw new BusinessException("用户未找到");
        }
        
        if (!StringUtils.isEmpty(pwd)) {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + oldInfo.getAccount() +  DigestUtils.md5DigestAsHex(pwd.getBytes()));
            userEntity.setPassword(cryptPwd);
        }
        boolean result = this.updateById(userEntity);
        // 修改用户机构
        if (result && null != user.getOrganizationId())
        {
            LambdaQueryWrapper<OrganizationUser> orgUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orgUserLambdaQueryWrapper.eq(OrganizationUser::getUserId, userEntity.getId()).eq(OrganizationUser::getOrganizationId, user.getOrganizationId());
            OrganizationUser orgUserOld = organizationUserService.getOne(orgUserLambdaQueryWrapper);
            // 如果不存在时，则进行修改
            if (null == orgUserOld)
            {
                // 这里查询是为了只移出所在机构的数据权限，其他数据权限保持不变
                LambdaQueryWrapper<OrganizationUser> organizationUserRemoveWrapper = new LambdaQueryWrapper<>();
                organizationUserRemoveWrapper.eq(OrganizationUser::getUserId, userEntity.getId());
                OrganizationUser orgUserRemove = organizationUserService.getOne(organizationUserRemoveWrapper);
                
                // 只移出所在机构的数据权限
                LambdaQueryWrapper<DataPermissionUser> dataPermissionRemoveWrapper = new LambdaQueryWrapper<>();
                dataPermissionRemoveWrapper.eq(DataPermissionUser::getUserId, userEntity.getId()).eq(DataPermissionUser::getOrganizationId, orgUserRemove.getOrganizationId());
                
                //删除旧机构的数据权限
                dataPermissionUserService.remove(dataPermissionRemoveWrapper);
                //删除旧机构的组织机构关系
                organizationUserService.remove(organizationUserRemoveWrapper);
                //保存用户和组织机构的关系
                OrganizationUser orgUser = new OrganizationUser();
                orgUser.setUserId(userEntity.getId());
                orgUser.setOrganizationId(user.getOrganizationId());
                organizationUserService.save(orgUser);
                //默认增加用户所在机构数据权限，但是否有操作权限还是会根据资源权限判断
                DataPermissionUser dataPermissionUser = new DataPermissionUser();
                dataPermissionUser.setUserId(userEntity.getId());
                dataPermissionUser.setOrganizationId(user.getOrganizationId());
                dataPermissionUserService.save(dataPermissionUser);
            }
        }
        
        List<Long> roleIds = user.getRoleIds();
        if(result && !CollectionUtils.isEmpty(roleIds))
        {
            //查询出用户原先的角色信息
            LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userRoleLambdaQueryWrapper.eq(UserRole::getUserId, userEntity.getId());
            List<UserRole> userRoleList = userRoleService.list(userRoleLambdaQueryWrapper);
            //已有数据和最新数据的差集,即原先的数据有，但最新的数据没有，得到差集是需要删除的角色
            List<UserRole> userRoleDeleteList = userRoleList.stream().filter(item -> !roleIds.contains(item.getRoleId())).collect(Collectors.toList());
            if (!StringUtils.isEmpty(userRoleDeleteList))
            {
                userRoleService.removeByIds(userRoleDeleteList.stream().map(UserRole::getId).collect(Collectors.toList()));
            }
            
            // 最新数据和已有数据的差集，即传进来的数据有，但是原先数据没有，得到的差集是需要新增的角色。
            List<Long> userRoleAddList = roleIds.stream().filter(item -> !userRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList()).contains(item)).collect(Collectors.toList());
            if (!StringUtils.isEmpty(userRoleAddList))
            {
                List<UserRole> userRoleNewList = new ArrayList<>();
                //新增库里不存在的权限
                for (Long roleId : userRoleAddList)
                {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(userEntity.getId());
                    userRole.setRoleId(roleId);
                    userRoleNewList.add(userRole);
                }
                userRoleService.saveBatch(userRoleNewList);
            }
        } else if(result && null != user.getRoleId())
        {
            UserRole userRole = new UserRole();
            userRole.setUserId(userEntity.getId());
            userRole.setRoleId(user.getRoleId());
            LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userRoleLambdaQueryWrapper.eq(UserRole::getUserId, userEntity.getId()).eq(UserRole::getRoleId, user.getRoleId());
            List<UserRole> urList = userRoleService.list(userRoleLambdaQueryWrapper);
            //如果这个角色不存在，则删除其他角色，保存这个角色
            if (CollectionUtils.isEmpty(urList)) {
                LambdaQueryWrapper<UserRole> wpd = new LambdaQueryWrapper<>();
                wpd.eq(UserRole::getUserId, userEntity.getId());
                userRoleService.remove(wpd);
                userRoleService.save(userRole);
            }
        }
        return result;
    }
    
    /**
     * 重置用户密码
     * @param userId
     * @return
     */
    @Override
    public boolean resetUserPassword(Long userId) {
        if (null == userId) {
            throw new BusinessException("用户不存在");
        }
        User oldInfo = this.getById(userId);
        if (oldInfo == null)
        {
            throw new BusinessException("用户不存在");
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + oldInfo.getAccount() +  DigestUtils.md5DigestAsHex(defaultPwd.getBytes()));
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getPassword, cryptPwd).eq(User::getId, userId);
        boolean result = this.update(updateWrapper);
        return result;
    }
    
    /**
     * 修改用户状态
     * @param userId
     * @return
     */
    @Override
    public boolean updateUserStatus( Long userId, Integer status) {
        if (null == userId || null == status) {
            throw new BusinessException("参数错误");
        }
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getStatus, status).eq(User::getId, userId);
        boolean result = this.update(updateWrapper);
        return result;
    }
    
    /**
     * 单个删除用户
     * @param userId
     * @return
     */
    @Override
    public boolean deleteUser(Long userId) {
        boolean result = removeById(userId);
        if (result)
        {
            this.deleteUserRelation(userId);
        }
        return result;
    }
    
    /**
     * 批量删除用户
     * @param userIds
     * @return
     */
    @Override
    public boolean batchDeleteUser(List<Long> userIds) {
        boolean result = removeByIds(userIds);
        if (result)
        {
            for (Long userId : userIds)
            {
                this.deleteUserRelation(userId);
            }
        }
        return result;
    }
    
    /**
     * 通过账号查询用户
     * @param userAccount 用户账号
     * @return
     */
    @Override
    public User queryUserByAccount(String userAccount) {
        LambdaQueryWrapper<User> ew = new LambdaQueryWrapper<>();
        ew.and(e -> e.eq(User::getAccount, userAccount).or().eq(User::getEmail, userAccount).or().eq(User::getMobile,
                userAccount));
        return getOne(ew);
    }
    
    /**
     * 用户是否存在
     * @param user
     * @return
     */
    @Override
    public Boolean checkUserExist(User user) {
        List<User> userList = userMapper.queryExistUser(user);
        // 如果存在则返回true
        if (!CollectionUtils.isEmpty(userList)) {
            return true;
        }
        return false;
    }
    
    /**
     * 登录时查询用户
     * @param user
     * @return
     */
    @Override
    public UserInfo queryUserInfo(User user) {

        UserInfo userInfo = userMapper.queryUserInfo(user);

        if (null == userInfo)
        {
            throw new BusinessException(ResultCodeEnum.INVALID_USERNAME.getMsg());
        }

        String roleIds = userInfo.getRoleIds();
        //组装角色ID列表，用于Oatuh2和Gateway鉴权
        if (!StringUtils.isEmpty(roleIds))
        {
            String[] roleIdsArray = roleIds.split(StrUtil.COMMA);
            userInfo.setRoleIdList(Arrays.asList(roleIdsArray));
        }

        String roleKeys = userInfo.getRoleKeys();
        //组装角色key列表，用于前端页面鉴权
        if (!StringUtils.isEmpty(roleKeys))
        {
            String[] roleKeysArray = roleKeys.split(StrUtil.COMMA);
            userInfo.setRoleKeyList(Arrays.asList(roleKeysArray));
        }

        String dataPermissionTypes = userInfo.getDataPermissionTypes();
        // 获取用户的角色数据权限级别
        if (!StringUtils.isEmpty(dataPermissionTypes))
        {
            String[] dataPermissionTypeArray = dataPermissionTypes.split(StrUtil.COMMA);
            userInfo.setDataPermissionTypeList(Arrays.asList(dataPermissionTypeArray));
        }

        String organizationIds = userInfo.getOrganizationIds();
        // 获取用户机构数据权限列表
        if (!StringUtils.isEmpty(organizationIds))
        {
            String[] organizationIdArray = organizationIds.split(StrUtil.COMMA);
            userInfo.setOrganizationIdList(Arrays.asList(organizationIdArray));
        }

        QueryUserResourceDTO queryUserResourceDTO = new QueryUserResourceDTO();
        queryUserResourceDTO.setUserId(userInfo.getId());
        List<Resource> resourceList = resourceService.queryResourceListByUserId(queryUserResourceDTO);

        // 查询用户权限列表key，用于前端页面鉴权
        List<String> menuList = resourceList.stream().map(Resource::getResourceKey).collect(Collectors.toList());
        userInfo.setResourceKeyList(menuList);

        // 查询用户资源列表，用于SpringSecurity鉴权
        List<String> resourceUrlList = resourceList.stream().filter(s-> !ResourceEnum.MODULE.getCode().equals(s.getResourceType()) && !ResourceEnum.MENU.getCode().equals(s.getResourceType())).map(Resource::getResourceUrl).collect(Collectors.toList());
        userInfo.setResourceUrlList(resourceUrlList);

        // 查询用户菜单树，用于页面展示
        List<Resource> menuTree = resourceService.queryMenuTreeByUserId(userInfo.getId());
        userInfo.setMenuTree(menuTree);

        return userInfo;
    }
    
    /**
     * 删除用户的关联关系
     * @param userId
     */
    private void deleteUserRelation(Long userId) {
        if (null != userId)
        {
            //删除角色关联
            LambdaQueryWrapper<UserRole> wpd = new LambdaQueryWrapper<>();
            wpd.eq(UserRole::getUserId, userId);
            userRoleService.remove(wpd);
            //删除组织关联
            LambdaQueryWrapper<OrganizationUser> organizationUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            organizationUserLambdaQueryWrapper.eq(OrganizationUser::getUserId, userId);
            organizationUserService.remove(organizationUserLambdaQueryWrapper);
            //删除数据权限关联
            LambdaQueryWrapper<DataPermissionUser> dataPermissionUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dataPermissionUserLambdaQueryWrapper.eq(DataPermissionUser::getUserId, userId);
            dataPermissionUserService.remove(dataPermissionUserLambdaQueryWrapper);
        }
    }
    
    /**
     * 处理省市区数据
     * @param userEntity
     * @param areas
     * @return
     */
    private User resolveAreas(User userEntity, List<String> areas){
        if (!CollectionUtils.isEmpty(areas)) {
            userEntity.setProvince(areas.get(GitEggConstant.Address.PROVINCE));
            userEntity.setCity(areas.get(GitEggConstant.Address.CITY));
            userEntity.setArea(areas.get(GitEggConstant.Address.AREA));
        }
        return userEntity;
    }

}
