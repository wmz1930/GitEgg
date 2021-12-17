package com.gitegg.service.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.util.BeanCopierUtils;
import com.gitegg.platform.mybatis.enums.DataPermissionTypeEnum;
import com.gitegg.service.system.dto.*;
import com.gitegg.service.system.entity.*;
import com.gitegg.service.system.enums.ResourceEnum;
import com.gitegg.service.system.mapper.UserMapper;
import com.gitegg.service.system.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

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

//    @Value("${system.defaultPwd}")
    private String defaultPwd;

//    @Value("${system.defaultRoleId}")
    private Long defaultRoleId;

//    @Value("${system.defaultOrgId}")
    private Long defaultOrgId;

    @Override
    public Page<UserInfo> selectUserList(Page<UserInfo> page, QueryUserDTO user) {
        Page<UserInfo> pageUserInfo = userMapper.selectUserList(page, user);
        return pageUserInfo;
    }

    @Override
    public boolean createUser(CreateUserDTO user) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.and(e -> e.eq("account", user.getAccount()).or().eq("account", user.getNickname()).or()
            .eq("account", user.getEmail()).or().eq("account", user.getMobile()).or().eq("nickname", user.getAccount())
            .or().eq("nickname", user.getNickname()).or().eq("nickname", user.getEmail()).or()
            .eq("nickname", user.getMobile()).or().eq("email", user.getAccount()).or().eq("email", user.getNickname())
            .or().eq("email", user.getEmail()).or().eq("email", user.getMobile()).or().eq("mobile", user.getAccount())
            .or().eq("mobile", user.getNickname()).or().eq("mobile", user.getEmail()).or()
            .eq("mobile", user.getMobile()));
        List<User> userList = list(ew);
        if (!CollectionUtils.isEmpty(userList)) {
            throw new BusinessException("账号已经存在");
        }

        if(null == user.getOrganizationId())
        {
            user.setOrganizationId(defaultOrgId);
        }

        Long roleId = user.getRoleId();
        List<Long> roleIds = user.getRoleIds();
        if (null == roleId && CollectionUtils.isEmpty(roleIds)) {
            // 默认值，改成配置
            roleId = defaultRoleId;
        }
        List<String> areas = user.getAreas();
        if (!CollectionUtils.isEmpty(areas)) {
            user.setProvince(areas.get(GitEggConstant.Address.PROVINCE));
            user.setCity(areas.get(GitEggConstant.Address.CITY));
            user.setArea(areas.get(GitEggConstant.Address.AREA));
        }
        User userEntity = BeanCopierUtils.copyByClass(user, User.class);
        String pwd = userEntity.getPassword();
        if (StringUtils.isEmpty(pwd)) {
            // 默认密码，配置文件配置
            pwd = defaultPwd;
            // 初次登录需要修改密码
            // userEntity.setUserStatus( "2" );
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + userEntity.getAccount() +  DigestUtils.md5DigestAsHex(pwd.getBytes()));
        userEntity.setPassword(cryptPwd);
        boolean result = save(userEntity);
        if (result) {
            //保存用户和组织机构的关系
            Long organizationId = user.getOrganizationId();
            OrganizationUser orgUser = new OrganizationUser();
            orgUser.setUserId(userEntity.getId());
            orgUser.setOrganizationId(organizationId);
            organizationUserService.save(orgUser);

            //默认增加用户所在机构数据权限值，但是否有操作权限还是会根据资源权限判断
            DataPermissionUser dataPermission = new DataPermissionUser();
            dataPermission.setUserId(userEntity.getId());
            dataPermission.setOrganizationId(organizationId);
            dataPermissionUserService.save(dataPermission);

            //保存用户角色信息
            user.setId(userEntity.getId());
            user.setPassword(cryptPwd);
            UserRole userRole = new UserRole();
            userRole.setUserId(userEntity.getId());
            if(!CollectionUtils.isEmpty(roleIds))
            {
                for (Long role : roleIds)
                {
                    userRole.setRoleId(role);
                    result = userRoleService.save(userRole);
                }
            }
            else
            {
                userRole.setRoleId(roleId);
                result = userRoleService.save(userRole);
            }
        }
        return result;
    }

    @Override
