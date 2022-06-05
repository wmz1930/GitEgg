package com.gitegg.oauth.service;

import java.util.Collection;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.gitegg.platform.base.constant.AuthConstant;

import lombok.Getter;

/**
 * 用户信息拓展
 *
 * @author GitEgg
 */
@Getter
public class GitEggUserDetails extends User {

    private static final long serialVersionUID = -53908893011700266L;
    /**
     * 用户id
     */
    private final Long id;

    /**
     * 租户ID
     */
    private final String tenantId;

    /**
     * 第三方认证ID
     */
    private final String oauthId;

    /**
     * 账号
     */
    private final String account;

    /**
     * 姓名
     */
    private final String realName;

    /**
     * 昵称
     */
    private final String nickname;

    /**
     * 机构id
     */
    private final Long organizationId;

    /**
     * 机构id
     */
    private final String organizationName;

    /**
     * 角色id
     */
    private final Long roleId;

    /**
     * 角色名
     */
    private final String roleName;

    /**
     * 机构id
     */
    private final String organizationNames;

    /**
     * 角色id集合
     */
    private final String roleIds;

    /**
     * 角色名集合
     */
    private final String roleNames;

    /**
     * 数据权限类型列表
     */
    private List<String> dataPermissionTypeList;

    /**
     * 数据权限机构id列表
     */
    private final String organizationIds;

    /**
     * 角色id列表
     */
    private List<String> roleIdList;

    /**
     * 角色key列表
     */
    private List<String> roleKeyList;

    /**
     * 菜单列表
     */
    private List<String> resourceKeyList;

    /**
     * 机构id列表
     */
    private List<String> organizationIdList;

    /**
     * 头像
     */
    private final String avatar;



    public GitEggUserDetails(Long id, String tenantId, String oauthId, String nickname, String realName,
                             Long organizationId, String organizationName, String organizationIds, String organizationNames, Long roleId, String roleIds, String roleName, String roleNames,
                             List<String> roleIdList, List<String> roleKeyList, List<String> resourceKeyList, List<String> dataPermissionTypeList, List<String> organizationIdList,
        String avatar, String account, String password, boolean enabled, boolean accountNonExpired,
        boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(account, password, enabled, accountNonExpired, credentialsNonExpired,
            accountNonLocked, authorities);
        this.id = id;
        this.tenantId = tenantId;
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.account = account;
        this.realName = realName;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.organizationIds = organizationIds;
        this.organizationNames = organizationNames;
        this.roleIds = roleIds;
        this.roleNames = roleNames;
        this.roleIdList = roleIdList;
        this.roleKeyList = roleKeyList;
        this.resourceKeyList = resourceKeyList;
        this.dataPermissionTypeList = dataPermissionTypeList;
        this.organizationIdList = organizationIdList;
        this.avatar = avatar;
    }

}
