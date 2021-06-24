package com.gitegg.platform.base.constant;

public class AuthConstant {

    /**
     * 租户id
     */
    public static final String TENANT_ID = "TenantId";

    /**
     * JWT存储权限前缀
     */
    public static final String AUTHORITY_PREFIX = "ROLE_";

    /**
     * 账号锁定前缀
     */
    public static final String LOCK_ACCOUNT_PREFIX = "LOCK_ACCOUNT:";

    /**
     * JWT存储权限属性
     */
    public static final String AUTHORITY_CLAIM_NAME = "authorities";

    /**
     * 认证信息Http请求头
     */
    public static final String JWT_TOKEN_HEADER = "Authorization";

    /**
     * RefreshToken
     */
    public static final String REFRESH_TOKEN = "RefreshToken";

    /**
     * JWT令牌前缀
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * JWT载体key
     */
    public static final String JWT_PAYLOAD_KEY = "payload";

    /**
     * 请求头中的User
     */
    public static final String HEADER_USER = "User";

    /**
     * Redis缓存权限规则key
     */
    public static final String RESOURCE_ROLES_KEY = "auth:resource:roles";

    /**
     * 当开启多租户模式时，Redis缓存权限规则key
     */
    public static final String TENANT_RESOURCE_ROLES_KEY = "auth:tenant:resource:roles:";

    /**
     * 黑名单token前缀
     */
    public static final String TOKEN_BLACKLIST = "auth:token:blacklist:";

    /**
     * 白名单token前缀
     */
    public static final String TOKEN_WHITELIST = "auth:token:whitelist:";

    /**
     * 密码加密方式
     */
    public static final String BCRYPT = "{bcrypt}";

    /**
     * 校验类型
     */
    public static final String GRANT_TYPE = "grant_type";

    /**
     * 客户端id
     */
    public static final String AUTH_CLIENT_ID = "client_id";

    /**
     * 手机号
     */
    public static final String PHONE_NUMBER = "phone_number";

    public static final String CLIENT_DETAILS_FIELDS = "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    public static final String BASE_CLIENT_DETAILS_SQL = "select " + CLIENT_DETAILS_FIELDS + " from t_oauth_client_details";

    public static final String FIND_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " where del_flag = 0 order by client_id";

    public static final String SELECT_CLIENT_DETAILS_SQL = BASE_CLIENT_DETAILS_SQL + " where del_flag = 0 and client_id = ?";
}
