package com.gitegg.platform.mybatis.constant;

public class DataPermissionConstant {

    /**
     * Redis缓存数据权限规则key
     */
    public static final String DATA_PERMISSION_KEY = "auth:data:permission";

    /**
     * 带租户的Redis缓存数据权限规则key
     */
    public static final String TENANT_DATA_PERMISSION_KEY = "auth:tenant:data:permission:";

    /**
     * Redis缓存数据权限规则key mapper_
     */
    public static final String DATA_PERMISSION_KEY_MAPPER = "mapper_";

    /**
     * Redis缓存数据权限规则key role_
     */
    public static final String DATA_PERMISSION_KEY_TYPE = "_type_";

    /**
     * Redis缓存数据权限规则匹配
     */
    public static final String DATA_PERMISSION_KEY_REGULAR_EXPRESSION = "*";

    /**
     * 数据权限表名称
     */
    public static final String DATA_PERMISSION_TABLE_NAME = "t_sys_organization";

    /**
     * 数据权限表别称
     */
    public static final String DATA_PERMISSION_TABLE_ALIAS_NAME = "organizationPermission";

    /**
     * 数据权限字段ORGANIZATION_ID
     */
    public static final String DATA_PERMISSION_ORGANIZATION_ID = "organization_id";

    /**
     * 数据权限字段ID
     */
    public static final String DATA_PERMISSION_ID = "id";

    /**
     * 仅自己可见
     */
    public static final String DATA_PERMISSION_SELF = "creator";

    /**
     * ANCESTORS
     */
    public static final String DATA_PERMISSION_ANCESTORS = "ancestors";

    /**
     * FIND_IN_SET
     */
    public static final String DATA_PERMISSION_FIND_IN_SET = "find_in_set";

}