//    @CacheEvict(value = "users", key = "'id_'.concat(#user.id)")
    public boolean updateUser(UpdateUserDTO user) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.ne("id", user.getId())
            .and(e -> e.eq("account", user.getAccount()).or().eq("account", user.getNickname()).or()
                .eq("account", user.getEmail()).or().eq("account", user.getMobile()).or()
                .eq("nickname", user.getAccount()).or().eq("nickname", user.getNickname()).or()
                .eq("nickname", user.getEmail()).or().eq("nickname", user.getMobile()).or()
                .eq("email", user.getAccount()).or().eq("email", user.getNickname()).or().eq("email", user.getEmail())
                .or().eq("email", user.getMobile()).or().eq("mobile", user.getAccount()).or()
                .eq("mobile", user.getNickname()).or().eq("mobile", user.getEmail()).or()
                .eq("mobile", user.getMobile()));
        List<User> userList = list(ew);
        if (!CollectionUtils.isEmpty(userList)) {
            throw new BusinessException("账号已经存在");
        }
        List<String> areas = user.getAreas();
        if (!CollectionUtils.isEmpty(areas)) {
            user.setProvince(areas.get(GitEggConstant.Address.PROVINCE));
            user.setCity(areas.get(GitEggConstant.Address.CITY));
            user.setArea(areas.get(GitEggConstant.Address.AREA));
        }
        User userEntity = BeanCopierUtils.copyByClass(user, User.class);
        String pwd = userEntity.getPassword();
        User oldInfo = getById(userEntity.getId());
        if (!StringUtils.isEmpty(pwd)) {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String cryptPwd = passwordEncoder.encode(AuthConstant.BCRYPT + oldInfo.getAccount() +  DigestUtils.md5DigestAsHex(pwd.getBytes()));
            userEntity.setPassword(cryptPwd);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", userEntity.getId());
        boolean result = update(userEntity, wrapper);

        //修改后更新缓存
        redisTemplate.delete("users:account_" + oldInfo.getAccount());
        if (!StringUtils.isEmpty(oldInfo.getEmail()))
        {
            redisTemplate.delete("users:account_" + oldInfo.getEmail());
        }
        if (!StringUtils.isEmpty(oldInfo.getMobile()))
        {
            redisTemplate.delete("users:account_" + oldInfo.getMobile());
        }

        Long organizationId = user.getOrganizationId();
        QueryWrapper<OrganizationUser> organizationUserWrapper = new QueryWrapper<>();
        organizationUserWrapper.eq("user_id", userEntity.getId()).eq("organization_id", organizationId);
        OrganizationUser orgUserOld = organizationUserService.getOne(organizationUserWrapper);
        if (null == orgUserOld && null != organizationId)
        {
            QueryWrapper<OrganizationUser> organizationUserRemoveWrapper = new QueryWrapper<>();
            organizationUserRemoveWrapper.eq("user_id", userEntity.getId());
            OrganizationUser orgUserRemove = organizationUserService.getOne(organizationUserRemoveWrapper);
            QueryWrapper<DataPermissionUser> dataPermissionRemoveWrapper = new QueryWrapper<>();
            dataPermissionRemoveWrapper.eq("user_id", userEntity.getId()).eq("organization_id", orgUserRemove.getOrganizationId());
            //删除旧机构的数据权限
            dataPermissionUserService.remove(dataPermissionRemoveWrapper);
            //删除旧机构的组织机构关系
            organizationUserService.remove(organizationUserRemoveWrapper);
            //保存用户和组织机构的关系
            OrganizationUser orgUser = new OrganizationUser();
            orgUser.setUserId(userEntity.getId());
            orgUser.setOrganizationId(organizationId);
            organizationUserService.save(orgUser);
            //默认增加用户所在机构数据权限值，但是否有操作权限还是会根据资源权限判断
            DataPermissionUser dataPermissionUser = new DataPermissionUser();
            dataPermissionUser.setUserId(userEntity.getId());
            dataPermissionUser.setOrganizationId(organizationId);
            dataPermissionUserService.save(dataPermissionUser);
        }

        List<Long> roleIds = user.getRoleIds();
        if (result && (null != user.getRoleId() || !CollectionUtils.isEmpty(roleIds))) {
            if(!CollectionUtils.isEmpty(roleIds))
            {
                //删除不存在的权限
                QueryWrapper<UserRole> wp = new QueryWrapper<>();
                wp.eq("user_id", userEntity.getId());
                List<UserRole> urList = userRoleService.list(wp);
                if (!CollectionUtils.isEmpty(urList)) {
                    for (UserRole role : urList)
                    {
                        //如果这个权限不存在，则删除
                        if (!roleIds.contains(role.getRoleId()))
                        {
                            QueryWrapper<UserRole> wpd = new QueryWrapper<>();
                            wpd.eq("user_id", userEntity.getId()).eq("role_id", role.getRoleId());
                            userRoleService.remove(wpd);
                        }
                    }
                }

                //新增库里不存在的权限
                for (Long role : roleIds)
                {
                    QueryWrapper<UserRole> oldWp = new QueryWrapper<>();
                    oldWp.eq("user_id", userEntity.getId()).eq("role_id", role);
                    UserRole oldUserRole = userRoleService.getOne(oldWp);
                    //查询出库中存在的角色列表，如果更新中的存在则不操作，如果不存在则新增
                    if(null == oldUserRole)
                    {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userEntity.getId());
                        userRole.setRoleId(role);
                        result = userRoleService.save(userRole);
                    }
                }
            }
            else if(null != user.getRoleId())
            {
                UserRole userRole = new UserRole();
                userRole.setUserId(userEntity.getId());
                userRole.setRoleId(user.getRoleId());
                QueryWrapper<UserRole> wp = new QueryWrapper<>();
                wp.eq("user_id", userEntity.getId()).eq("role_id", user.getRoleId());
                List<UserRole> urList = userRoleService.list(wp);
                //如果这个权限不存在，则删除其他权限，保存这个权限
                if (CollectionUtils.isEmpty(urList)) {
                    QueryWrapper<UserRole> wpd = new QueryWrapper<>();
                    wpd.eq("user_id", userEntity.getId());
                    userRoleService.remove(wpd);
                    result = userRoleService.save(userRole);
                }
            }
            redisTemplate.delete("roles:user_id_" + userEntity.getId());
            redisTemplate.delete("resources:user_id_" + userEntity.getId());
            redisTemplate.delete("resources:all_user_id_" + userEntity.getId());
        }
        return result;
    }

    @Override
