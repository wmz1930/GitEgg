package com.gitegg.platform.base.annotation.auth;

import java.lang.annotation.*;

/**
 * 数据权限过滤
 *
 * @author gitegg
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(DataPermissions.class)
public @interface DataPermission {

    /**
     * 需要做数据权限主表
     */
    String dataTableName() default  "";

    /**
     * 需要做数据权限表的别名
     */
    String dataTableAlias() default  "";

    /**
     * 数据权限需要排除的字段
     */
    String dataColumnExclude() default  "";

    /**
     * 数据权限需要保留的字段
     */
    String dataColumnInclude() default  "";

    /**
     * 数据权限表
     */
   String innerTableName() default "t_sys_organization";

    /**
     * 数据权限表的别名
     */
    String innerTableAlias() default "dpOrg";

    /**
     * 数据权限类型:1只能查看本人 2只能查看本部门 3只能查看本部门及子部门 4可以查看所有数据 5 自定义
     */
    String dataPermissionType() default "";

    /**
     * 自定义数据权限类型
     */
    String customExpression() default "";
}