//    @CacheEvict(value = "users", key = "'id_'.concat(#userId)")
    public boolean deleteUser(Long userId) {
        User oldInfo = getById(userId);
        boolean result = removeById(userId);
        if (result) {
            redisTemplate.delete("users:account_" + oldInfo.getAccount());
            if (!StringUtils.isEmpty(oldInfo.getEmail()))
            {
                redisTemplate.delete("users:account_" + oldInfo.getEmail());
            }
            if (!StringUtils.isEmpty(oldInfo.getMobile()))
            {
                redisTemplate.delete("users:account_" + oldInfo.getMobile());
            }
            redisTemplate.delete("users:user_id_" + userId);
        }
        return result;
    }

    @Override
    public boolean batchDeleteUser(List<Long> userIds) {
        List<User> userList = listByIds(userIds);
        for (User oldInfo : userList)
        {
            redisTemplate.delete("users:account_" + oldInfo.getAccount());
            if (!StringUtils.isEmpty(oldInfo.getEmail()))
            {
                redisTemplate.delete("users:account_" + oldInfo.getEmail());
            }
            if (!StringUtils.isEmpty(oldInfo.getMobile()))
            {
                redisTemplate.delete("users:account_" + oldInfo.getMobile());
            }
            redisTemplate.delete("users:id_" + oldInfo.getId());
            redisTemplate.delete("roles:user_id_" + oldInfo.getId());
        }
        boolean result = removeByIds(userIds);
        return result;
    }

    @Override
    public User queryUserByAccount(String userAccount) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.and(e -> e.eq("account", userAccount).or().eq("email", userAccount).or().eq("mobile",
                userAccount));
        return getOne(ew);
    }

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
            String[] roleIdsArray = roleIds.split(",");
            userInfo.setRoleIdList(Arrays.asList(roleIdsArray));
        }

        String roleKeys = userInfo.getRoleKeys();
        //组装角色key列表，用于前端页面鉴权
        if (!StringUtils.isEmpty(roleKeys))
        {
            String[] roleKeysArray = roleKeys.split(",");
            userInfo.setRoleKeyList(Arrays.asList(roleKeysArray));
        }

        String dataPermissionTypes = userInfo.getDataPermissionTypes();
        // 获取用户的角色数据权限级别
        if (!StringUtils.isEmpty(dataPermissionTypes))
        {
            String[] dataPermissionTypeArray = dataPermissionTypes.split(",");
            userInfo.setDataPermissionTypeList(Arrays.asList(dataPermissionTypeArray));
        }

        String organizationIds = userInfo.getOrganizationIds();
        // 获取用户机构数据权限列表
        if (!StringUtils.isEmpty(organizationIds))
        {
            String[] organizationIdArray = organizationIds.split(",");
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

}
