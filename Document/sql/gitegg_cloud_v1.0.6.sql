/*
 Navicat Premium Data Transfer

 Source Server         : 测试
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : 127.0.0.1:3306
 Source Schema         : gitegg_cloud

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : 65001

 Date: 09/02/2023 13:01:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_just_auth_config
-- ----------------------------
DROP TABLE IF EXISTS `t_just_auth_config`;
CREATE TABLE `t_just_auth_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT 'JustAuth开关',
  `enum_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义扩展第三方登录的配置类',
  `http_timeout` int(11) NULL DEFAULT NULL COMMENT 'Http请求的超时时间',
  `cache_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存类型',
  `cache_prefix` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存前缀',
  `cache_timeout` int(11) NULL DEFAULT NULL COMMENT '缓存超时时间',
  `status` tinyint(2) NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户第三方登录功能配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_just_auth_config
-- ----------------------------
INSERT INTO `t_just_auth_config` VALUES (2, 0, 0, '234234', 3000, 'Redis', 'GITEGG:JUSTAUTH:', 60, 0, '', '2022-05-16 15:22:06', 1, '2022-12-30 14:09:43', 1, 0);

-- ----------------------------
-- Table structure for t_just_auth_social
-- ----------------------------
DROP TABLE IF EXISTS `t_just_auth_social`;
CREATE TABLE `t_just_auth_social`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `uuid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户第三方系统的唯一ID',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户来源',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `blog` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户网址',
  `company` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在公司',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户备注',
  `gender` tinyint(2) NOT NULL DEFAULT 0 COMMENT '性别 -1未知 1男 0女',
  `access_token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户的授权令牌',
  `expire_in` int(11) NULL DEFAULT NULL COMMENT '第三方用户的授权令牌的有效期',
  `refresh_token` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '刷新令牌',
  `access_token_expire_in` int(11) NULL DEFAULT NULL COMMENT '第三方刷新令牌的有效期',
  `uid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户的 ID',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户的 open id',
  `access_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个别平台的授权信息',
  `union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方用户的 union id',
  `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Google Scope',
  `token_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Google TokenType',
  `id_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Google IdToken',
  `mac_algorithm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小米MacAlgorithm',
  `mac_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小米Mac_Key',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业微信code',
  `oauth_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Twitter OauthToken',
  `oauth_token_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Twitter OauthTokenSecret',
  `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Twitter UserId',
  `screen_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Twitter ScreenName',
  `oauth_callback_confirmed` tinyint(1) NULL DEFAULT NULL COMMENT 'Twitter OauthCallbackConfirmed',
  `rawUserInfo` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '原始用户信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '第三方用户信息表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_just_auth_social_user
-- ----------------------------
DROP TABLE IF EXISTS `t_just_auth_social_user`;
CREATE TABLE `t_just_auth_social_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户id',
  `social_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '第三方用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '第三方用户关联关系' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_just_auth_source
-- ----------------------------
DROP TABLE IF EXISTS `t_just_auth_source`;
CREATE TABLE `t_just_auth_source`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `source_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方登录的名称',
  `source_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方登录类型：默认default  自定义custom',
  `request_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义第三方登录的请求Class',
  `client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端id：对应各平台的appKey',
  `client_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端Secret：对应各平台的appSecret',
  `redirect_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录成功后的回调地址',
  `alipay_public_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝公钥：当选择支付宝登录时，该值可用',
  `union_id` tinyint(1) NULL DEFAULT NULL COMMENT '是否需要申请unionid，目前只针对qq登录',
  `stack_overflow_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Stack Overflow Key',
  `agent_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业微信，授权方的网页应用ID',
  `user_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业微信第三方授权用户类型，member|admin',
  `domain_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '域名前缀 使用 Coding 登录和 Okta 登录时，需要传该值。',
  `ignore_check_state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '忽略校验code state 参数，默认不开启。',
  `scopes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支持自定义授权平台的 scope 内容',
  `device_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID, 设备唯一标识ID',
  `client_os_type` int(11) NULL DEFAULT NULL COMMENT '喜马拉雅：客户端操作系统类型，1-iOS系统，2-Android系统，3-Web',
  `pack_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '喜马拉雅：客户端包名',
  `pkce` tinyint(1) NULL DEFAULT NULL COMMENT '是否开启 PKCE 模式，该配置仅用于支持 PKCE 模式的平台，针对无服务应用，不推荐使用隐式授权，推荐使用 PKCE 模式',
  `auth_server_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Okta 授权服务器的 ID， 默认为 default。',
  `ignore_check_redirect_uri` tinyint(1) NOT NULL DEFAULT 0 COMMENT '忽略校验 {@code redirectUri} 参数，默认不开启。',
  `proxy_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Http代理类型',
  `proxy_host_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Http代理Host',
  `proxy_port` int(11) NULL DEFAULT NULL COMMENT 'Http代理Port',
  `status` tinyint(2) NULL DEFAULT 1 COMMENT '\'0\'禁用，\'1\' 启用',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户第三方登录信息配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_just_auth_source
-- ----------------------------
INSERT INTO `t_just_auth_source` VALUES (1, 0, 'DINGTALK', 'default', NULL, 'xxxxxxxxxx', 'xxxxxxxxxx', 'http://192.168.0.2:3100/social/dingtalk/callback', NULL, 0, NULL, NULL, NULL, NULL, 1, '123,234,345', '15152', NULL, NULL, 1, NULL, 1, 'HTTP', '127.0.0.1', 8080, 1, '21515', '2022-05-19 16:00:36', 1, '2023-01-19 09:35:30', 1, 0);
INSERT INTO `t_just_auth_source` VALUES (2, 0, 'GITHUB', 'default', NULL, '3456', '3456', '3456', NULL, 0, NULL, NULL, NULL, NULL, 0, NULL, '3456', NULL, NULL, 1, NULL, 0, 'HTTP', '3456', 3456, 1, '3456', '2022-06-05 22:16:53', 1, '2022-06-05 22:17:26', 1, 1);
INSERT INTO `t_just_auth_source` VALUES (3, 0, 'GITHUB', 'default', NULL, 'xxxxxxxxx', 'xxxxxxxxxxxxx', 'http://192.168.0.2:3100/social/github/callback', NULL, 0, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, 0, NULL, 0, NULL, NULL, NULL, 1, NULL, '2022-06-06 12:02:22', 1, '2023-01-19 09:35:49', 1, 0);
INSERT INTO `t_just_auth_source` VALUES (4, 0, 'WEIBO', 'default', NULL, '34563', '345', '345', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '345', NULL, NULL, 1, NULL, 1, 'HTTP', '345', 345, 1, '345', '2022-12-30 15:19:09', 1, '2022-12-30 15:19:29', 1, 0);
INSERT INTO `t_just_auth_source` VALUES (5, 0, 'WEIBO', 'default', NULL, '3453', '345', '345', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '345', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, 1, '345', '2022-12-30 15:25:33', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `t_oauth_client_details`;
CREATE TABLE `t_oauth_client_details`  (
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用于唯一标识每一个客户端(client); 在注册时必须填写(也可由服务端自动生成).\r\n用于唯一标识每一个客户端(client); 在注册时必须填写(也可由服务端自动生成)',
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: \"unity-resource,mobile-resource\"',
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '	用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成)',
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定客户端申请的权限范围,可选值包括read,write,trust;若有多个权限范围用逗号(,)分隔',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定客户端支持的grant_type,可选值包括authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔,如: \"authorization_code,password\"',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端的重定向URI',
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔',
  `access_token_validity` int(11) NULL DEFAULT NULL COMMENT '设定客户端的access_token的有效时间值(单位:秒)',
  `refresh_token_validity` int(11) NULL DEFAULT NULL COMMENT '设定客户端的refresh_token的有效时间值(单位:秒)',
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实际应用中, 可以用该字段来存储关于客户端的一些其他信息,如客户端的国家,地区,注册时的IP地址等等',
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设置用户是否自动Approval操作, 默认值为 \'false\', 可选值包括 \'true\',\'false\', \'read\',\'write\'.',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oauth_client_details
-- ----------------------------
INSERT INTO `t_oauth_client_details` VALUES ('gitegg-admin', NULL, '{bcrypt}$2a$10$1Cc8Ji1PenKletLLR8paOur5KX1u5gStTeVKaLA2kFQzMjwLvsKTq', 'all', 'password,captcha,sms_captcha,social,refresh_token,authorization_code', NULL, NULL, 3600, 7200, NULL, 'true', NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_oauth_client_details` VALUES ('gitegg-weapp', NULL, '{bcrypt}$2a$10$3q73GZNZbZkKN5jnnj1wLuZ84N9RvQ19taTMzXcMxGzcmrBsFCJcO', 'all', 'password,captcha,sms_captcha,social,refresh_token,authorization_code', NULL, NULL, 3600, 7200, NULL, 'true', NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_code_generator_api
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_api`;
CREATE TABLE `t_sys_code_generator_api`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `api_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `api_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口地址',
  `api_method` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `api_params` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口参数',
  `api_status` tinyint(2) NULL DEFAULT 1 COMMENT '状态',
  `api_object` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口对象',
  `label_field` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'label字段',
  `value_field` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'value字段',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '操作人',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_DICT_NAME`(`api_name`) USING BTREE,
  INDEX `INDEX_DICT_CODE`(`api_path`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接口配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_code_generator_api
-- ----------------------------
INSERT INTO `t_sys_code_generator_api` VALUES (1, 0, '机构数据接口', '/@/api/system/organization', 'getOrganizationList', '{ parentId: 0 }', 1, 'list', 'organizationName', 'id', '请求机构的数据', '2022-12-12 16:50:48', 1, '2022-12-12 16:59:42', 1, 0);
INSERT INTO `t_sys_code_generator_api` VALUES (2, 0, '资源权限接口', '/@/api/system/resource', 'getResourceList', '{parentId: 0}', 1, 'list', 'resourceName', 'id', '请求资源权限接口的接口', '2022-12-12 16:52:08', 1, '2022-12-12 16:59:38', 1, 0);
INSERT INTO `t_sys_code_generator_api` VALUES (3, 0, '角色数据接口', '/@/api/system/role', 'getRoleListAll', '', 1, 'list', 'roleName', 'id', '请求角色数据的接口', '2022-12-12 16:54:29', 1, '2022-12-12 16:59:34', 1, 0);
INSERT INTO `t_sys_code_generator_api` VALUES (4, 0, '省市区数据', 'province-city-china/data', 'level', '', 1, '', 'name', 'code', '省市区下拉选择的数据', '2022-12-12 17:00:50', 1, NULL, NULL, 1);

-- ----------------------------
-- Table structure for t_sys_code_generator_config
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_config`;
CREATE TABLE `t_sys_code_generator_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源',
  `service_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务类型 cloud  boot',
  `front_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端版本 vue2  vue3',
  `module_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `module_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块代码',
  `service_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `table_alias` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表别名',
  `table_prefix` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表前缀',
  `parent_package` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级包名',
  `domain_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实体名称，界面展示的实体名称',
  `controller_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'controller路径',
  `form_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单类型 modal弹出框  drawer抽屉  tab新窗口',
  `table_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表类型 single单表  join_query连接查询 main_sub主表子表',
  `extends_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '主表子表时，是否使用继承',
  `table_show_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '展示类型 table数据表格 tree_table 树表格 3 left_tree_table左树右表  tree数据树  table_table左表右表  left_table_tree左表右树',
  `form_item_col` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单字段排列 1一列一行  2 两列一行',
  `left_tree_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '左树类型 organization机构树 resource资源权限树 ',
  `service_code_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端代码路径',
  `front_code_path` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端代码路径',
  `front_code_dir` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'view代码系统目录',
  `import_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持导入 1支持 0不支持',
  `export_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持导出 1支持 0不支持',
  `query_reuse` tinyint(1) NOT NULL DEFAULT 1 COMMENT '查询复用：分页查询和单条记录查询公用同一个sql语句',
  `status_handling` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态处理',
  `code_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '代码生成类型  全部  仅后端代码  仅前端代码',
  `code_save_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代码保存方式  远程下载   覆盖本地',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成配置表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_sys_code_generator_datasource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_datasource`;
CREATE TABLE `t_sys_code_generator_datasource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `datasource_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源名称',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接地址',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `driver` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库驱动',
  `db_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_code_generator_datasource
-- ----------------------------
INSERT INTO `t_sys_code_generator_datasource` VALUES (1, 0, '测试数据库', 'jdbc:mysql://127.0.0.1/gitegg_cloud?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&all owMultiQueries=true&serverTimezone=Asia/Shanghai', 'root', 'root', '', 'mysql', '测试数据库', '2021-08-26 11:49:27', 1, '2021-08-26 13:49:26', 1, 1);
INSERT INTO `t_sys_code_generator_datasource` VALUES (75, 0, '测试数据库001', 'jdbc:mysql://127.0.0.1/gitegg_cloud?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&all owMultiQueries=true&serverTimezone=Asia/Shanghai', 'root', 'root', '', 'mysql', '', '2021-11-11 19:06:11', 1, '2022-12-01 14:57:00', 1, 0);


-- ----------------------------
-- Table structure for t_sys_code_generator_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_dict`;
CREATE TABLE `t_sys_code_generator_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '字典上级',
  `ancestors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有上级字典id的集合，便于查找',
  `dict_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `dict_order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `dict_status` tinyint(2) NULL DEFAULT 1 COMMENT '1有效，0禁用',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '操作人',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_DICT_NAME`(`dict_name`) USING BTREE,
  INDEX `INDEX_DICT_CODE`(`dict_code`) USING BTREE,
  INDEX `INDEX_PARENT_ID`(`parent_id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_code_generator_dict
-- ----------------------------
INSERT INTO `t_sys_code_generator_dict` VALUES (12, 0, 0, '0', '数据库类型', 'DB_TYPE', 3, 1, '代码生成时，可选择的数据库类型', '2021-08-25 15:36:28', 1, '2021-09-10 15:48:22', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (13, 0, 12, '0,12', 'MySql数据库', 'mysql', 1, 1, '', '2021-08-25 15:52:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (14, 0, 12, '0,12', 'MariaDB数据库', 'mariadb', 2, 1, '', '2021-08-25 15:52:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (15, 0, 12, '0,12', 'Oracle11g及以下数据库', 'oracle', 3, 1, '', '2021-08-25 15:52:38', 1, '2021-08-25 15:52:50', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (16, 0, 12, '0,12', 'Oracle12c+数据库', 'oracle12c', 4, 1, '', '2021-08-25 15:53:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (17, 0, 12, '0,12', 'DB2数据库', 'db2', 5, 1, '', '2021-08-25 15:53:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (18, 0, 12, '0,12', 'H2数据库', 'h2', 6, 1, '', '2021-08-25 15:53:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (19, 0, 12, '0,12', 'HSQL数据库', 'hsql', 7, 1, '', '2021-08-25 15:53:43', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (20, 0, 12, '0,12', 'SQLite数据库', 'sqlite', 8, 1, '', '2021-08-25 15:53:56', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (21, 0, 12, '0,12', 'Postgre数据库', 'postgresql', 9, 1, '', '2021-08-25 15:54:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (22, 0, 12, '0,12', 'SQLServer2005数据库', 'sqlserver2005', 10, 1, '', '2021-08-25 15:54:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (23, 0, 12, '0,12', 'SQLServer数据库', 'sqlserver', 11, 1, '', '2021-08-25 15:55:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (24, 0, 12, '0,12', '达梦数据库', 'dm', 12, 1, '', '2021-08-25 15:56:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (25, 0, 12, '0,12', '虚谷数据库', 'xugu', 13, 1, '', '2021-08-25 15:56:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (26, 0, 12, '0,12', '人大金仓数据库', 'kingbasees', 14, 1, '', '2021-08-25 15:57:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (27, 0, 12, '0,12', 'Phoenix HBase数据库', 'phoenix', 15, 1, '', '2021-08-25 15:57:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (28, 0, 12, '0,12', 'Gauss 数据库', 'zenith', 16, 1, '', '2021-08-25 15:57:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (29, 0, 12, '0,12', 'clickhouse 数据库', 'clickhouse', 17, 1, '', '2021-08-25 15:58:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (30, 0, 12, '0,12', '南大通用数据库', 'gbase', 18, 1, '', '2021-08-25 15:58:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (31, 0, 12, '0,12', '神通数据库', 'oscar', 19, 1, '', '2021-08-25 15:58:31', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (32, 0, 12, '0,12', 'Sybase ASE 数据库', 'sybase', 20, 1, '', '2021-08-25 15:58:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (33, 0, 12, '0,12', 'OceanBase 数据库', 'oceanbase', 21, 1, '', '2021-08-25 15:59:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (34, 0, 12, '0,12', 'Firebird 数据库', 'Firebird', 22, 1, '', '2021-08-25 15:59:28', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (35, 0, 12, '0,12', '瀚高数据库', 'highgo', 23, 1, '', '2021-08-25 15:59:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (36, 0, 12, '0,12', '其他数据库', 'other', 24, 1, '', '2021-08-25 15:59:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (37, 0, 0, '0', '表单展现类型', 'FORM_TYPE', 4, 1, '代码生成时，表单的展现类型model弹出框  drawer抽屉  tab新页面', '2021-09-10 15:47:47', 1, '2021-09-10 15:48:02', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (38, 0, 37, '0,37', '弹出框', 'Modal', 1, 1, '弹出框', '2021-09-10 15:48:41', 1, '2022-12-09 09:58:19', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (39, 0, 37, '0,37', '抽屉', 'Drawer', 2, 1, '抽屉', '2021-09-10 15:49:03', 1, '2022-12-09 09:58:25', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (40, 0, 37, '0,37', '新窗口', 'Tab', 3, 1, '新窗口', '2021-09-10 15:49:28', 1, '2022-12-09 10:04:48', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (41, 0, 0, '0', '表数据类型', 'TABLE_DATA_TYPE', 5, 1, '表数据类型 single单表  multi多表', '2021-09-10 15:50:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (42, 0, 41, '0,41', '单表', 'single', 1, 1, '单表', '2021-09-10 15:51:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (43, 0, 41, '0,41', '多表查询', 'join_query', 2, 1, 'left join  等需要查询多个表数据时使用', '2021-09-10 15:51:14', 1, '2022-12-09 16:41:47', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (44, 0, 0, '0', '数据列表展现类型', 'TABLE_DATA_SHOW_TYPE', 6, 1, '数据列表展现类型 table数据表格 tree_table 树表格 3 left_tree_table左树右表  tree数据树', '2021-09-10 15:53:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (45, 0, 44, '0,44', '数据表格', 'table', 1, 1, '普通数据表格', '2021-09-10 15:56:55', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (46, 0, 44, '0,44', '树表格', 'tree_table', 2, 1, '树形表格', '2021-09-10 15:57:31', 1, '2022-12-13 12:40:05', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (47, 0, 44, '0,44', '左树右表', 'left_tree_table', 3, 1, '左树右表', '2021-09-10 15:58:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (48, 0, 44, '0,44', '数据树', 'tree', 4, 0, '数据树，应用场景不多，用树形表格代替更合适。暂时禁用，有需求的自己开发。', '2021-09-10 15:59:29', 1, '2022-12-09 11:35:27', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (49, 0, 44, '0,44', '左表右表', 'table_table', 5, 1, '左侧是表 右侧也是表', '2021-09-10 16:01:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (50, 0, 44, '0,44', '左表右树', 'left_table_tree', 6, 0, '左表右树', '2021-09-10 16:02:52', 1, '2022-12-09 11:35:39', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (51, 0, 44, '0,44', '左树右树形表', 'left_tree_tree_table', 7, 0, '左树右树形表', '2021-09-10 16:09:53', 1, '2022-12-09 11:35:44', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (52, 0, 44, '0,44', '左树右树', 'tree_tree', 8, 0, '左树右树', '2021-09-10 16:10:22', 1, '2022-12-09 11:36:18', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (53, 0, 0, '0', '树类型', 'TREE_TYPE', 7, 1, '树类型', '2021-09-10 16:12:24', 1, '2021-09-27 10:28:26', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (54, 0, 53, '0,53', '机构树', 'OrganizationTree', 1, 1, '机构树', '2021-09-10 16:21:26', 1, '2022-12-09 11:45:01', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (55, 0, 53, '0,53', '资源权限树', 'ResourceTree', 2, 1, '资源权限树', '2021-09-10 16:21:45', 1, '2022-12-09 11:45:21', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (56, 0, 0, '0', '表单列数', 'FORM_COL', 8, 1, '表单的行列数', '2021-09-13 11:32:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (57, 0, 56, '0,56', '一行一列', '24', 1, 1, '一行一列', '2021-09-13 11:32:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (58, 0, 56, '0,56', '一行两列', '12', 2, 1, '一行两列', '2021-09-13 11:32:44', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (59, 0, 0, '0', '通用是否', 'YES_NO', 9, 1, '通用是否', '2021-09-13 11:33:56', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (60, 0, 59, '0,59', '是', '1', 1, 1, '是', '2021-09-13 11:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (61, 0, 59, '0,59', '否', '0', 2, 1, '否', '2021-09-13 11:34:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (62, 0, 0, '0', '联表类型', 'UNION_TYPE', 10, 1, '联表类型', '2021-09-18 15:13:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (63, 0, 62, '0,62', '左连接(LEFT JOIN)', 'left', 1, 1, '左连接(LEFT JOIN)', '2021-09-18 15:16:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (64, 0, 62, '0,62', '右连接(RIGHT JOIN)', 'right', 2, 1, '右连接(RIGHT JOIN)', '2021-09-18 15:17:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (65, 0, 62, '0,62', '等值链接(INNER JOIN)', 'inner', 3, 1, '等值链接(INNER JOIN)', '2021-09-18 15:17:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (66, 0, 62, '0,62', '联合查询(UNION)', 'union', 4, 1, '联合查询(UNION)', '2021-09-18 15:18:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (67, 0, 0, '0', '实体字段类型', 'ENTITY_TYPE', 11, 1, '字段类型', '2021-09-24 10:14:45', 1, '2021-09-24 10:15:03', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (68, 0, 67, '0,67', 'String', 'java.lang.String', 1, 1, 'java.lang.String', '2021-09-24 10:15:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (69, 0, 67, '0,67', 'Integer', 'java.lang.Integer', 2, 1, 'java.lang.Integer', '2021-09-24 10:15:34', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (70, 0, 67, '0,67', 'Long', 'java.lang.Long', 3, 1, 'java.lang.Long', '2021-09-24 10:15:55', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (71, 0, 67, '0,67', 'LocalDateTime', 'java.time.LocalDateTime', 4, 1, 'java.time.LocalDateTime', '2021-09-24 10:16:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (72, 0, 67, '0,67', 'BigDecimal', 'java.math.BigDecimal', 5, 1, 'java.math.BigDecimal', '2021-09-24 10:26:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (73, 0, 0, '0', '组件类型', 'CONTROL_TYPE', 12, 1, '组件类型', '2021-09-27 09:06:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (74, 0, 73, '0,73', '单行文本框', 'INPUT_TEXT', 1, 1, '单行文本框', '2021-09-27 09:10:56', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (75, 0, 73, '0,73', '多行文本框', 'TEXTAREA', 2, 1, '多行文本框', '2021-09-27 09:12:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (76, 0, 73, '0,73', '下拉列表', 'SELECT', 3, 1, 'SELECT', '2021-09-27 09:14:00', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (77, 0, 73, '0,73', '单选框', 'RADIO', 4, 1, '单选框', '2021-09-27 09:14:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (78, 0, 73, '0,73', '多选框', 'CHECKBOX', 5, 1, '多选框', '2021-09-27 09:15:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (79, 0, 73, '0,73', '多选下拉框', 'SELECT_MULTI', 6, 1, '多选下拉框', '2021-09-27 09:17:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (80, 0, 73, '0,73', '开关', 'SWITCH', 7, 1, '开关', '2021-09-27 09:26:27', 1, '2021-09-27 09:32:00', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (81, 0, 73, '0,73', '日期选择框', 'DTAE_PICKER', 8, 1, '日期选择框', '2021-09-27 09:28:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (82, 0, 73, '0,73', '日期范围框', 'DATE_PICKER_RANAGE', 9, 1, '日期范围框', '2021-09-27 09:28:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (83, 0, 73, '0,73', '时间选择框（时分秒）', 'TIME_PICKER', 10, 1, '时间选择框（时分秒）', '2021-09-27 09:29:34', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (84, 0, 73, '0,73', '单图片上传', 'UPLOAD_IMG', 11, 1, '单图片上传', '2021-09-27 09:36:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (85, 0, 73, '0,73', '多图片上传', 'UPLOAD_IMG_MULTI', 12, 1, '多图片上传', '2021-09-27 09:36:36', 1, NULL, NULL, 1);
INSERT INTO `t_sys_code_generator_dict` VALUES (86, 0, 73, '0,73', '单文件上传', 'UPLOAD_FILE', 13, 1, '单文件上传', '2021-09-27 09:36:55', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (87, 0, 73, '0,73', '多文件上传', 'UPLOAD_FILE_MULTI', 14, 1, '多文件上传', '2021-09-27 09:37:16', 1, NULL, NULL, 1);
INSERT INTO `t_sys_code_generator_dict` VALUES (88, 0, 73, '0,73', '组织下拉树', 'ORG_SELECT_TREE', 15, 1, '组织下拉树', '2021-09-27 09:38:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (89, 0, 73, '0,73', '富文本', 'RICH_TEXT', 16, 1, '富文本', '2021-09-27 09:39:00', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (90, 0, 0, '0', '查询类型', 'QUERY_TYPE', 13, 1, '查询类型', '2021-09-27 10:28:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (91, 0, 90, '0,90', '等于(=)', 'EQUAL', 1, 1, '等于', '2021-09-27 10:31:45', 1, '2021-09-27 10:33:10', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (92, 0, 90, '0,90', '不等于( != )', 'NOT_EQUAL', 2, 1, '不等于( != )', '2021-09-27 10:32:15', 1, '2021-09-27 10:33:19', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (93, 0, 90, '0,90', '大于( > )', 'GREATER', 3, 1, '大于( > )', '2021-09-27 10:33:02', 1, '2021-09-27 10:33:26', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (94, 0, 90, '0,90', '大于等于( >= )', 'GREATER_EQUAL', 4, 1, '大于等于', '2021-09-27 10:33:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (95, 0, 90, '0,90', '小于( < )', 'LESS', 5, 1, '小于', '2021-09-27 10:34:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (96, 0, 90, '0,90', '小于等于( <= )', 'LESS_EQUAL', 6, 1, '小于等于', '2021-09-27 10:34:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (97, 0, 90, '0,90', '模糊查询(like \'%abc%\')', 'LIKE', 7, 1, '模糊查询', '2021-09-27 10:57:03', 1, '2021-09-27 10:57:51', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (98, 0, 90, '0,90', '左边模糊(like \'abc%\')', 'LIKE_LEFT', 8, 1, '左边模糊(like \'%abc\')', '2021-09-27 10:58:54', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (99, 0, 90, '0,90', '右边模糊(like \'%abc\')', 'LIKE_RIGHT', 9, 1, '右边模糊(like \'abc%\')', '2021-09-27 10:59:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (100, 0, 0, '0', '字段校验规则', 'FIELD_VERIFY', 14, 1, '字段校验', '2021-09-29 17:59:24', 1, '2021-09-29 17:59:48', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (101, 0, 73, '0,73', '数字输入框', 'INPUT_NUMBER', 17, 1, '数字输入框', '2021-10-12 09:48:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (102, 0, 73, '0,73', '评分', 'RATE', 18, 1, '评分', '2021-10-12 10:04:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (103, 0, 0, '0', '通用真假', 'TRUE_FALSE', 3, 1, '通用真假', '2021-10-12 17:55:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (104, 0, 103, '0,103', '是', 'true', 1, 1, '是', '2021-10-12 17:55:31', 1, '2021-10-15 15:38:44', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (105, 0, 103, '0,103', '否', 'false', 2, 1, '否', '2021-10-12 17:55:45', 1, '2021-10-15 15:38:38', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (106, 0, 0, '0', '通用状态', 'ENABLE_OR_NOT', 7, 1, '通用状态', '2021-10-15 15:44:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (107, 0, 106, '0,106', '启用', '1', 1, 1, '启用', '2021-10-15 15:44:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (108, 0, 106, '0,106', '禁用', '0', 2, 1, '禁用', '2021-10-15 15:45:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (109, 0, 0, '0', '代码生成方式', 'CODE_TYPE', 17, 1, '代码生成方式', '2021-10-15 17:54:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (110, 0, 109, '0,109', '全部', 'ALL', 1, 1, '全部', '2021-10-15 17:54:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (111, 0, 109, '0,109', '后端代码', 'SERVICE', 2, 1, '后端代码', '2021-10-15 17:55:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (112, 0, 109, '0,109', '前端代码', 'FRONT', 3, 1, '前端代码', '2021-10-15 17:55:33', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (113, 0, 0, '0', '灯杆类型', 'POLE_MODEL', 20, 1, '灯杆类型', '2021-11-24 12:51:34', 1, '2021-11-24 12:58:01', 1, 1);
INSERT INTO `t_sys_code_generator_dict` VALUES (114, 0, 113, '0,113', '固定式', '1', 1, 1, '', '2021-11-24 12:52:42', 1, NULL, NULL, 1);
INSERT INTO `t_sys_code_generator_dict` VALUES (115, 0, 113, '0,113', '滑槽式', '2', 2, 1, '', '2021-11-24 12:52:52', 1, NULL, NULL, 1);
INSERT INTO `t_sys_code_generator_dict` VALUES (116, 0, 113, '0,113', '机架式', '3', 3, 1, '', '2021-11-24 12:53:05', 1, NULL, NULL, 1);
INSERT INTO `t_sys_code_generator_dict` VALUES (117, 0, 41, '0,41', '主表子表', 'main_sub', 3, 1, '主表子表：当一个表需要拆分为多个表时，有一个是主表记录常用字段，有一个或者多个子表，分别存放不同类的数据字段。', '2021-12-03 22:12:40', 1, '2022-12-09 16:43:46', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (121, 0, 0, '0', '代码保存方式', 'CODE_SAVE_TYPE', 19, 1, '代码保存方式', '2022-12-01 11:32:14', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (122, 0, 121, '0,121', '远程下载', 'SERVER_DOWNLOAD', 1, 1, '服务器下载', '2022-12-01 11:33:03', 1, '2022-12-01 11:33:46', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (123, 0, 121, '0,121', '本地覆盖', 'LOCAL_OVERLAY', 2, 1, '本地覆盖', '2022-12-01 11:33:33', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (124, 0, 0, '0', '服务类型', 'SERVICE_TYPE', 18, 1, '服务类型', '2022-12-06 18:08:49', 1, '2022-12-06 18:08:55', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (125, 0, 124, '0,124', 'SpringBoot', 'boot', 1, 1, '单体应用', '2022-12-06 18:09:24', 1, '2022-12-06 18:24:42', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (126, 0, 124, '0,124', 'SpringCloud', 'cloud', 2, 1, '微服务', '2022-12-06 18:09:44', 1, '2022-12-06 18:24:47', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (127, 0, 0, '0', '前端版本', 'FRONT_TYPE', 20, 1, '前端版本', '2022-12-06 18:10:56', 1, '2022-12-06 18:26:51', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (128, 0, 127, '0,127', 'vue2', 'vue2', 1, 1, 'VUE2', '2022-12-06 18:12:00', 1, '2022-12-11 16:45:25', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (129, 0, 127, '0,127', 'vue3', 'vue3', 2, 1, 'VUE3', '2022-12-06 18:12:15', 1, '2022-12-11 16:45:14', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (130, 0, 73, '0,73', '下拉树选择框', 'API_TREE_SELECT', 19, 1, '下拉树选择框', '2022-12-08 11:13:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (131, 0, 73, '0,73', '树', 'API_TREE', 20, 1, 'API树', '2022-12-08 11:13:48', 1, '2022-12-13 11:07:18', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (132, 0, 73, '0,73', '级联选择', 'CASCADER', 21, 1, '级联选择', '2022-12-08 11:23:28', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (133, 0, 73, '0,73', '省市区选择框', 'PROVINCE_CITY_AREA', 22, 1, '省市区选择框', '2022-12-12 23:17:05', 1, '2022-12-12 23:17:13', 1, 0);
INSERT INTO `t_sys_code_generator_dict` VALUES (134, 0, 73, '0,73', '密码输入框', 'INPUT_PASSWORD', 23, 1, '密码输入框', '2022-12-13 18:20:58', 1, '2022-12-13 18:21:16', 1, 0);

-- ----------------------------
-- Table structure for t_sys_code_generator_field
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_field`;
CREATE TABLE `t_sys_code_generator_field`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `generation_id` bigint(20) NOT NULL COMMENT '代码生成主键',
  `join_id` bigint(20) NOT NULL COMMENT '关联表主键',
  `join_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `field_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `comment` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段描述',
  `entity_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实体类型',
  `entity_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实体名称',
  `form_add` tinyint(1) NOT NULL DEFAULT 0 COMMENT '表单新增',
  `form_edit` tinyint(1) NOT NULL DEFAULT 0 COMMENT '表单编辑',
  `query_term` tinyint(1) NOT NULL DEFAULT 0 COMMENT '查询条件',
  `list_show` tinyint(1) NOT NULL DEFAULT 0 COMMENT '列表展示',
  `import_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持导入 1支持 0不支持',
  `export_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持导出 1支持 0不支持',
  `required` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否必填',
  `field_unique` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否唯一',
  `query_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询类型',
  `control_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件类型',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典编码',
  `api_id` bigint(20) NULL DEFAULT NULL COMMENT '数据接口配置id',
  `min` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最小值',
  `max` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最大值',
  `min_length` int(11) NOT NULL DEFAULT 0 COMMENT '最小长度',
  `max_length` int(11) NULL DEFAULT NULL COMMENT '字段最大长度',
  `default_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `validate_id` bigint(20) NULL DEFAULT NULL COMMENT '校验规则主键',
  `validate_regular` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义正则表达式校验规则',
  `field_sort` int(11) NOT NULL DEFAULT 1 COMMENT '显示排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_field`(`generation_id`, `join_id`, `join_table_name`, `field_name`) USING BTREE COMMENT '联合约束'
) ENGINE = InnoDB AUTO_INCREMENT = 952 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段属性配置表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_sys_code_generator_field_validate
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_field_validate`;
CREATE TABLE `t_sys_code_generator_field_validate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `field_id` bigint(20) NOT NULL COMMENT '字段主键',
  `validate_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '校验类型',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段校验规则配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_code_generator_field_validate
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_code_generator_table_join
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_table_join`;
CREATE TABLE `t_sys_code_generator_table_join`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `generation_id` bigint(20) NOT NULL COMMENT '代码生成主键',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源和主表一致',
  `module_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `module_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块代码',
  `controller_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'controller路径',
  `join_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `join_table_alias` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表别名',
  `join_table_prefix` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表前缀',
  `join_table_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'left左连接 right右连接 inner等值连接 union联合查询',
  `join_table_select` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义查询字段',
  `join_table_on` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义on条件',
  `association_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联主键id',
  `table_sort` int(11) NULL DEFAULT NULL COMMENT '显示排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '多表查询时的联合表配置' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_sys_code_generator_validate
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_validate`;
CREATE TABLE `t_sys_code_generator_validate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `validate_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '校验名称',
  `validate_regular` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正则表达式校验规则',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '\'0\'禁用，\'1\' 启用',
  `comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段校验规则配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_code_generator_validate
-- ----------------------------
INSERT INTO `t_sys_code_generator_validate` VALUES (1, 0, '手机号码', '^1[0-9]{10}$', 1, NULL, '2021-10-15 16:47:18', 1, '2021-10-15 17:16:51', 1, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (2, 0, '电子邮箱', '^([A-Za-z0-9_\\\\-\\\\.])+\\\\@([A-Za-z0-9_\\\\-\\\\.])+\\\\.([A-Za-z]{2,4})$', 1, NULL, '2021-10-15 18:42:24', 1, '2021-12-17 16:52:54', 1, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (3, 0, '数字校验', '^[0-9]*$', 1, NULL, '2021-11-24 13:20:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (4, 0, '中文、英文、数字包括下划线', '^[\\u4E00-\\u9FA5A-Za-z0-9_]+$', 1, NULL, '2021-11-24 13:42:26', 1, '2022-12-06 16:09:19', 1, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (5, 0, '身份证号(15位、18位数字)', '^\\d{15}|\\d{18}$', 1, NULL, '2021-11-24 13:43:05', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (6, 0, '域名', '[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+/.?', 1, NULL, '2021-11-24 13:44:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (7, 0, '数字+字母', '^[A-Za-z0-9]+$', 1, NULL, '2021-11-24 13:44:42', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (8, 0, '汉字', '^[\\u4e00-\\u9fa5]{0,}$', 1, NULL, '2021-11-24 13:45:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (9, 0, '英文和数字', '^[A-Za-z0-9]+$ 或 ^[A-Za-z0-9]{4,40}$', 1, NULL, '2021-11-24 13:45:37', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (10, 0, '字母', '^[A-Za-z]+$', 1, NULL, '2021-11-24 13:45:53', 1, NULL, NULL, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (11, 0, '带1-2位小数的正数或负数', '^(-)?\\d+(.\\d{1,2})?$', 1, NULL, '2021-11-24 13:47:46', 1, '2021-11-24 14:30:07', 1, 0);
INSERT INTO `t_sys_code_generator_validate` VALUES (12, 0, '5555', '555', 1, NULL, '2022-11-29 23:22:04', 1, '2022-11-29 23:22:11', 1, 1);
INSERT INTO `t_sys_code_generator_validate` VALUES (13, 0, '6666', '666', 1, '66666', '2022-11-29 23:39:53', 1, '2022-11-29 23:40:07', 1, 1);
INSERT INTO `t_sys_code_generator_validate` VALUES (14, 0, '999', '999', 1, '999', '2022-11-29 23:41:21', 1, '2022-11-29 23:41:25', 1, 1);

-- ----------------------------
-- Table structure for t_sys_data_permission_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_data_permission_role`;
CREATE TABLE `t_sys_data_permission_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `resource_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '功能权限id',
  `data_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限名称',
  `data_mapper_function` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限对应的mapper方法全路径',
  `data_table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '需要做数据权限主表',
  `data_table_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '需要做数据权限表的别名',
  `data_column_exclude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限需要排除的字段',
  `data_column_include` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限需要保留的字段',
  `inner_table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限表,默认t_sys_organization',
  `inner_table_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限表的别名,默认organization',
  `data_permission_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '数据权限类型:1只能查看本人 2只能查看本部门 3只能查看本部门及子部门 4可以查看所有数据',
  `custom_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义数据权限（增加 where条件）',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态 0禁用，1 启用,',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据权限配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_data_permission_role
-- ----------------------------
INSERT INTO `t_sys_data_permission_role` VALUES (1, 0, 15, '查询用户列表数据权限', 'com.gitegg.service.system.mapper.UserMapper.selectUserList', 't_sys_organization_user', 'organizationUser', 'password,roleKey', 'roleIds,createTime', 't_sys_organization', 'orgDataPermission', '2', '', 1, '查询用户列表数据权限', '2021-05-13 16:59:07', 1, '2021-07-22 16:26:37', 1, 0);


-- ----------------------------
-- Table structure for t_sys_data_permission_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_data_permission_user`;
CREATE TABLE `t_sys_data_permission_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `organization_id` bigint(20) NOT NULL COMMENT '机构id',
  `status` tinyint(2) NULL DEFAULT 1 COMMENT '状态 0禁用，1 启用,',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据权限多部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_data_permission_user
-- ----------------------------
INSERT INTO `t_sys_data_permission_user` VALUES (1, 1, 2, 1, 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (2, 1, 3, 1, 1, '2020-11-27 02:55:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (4, 0, 4, 9, 1, '2020-12-25 15:19:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (5, 0, 5, 12, 1, '2020-12-25 17:02:36', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (7, 0, 2, 13, 1, '2020-12-26 21:19:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (15, 0, 4, 13, 1, '2020-12-27 14:11:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (16, 0, 3, 12, 1, '2020-12-27 14:14:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (17, 0, 6, 13, 1, '2021-01-11 09:55:02', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (18, 0, 6, 12, 1, '2021-05-14 17:50:56', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (19, 0, 6, 9, 1, '2021-05-14 17:51:07', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (20, 0, 6, 14, 1, '2021-05-14 17:51:07', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (21, 0, 6, 10, 1, '2021-05-14 17:51:07', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (22, 0, 5, 10, 1, '2021-05-14 17:51:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (23, 0, 6, 11, 1, '2021-05-14 17:51:21', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (24, 0, 2, 11, 1, '2021-05-14 19:51:34', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (25, 0, 6, 14, 1, '2021-05-14 20:00:17', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (26, 0, 2, 14, 1, '2021-07-06 09:54:20', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (27, 0, 7, 14, 1, '2021-07-06 18:17:26', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (28, 0, 7, 11, 1, '2021-07-06 18:17:40', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (29, 0, 8, 12, 1, '2021-08-26 17:28:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (30, 0, 7, 12, 1, '2021-12-22 17:06:07', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (31, 0, 2, 12, 1, '2021-12-22 17:06:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (32, 0, 9, 79, 1, '2022-05-29 21:39:05', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (33, 0, 10, 79, 1, '2022-05-29 21:52:59', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (34, 0, 11, 79, 1, '2022-05-29 21:57:37', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (35, 0, 12, 79, 1, '2022-05-29 22:12:09', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (36, 0, 13, 79, 1, '2022-05-29 22:30:56', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (37, 0, 14, 79, 1, '2022-05-29 22:38:29', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (38, 0, 15, 79, 1, '2022-05-29 23:04:56', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (39, 0, 16, 79, 1, '2022-05-29 23:08:55', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (40, 0, 17, 1, 1, '2022-05-29 23:12:30', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (41, 0, 18, 1, 1, '2022-05-30 10:28:55', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (42, 0, 19, 1, 1, '2022-05-30 14:10:44', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (43, 0, 20, 1, 1, '2022-05-30 14:12:58', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (44, 0, 21, 1, 1, '2022-05-30 14:25:16', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (45, 0, 22, 1, 1, '2022-05-30 14:27:28', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (46, 0, 23, 1, 1, '2022-05-30 14:31:36', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (47, 0, 24, 1, 1, '2022-05-30 14:34:06', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (48, 0, 25, 1, 1, '2022-06-01 18:39:10', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (49, 0, 26, 1, 1, '2022-06-01 18:45:59', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (50, 0, 27, 1, 1, '2022-06-01 18:58:07', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (51, 0, 28, 1, 1, '2022-06-02 18:18:35', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (52, 0, 29, 1, 1, '2022-06-02 18:21:02', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (53, 0, 30, 1, 1, '2022-06-02 18:34:12', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (54, 0, 31, 1, 1, '2022-06-02 19:11:00', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (55, 0, 32, 1, 1, '2022-06-03 10:22:26', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (56, 0, 33, 1, 1, '2022-06-03 12:22:08', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (57, 0, 34, 1, 1, '2022-06-03 12:34:35', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (58, 0, 35, 1, 1, '2022-06-03 12:46:13', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (59, 0, 36, 1, 1, '2022-06-05 20:23:36', NULL, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (60, 0, 37, 1, 1, '2022-06-17 16:47:06', NULL, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (61, 0, 38, 1, 1, '2022-06-20 14:28:54', NULL, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (62, 0, 39, 1, 1, '2022-07-05 17:24:29', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (63, 0, 40, 12, 1, '2022-11-03 20:12:45', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (64, 0, 41, 11, 1, '2022-11-07 15:13:16', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (65, 0, 41, 11, 1, '2022-11-13 14:15:53', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (66, 0, 41, 17, 1, '2022-11-13 14:15:53', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (67, 0, 41, 10, 1, '2022-11-13 14:15:53', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (68, 0, 41, 14, 1, '2022-11-13 14:15:53', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (69, 0, 41, 10, 1, '2022-11-13 15:36:58', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (70, 0, 41, 1, 1, '2022-11-13 15:36:58', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (71, 0, 41, 14, 1, '2022-11-13 15:36:58', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (72, 0, 41, 17, 1, '2022-11-13 15:37:19', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (73, 0, 41, 1, 1, '2022-11-14 09:08:38', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (74, 0, 40, 11, 1, '2022-11-14 12:56:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (75, 0, 42, 17, 1, '2022-11-22 13:45:46', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (76, 0, 42, 10, 1, '2022-11-22 17:48:46', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (77, 0, 42, 11, 1, '2022-11-22 17:48:46', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (78, 0, 42, 14, 1, '2022-11-22 17:55:15', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (79, 0, 42, 13, 1, '2022-11-22 17:55:15', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (80, 0, 42, 1, 1, '2022-11-22 17:55:28', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (81, 0, 43, 17, 1, '2022-11-24 17:47:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (82, 0, 1, 1, 1, '2022-12-06 17:05:09', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (83, 0, 42, 18, 1, '2022-12-06 17:06:53', 1, NULL, NULL, 1);
INSERT INTO `t_sys_data_permission_user` VALUES (84, 0, 44, 11, 1, '2022-12-16 14:21:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (85, 0, 45, 11, 1, '2022-12-16 14:22:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (86, 0, 46, 1, 1, '2023-01-29 17:48:33', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (87, 0, 47, 1, 1, '2023-02-01 23:03:32', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (88, 0, 48, 1, 1, '2023-02-02 15:34:16', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (89, 0, 49, 1, 1, '2023-02-02 23:26:18', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (90, 0, 50, 1, 1, '2023-02-02 23:28:35', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_data_permission_user` VALUES (91, 0, 50, 13, 1, '2023-02-05 19:34:40', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_dfs
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dfs`;
CREATE TABLE `t_sys_dfs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `dfs_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分布式存储分类',
  `dfs_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分布式存储编号',
  `access_url_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件访问地址前缀',
  `upload_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分布式存储上传地址',
  `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '空间名称',
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用ID',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'accessKey',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'secretKey',
  `dfs_default` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否默认存储 0否，1是',
  `dfs_status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态 0 禁用，1 启用',
  `access_control` tinyint(2) NOT NULL DEFAULT 0 COMMENT '访问控制 0私有，1公开',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除 1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分布式存储配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_dfs
-- ----------------------------
INSERT INTO `t_sys_dfs` VALUES (1, 0, 'MINIO', 'PrivateMinIO', 'http://127.0.0.1:9000', 'http://127.0.0.1:9000', 'gitegg', 'gitegg', 'gitegg', 'minioadmin', 'minioadmin', 1, 1, 0, 'MinIO存储', '2021-05-06 14:05:59', 1, '2022-12-27 17:47:30', 1, 0);
INSERT INTO `t_sys_dfs` VALUES (2, 0, 'QINIUYUN_KODO', 'PrivateQINiuKODO', 'http://private.img.gitegg.com', 'http://upload.qiniup.com', 'gitegg', 'gitegg', 'zone0', 'xxxxxxxxxxx', 'xxxxxxxxxxx', 0, 1, 0, '七牛云存储', '2021-05-06 14:09:50', 1, '2022-12-27 11:56:50', 1, 0);
INSERT INTO `t_sys_dfs` VALUES (3, 0, 'MINIO', 'MinIO', 'http://127.0.0.1:9000', 'http://127.0.0.1:9000', 'open', 'open', 'open', 'minioadmin', 'minioadmin', 0, 0, 1, 'MinIO存储', '2021-05-06 14:05:59', 1, '2022-12-28 14:36:58', 1, 0);
INSERT INTO `t_sys_dfs` VALUES (4, 0, 'QINIUYUN_KODO', 'QINiuKODO', 'http://img.gitegg.com', 'http://upload.qiniup.com', 'gitegg-open', 'gitegg', 'zone0', 'xxxxxxxxxxxxxxxxx', 'xxxxxxxxxxxxxxxx', 1, 1, 1, '七牛云存储', '2021-05-06 14:09:50', 1, '2023-02-05 20:34:20', 1, 0);

-- ----------------------------
-- Table structure for t_sys_dfs_file
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dfs_file`;
CREATE TABLE `t_sys_dfs_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `dfs_id` bigint(20) NULL DEFAULT NULL COMMENT '分布式存储配置id',
  `access_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件访问地址',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原文件名',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储文件名',
  `file_extension` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint(20) NULL DEFAULT 0 COMMENT '文件大小',
  `file_status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态 0上传成功失败，1 上传成功',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 189 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分布式存储文件记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_dfs_file
-- ----------------------------
INSERT INTO `t_sys_dfs_file` VALUES (1, 0, NULL, NULL, NULL, '20210506181426.png', NULL, 0, 1, NULL, '2021-05-08 16:20:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dfs_file` VALUES (2, 0, NULL, NULL, NULL, '20210506181426.png', NULL, 0, 1, NULL, '2021-05-08 16:27:55', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict`;
CREATE TABLE `t_sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '字典上级',
  `ancestors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有上级字典id的集合，便于查找',
  `dict_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `dict_order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `dict_status` tinyint(2) NULL DEFAULT 1 COMMENT '1有效，0禁用',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '操作人',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_DICT_NAME`(`dict_name`) USING BTREE,
  INDEX `INDEX_DICT_CODE`(`dict_code`) USING BTREE,
  INDEX `INDEX_PARENT_ID`(`parent_id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 133 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_dict
-- ----------------------------
INSERT INTO `t_sys_dict` VALUES (1, 0, 0, '0', '分布式存储分类', 'DFS_TYPE', 1, 1, '分布式存储分类', '2021-05-06 11:05:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (2, 0, 1, '0,1', 'MinIO', 'MINIO', 1, 1, '', '2021-05-06 11:05:43', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (3, 0, 1, '0,1', '七牛云Kodo', 'QINIUYUN_KODO', 2, 1, '', '2021-05-06 11:06:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (4, 0, 1, '0,1', '阿里云OSS', 'ALIYUN_OSS', 3, 1, '', '2021-05-06 11:06:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (5, 0, 1, '0,1', '腾讯云COS', 'TENCENT_COS', 4, 1, '', '2021-05-06 11:07:34', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (6, 0, 0, '0', '数据权限类型', 'DATA_PERMISSION_TYPE', 2, 1, '数据权限类型', '2021-05-13 15:12:03', 1, '2021-05-13 15:12:11', 1, 0);
INSERT INTO `t_sys_dict` VALUES (7, 0, 6, '0,6', '本人数据', '1', 1, 1, '', '2021-05-13 15:13:14', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (8, 0, 6, '0,6', '本机构数据', '2', 2, 1, '', '2021-05-13 15:13:42', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (9, 0, 6, '0,6', '本机构及子机构数据', '3', 3, 1, '', '2021-05-13 15:14:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (10, 0, 6, '0,6', '所有机构数据', '4', 4, 1, '', '2021-05-13 15:14:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (11, 0, 6, '0,6', '自定义', '5', 5, 1, '', '2021-05-13 15:14:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (12, 0, 0, '0', '数据库类型', 'DB_TYPE', 3, 1, '代码生成时，可选择的数据库类型', '2021-08-25 15:36:28', 1, '2021-09-10 15:48:22', 1, 0);
INSERT INTO `t_sys_dict` VALUES (13, 0, 12, '0,12', 'MySql数据库', 'mysql', 1, 1, '', '2021-08-25 15:52:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (14, 0, 12, '0,12', 'MariaDB数据库', 'mariadb', 2, 1, '', '2021-08-25 15:52:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (15, 0, 12, '0,12', 'Oracle11g及以下数据库', 'oracle', 3, 1, '', '2021-08-25 15:52:38', 1, '2021-08-25 15:52:50', 1, 0);
INSERT INTO `t_sys_dict` VALUES (16, 0, 12, '0,12', 'Oracle12c+数据库', 'oracle12c', 4, 1, '', '2021-08-25 15:53:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (17, 0, 12, '0,12', 'DB2数据库', 'db2', 5, 1, '', '2021-08-25 15:53:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (18, 0, 12, '0,12', 'H2数据库', 'h2', 6, 1, '', '2021-08-25 15:53:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (19, 0, 12, '0,12', 'HSQL数据库', 'hsql', 7, 1, '', '2021-08-25 15:53:43', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (20, 0, 12, '0,12', 'SQLite数据库', 'sqlite', 8, 1, '', '2021-08-25 15:53:56', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (21, 0, 12, '0,12', 'Postgre数据库', 'postgresql', 9, 1, '', '2021-08-25 15:54:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (22, 0, 12, '0,12', 'SQLServer2005数据库', 'sqlserver2005', 10, 1, '', '2021-08-25 15:54:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (23, 0, 12, '0,12', 'SQLServer数据库', 'sqlserver', 11, 1, '', '2021-08-25 15:55:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (24, 0, 12, '0,12', '达梦数据库', 'dm', 12, 1, '', '2021-08-25 15:56:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (25, 0, 12, '0,12', '虚谷数据库', 'xugu', 13, 1, '', '2021-08-25 15:56:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (26, 0, 12, '0,12', '人大金仓数据库', 'kingbasees', 14, 1, '', '2021-08-25 15:57:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (27, 0, 12, '0,12', 'Phoenix HBase数据库', 'phoenix', 15, 1, '', '2021-08-25 15:57:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (28, 0, 12, '0,12', 'Gauss 数据库', 'zenith', 16, 1, '', '2021-08-25 15:57:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (29, 0, 12, '0,12', 'clickhouse 数据库', 'clickhouse', 17, 1, '', '2021-08-25 15:58:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (30, 0, 12, '0,12', '南大通用数据库', 'gbase', 18, 1, '', '2021-08-25 15:58:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (31, 0, 12, '0,12', '神通数据库', 'oscar', 19, 1, '', '2021-08-25 15:58:31', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (32, 0, 12, '0,12', 'Sybase ASE 数据库', 'sybase', 20, 1, '', '2021-08-25 15:58:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (33, 0, 12, '0,12', 'OceanBase 数据库', 'oceanbase', 21, 1, '', '2021-08-25 15:59:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (34, 0, 12, '0,12', 'Firebird 数据库', 'Firebird', 22, 1, '', '2021-08-25 15:59:28', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (35, 0, 12, '0,12', '瀚高数据库', 'highgo', 23, 1, '', '2021-08-25 15:59:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (36, 0, 12, '0,12', '其他数据库', 'other', 24, 1, '', '2021-08-25 15:59:48', 1, '2022-11-30 09:43:00', 1, 0);
INSERT INTO `t_sys_dict` VALUES (53, 0, 0, '0', '树类型', 'TREE_TYPE', 4, 1, '树类型', '2021-09-10 16:12:24', 1, '2021-10-15 15:44:31', 1, 0);
INSERT INTO `t_sys_dict` VALUES (54, 0, 53, '0,53', '机构树', 'organization', 1, 1, '机构树', '2021-09-10 16:21:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (55, 0, 53, '0,53', '资源权限树', 'resource', 2, 1, '资源权限树', '2021-09-10 16:21:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (59, 0, 0, '0', '通用是否', 'YES_NO', 5, 1, '通用是否', '2021-09-13 11:33:56', 1, '2021-10-15 15:44:36', 1, 0);
INSERT INTO `t_sys_dict` VALUES (60, 0, 59, '0,59', '是', '1', 1, 1, '1', '2021-09-13 11:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (61, 0, 59, '0,59', '否', '0', 2, 1, '否', '2021-09-13 11:34:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (62, 0, 0, '0', '通用真假', 'TRUE_FALSE', 6, 1, '通用真假', '2021-10-12 17:55:08', 1, '2021-10-15 15:44:40', 1, 0);
INSERT INTO `t_sys_dict` VALUES (63, 0, 62, '0,62', '是', 'true', 1, 1, '是', '2021-10-12 17:55:31', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (64, 0, 62, '0,62', '否', 'false', 2, 1, '否', '2021-10-12 17:55:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (65, 0, 0, '0', '通用状态', 'ENABLE_OR_NOT', 7, 1, '通用状态', '2021-10-15 15:44:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (66, 0, 65, '0,65', '启用', '1', 1, 1, '启用', '2021-10-15 15:44:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (67, 0, 65, '0,65', '禁用', '0', 2, 1, '禁用', '2021-10-15 15:45:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (68, 0, 0, '0', '短信渠道', 'SMS_CHANNEL', 8, 1, '短信渠道', '2022-05-24 18:18:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (69, 0, 68, '0,68', '阿里云短信', 'aliyun', 1, 1, '阿里云短信', '2022-05-24 18:19:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (70, 0, 68, '0,68', '腾讯云短信', 'tencent', 2, 1, '腾讯云短信', '2022-05-24 18:19:43', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (71, 0, 68, '0,68', '七牛云短信', 'qiniu', 3, 1, '七牛云短信', '2022-05-24 18:20:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (72, 0, 0, '0', '短信类型', 'SMS_TEMPLATE_TYPE', 9, 1, '短信类型', '2022-05-25 11:02:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (73, 0, 72, '0,72', '验证码', '1', 1, 1, '验证码', '2022-05-25 11:02:36', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (74, 0, 72, '0,72', '通知短信', '2', 2, 1, '通知短信', '2022-05-25 11:03:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (75, 0, 0, '0', '时间单位', 'TIME_UNIT', 10, 1, '时间单位', '2022-05-25 14:35:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (76, 0, 75, '0,75', '纳秒', 'NANOSECONDS', 1, 1, '纳秒', '2022-05-25 14:35:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (77, 0, 75, '0,75', '微秒', 'MICROSECONDS', 2, 1, '微秒', '2022-05-25 14:36:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (78, 0, 75, '0,75', '毫秒', 'MILLISECONDS', 3, 1, '毫秒', '2022-05-25 14:36:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (79, 0, 75, '0,75', '秒', 'SECONDS', 4, 1, '秒', '2022-05-25 14:37:00', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (80, 0, 75, '0,75', '分钟', 'MINUTES', 5, 1, '分钟', '2022-05-25 14:37:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (81, 0, 75, '0,75', '小时', 'HOURS', 6, 1, '小时', '2022-05-25 14:37:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (82, 0, 75, '0,75', '天', 'DAYS', 7, 1, '天', '2022-05-25 14:38:14', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (83, 0, 0, '0', '邮件类型', 'MAIL_TEMPLATE_TYPE', 11, 1, '邮件类型', '2022-06-24 16:51:05', 1, '2022-11-21 12:05:21', 1, 0);
INSERT INTO `t_sys_dict` VALUES (84, 0, 83, '0,83', '普通邮件', '1', 1, 1, '普通邮件', '2022-06-24 16:51:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (85, 0, 83, '0,83', '验证码邮件', '2', 2, 1, '2', '2022-06-24 16:51:42', 1, '2022-11-21 12:26:27', 1, 0);
INSERT INTO `t_sys_dict` VALUES (96, 0, 0, '0', '资源权限类型', 'RESOURCE_TYPE', 12, 1, '资源权限类型', '2022-11-22 18:51:28', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (97, 0, 96, '0,96', '模块', '1', 1, 1, '模块', '2022-11-22 18:51:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (98, 0, 96, '0,96', '菜单', '2', 2, 1, '菜单', '2022-11-22 18:51:55', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (99, 0, 96, '0,96', '按钮', '3', 3, 1, '按钮', '2022-11-22 18:52:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (100, 0, 96, '0,96', '接口', '4', 4, 1, '接口', '2022-11-22 18:52:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (101, 0, 0, '0', '组织机构类型', 'ORGANIZATION_TYPE', 12, 1, '组织机构类型', '2022-11-24 19:32:20', 1, '2022-12-01 17:24:13', 1, 0);
INSERT INTO `t_sys_dict` VALUES (102, 0, 101, '0,101', '事业部', '1', 1, 1, '事业部', '2022-11-24 19:32:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (103, 0, 101, '0,101', '机构', '2', 2, 1, '2', '2022-11-24 19:32:54', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (116, 0, 0, '0', '访问控制', 'ACCESS_CONTROL', 14, 1, '访问控制', '2022-12-14 15:12:39', 1, '2022-12-14 15:13:15', 1, 0);
INSERT INTO `t_sys_dict` VALUES (117, 0, 116, '0,116', '公开', '1', 117, 1, '', '2022-12-14 15:18:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (118, 0, 116, '0,116', '私有', '0', 118, 1, '', '2022-12-14 15:19:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (119, 0, 0, '0', '成功失败', 'SUCCESS_AND_FAILURE', 15, 1, '成功失败', '2022-12-15 16:04:43', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (120, 0, 119, '0,119', '成功', '1', 1, 1, '成功', '2022-12-15 16:05:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (121, 0, 119, '0,119', '失败', '0', 2, 1, '失败', '2022-12-15 16:05:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (122, 0, 0, '0', 'Tag渲染颜色SUCCESS', 'TAG_COLOR_SUCCESS', 15, 1, 'Tag渲染颜色', '2022-12-28 11:49:26', 1, '2022-12-28 11:52:54', 1, 0);
INSERT INTO `t_sys_dict` VALUES (123, 0, 122, '0,122', 'green', '1', 1, 1, '成功', '2022-12-28 11:49:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (124, 0, 122, '0,122', 'red', '0', 2, 1, '失败', '2022-12-28 11:50:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (125, 0, 0, '0', 'Tag渲染颜色', 'TAG_COLOR', 15, 1, 'Tag渲染颜色', '2022-12-28 11:49:26', 1, '2022-12-28 11:52:54', 1, 0);
INSERT INTO `t_sys_dict` VALUES (126, 0, 125, '0,125', 'green', '1', 1, 1, '成功', '2022-12-28 11:49:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (127, 0, 125, '0,125', 'orange', '0', 2, 1, '其他', '2022-12-28 11:50:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (128, 0, 0, '0', '通用开关', 'ON_OFF', 18, 1, '通用开关', '2022-12-30 14:03:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (129, 0, 128, '0,128', '开', 'checkedChildren', 1, 1, '开', '2022-12-30 14:04:44', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (130, 0, 128, '0,128', '关', 'unCheckedChildren', 2, 1, '关', '2022-12-30 14:05:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict` VALUES (131, 0, 0, '0', '日志类型', 'OPERATION_LOG_TYPE', 19, 1, '日志类型', '2022-12-30 16:29:01', 1, '2023-02-05 19:53:11', 1, 0);
INSERT INTO `t_sys_dict` VALUES (132, 0, 131, '0,131', '操作日志', '1', 1, 1, '操作日志', '2022-12-30 16:29:16', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_dict_business
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict_business`;
CREATE TABLE `t_sys_dict_business`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '字典上级',
  `ancestors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有上级字典id的集合，便于查找',
  `dict_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `dict_order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `dict_status` tinyint(2) NULL DEFAULT 1 COMMENT '1有效，0禁用',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '操作人',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_DICT_NAME`(`dict_name`) USING BTREE,
  INDEX `INDEX_DICT_CODE`(`dict_code`) USING BTREE,
  INDEX `INDEX_PARENT_ID`(`parent_id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_dict_business
-- ----------------------------
INSERT INTO `t_sys_dict_business` VALUES (1, 0, 0, '0', '通用是否', 'YES_NO', 1, 1, '通用是否', '2021-09-13 11:33:56', 1, '2022-05-18 11:56:45', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (2, 0, 1, '0,1', '是', '1', 1, 1, '1', '2021-09-13 11:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (3, 0, 1, '0,1', '否', '0', 2, 1, '否', '2021-09-13 11:34:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (4, 0, 0, '0', '通用真假', 'TRUE_FALSE', 2, 1, '通用真假', '2021-10-12 17:55:08', 1, '2022-05-18 11:56:48', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (5, 0, 4, '0,4', '是', 'true', 1, 1, '是', '2021-10-12 17:55:31', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (6, 0, 4, '0,4', '否', 'false', 2, 1, '否', '2021-10-12 17:55:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (7, 0, 0, '0', '通用状态', 'ENABLE_OR_NOT', 3, 1, '通用状态', '2021-10-15 15:44:22', 1, '2022-05-18 11:56:52', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (8, 0, 7, '0,7', '启用', '1', 1, 1, '启用', '2021-10-15 15:44:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (9, 0, 7, '0,7', '禁用', '0', 2, 1, '禁用', '2021-10-15 15:45:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (10, 0, 0, '0', '通用开关', 'ON_OFF', 4, 1, 'SWITCH组件，字典值只能为checkedChildren 或 unCheckedChildren', '2022-05-16 12:55:19', 1, '2022-05-18 11:56:56', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (11, 0, 10, '0,10', '开', 'checkedChildren', 1, 1, '', '2022-05-16 12:55:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (12, 0, 10, '0,10', '关', 'unCheckedChildren', 2, 1, '', '2022-05-16 12:56:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (13, 0, 0, '0', '第三方登录类型', 'JUST_SOURCE_TYPE', 5, 1, '第三方登录类型', '2022-05-18 11:56:28', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (14, 0, 13, '0,13', '默认', 'default', 1, 1, 'JustAuth默认实现', '2022-05-18 11:57:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (15, 0, 13, '0,13', '自定义', 'custom', 2, 1, '自定义第三方登录实现', '2022-05-18 11:57:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (16, 0, 0, '0', '三方授权用户类型', 'JUST_USER_TYPE', 6, 1, '企业微信第三方授权用户类型,member|admin', '2022-05-18 11:59:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (17, 0, 16, '0,16', '普通用户', 'member', 1, 1, '普通用户', '2022-05-18 11:59:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (18, 0, 16, '0,16', '管理员', 'admin', 2, 1, '管理员', '2022-05-18 11:59:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (19, 0, 0, '0', '客户端操作系统类型', 'JUST_CLIENT_OS_TYPE', 7, 1, '喜马拉雅：客户端操作系统类型', '2022-05-18 14:07:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (20, 0, 19, '0,19', 'IOS系统', '1', 1, 1, '', '2022-05-18 14:07:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (21, 0, 19, '0,19', 'Android系统', '2', 2, 1, 'Android系统', '2022-05-18 14:08:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (22, 0, 19, '0,19', 'Web', '3', 3, 1, 'Web', '2022-05-18 14:08:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (23, 0, 0, '0', 'Http代理类型', 'HTTP_PROXY_TYPE', 8, 1, 'Http代理类型', '2022-05-18 14:51:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (24, 0, 23, '0,23', 'HTTP', 'HTTP', 1, 1, 'HTTP', '2022-05-18 14:51:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (25, 0, 23, '0,23', 'DIRECT', 'DIRECT', 2, 1, 'DIRECT', '2022-05-18 14:51:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (26, 0, 23, '0,23', 'SOCKS', 'SOCKS', 3, 1, 'SOCKS', '2022-05-18 14:52:00', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (27, 0, 0, '0', '第三方登录名称', 'JUST_SOURCE_NAME', 9, 1, 'JustAuth默认实现的第三方登录名称', '2022-05-18 14:57:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (28, 0, 27, '0,27', 'GITHUB', 'GITHUB', 1, 1, '', '2022-05-18 15:17:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (29, 0, 27, '0,27', 'WEIBO', 'WEIBO', 2, 1, 'WEIBO', '2022-05-18 15:17:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (30, 0, 27, '0,27', 'GITEE', 'GITEE', 3, 1, 'GITEE', '2022-05-18 15:17:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (31, 0, 27, '0,27', '钉钉扫码', 'DINGTALK', 4, 1, 'DINGTALK', '2022-05-18 15:18:01', 1, '2022-05-18 15:18:39', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (32, 0, 27, '0,27', '钉钉账号', 'DINGTALK_ACCOUNT', 5, 1, 'DINGTALK_ACCOUNT', '2022-05-18 15:18:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (33, 0, 27, '0,27', '百度', 'BAIDU', 6, 1, 'BAIDU', '2022-05-18 15:18:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (34, 0, 27, '0,27', 'CSDN', 'CSDN', 7, 1, 'CSDN', '2022-05-18 15:19:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (35, 0, 27, '0,27', 'CODING', 'CODING', 8, 1, 'CODING', '2022-05-18 15:19:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (36, 0, 27, '0,27', '开源中国', 'OSCHINA', 9, 1, 'OSCHINA', '2022-05-18 15:19:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (37, 0, 27, '0,27', '支付宝', 'ALIPAY', 10, 1, 'ALIPAY', '2022-05-18 15:19:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (38, 0, 27, '0,27', 'QQ', 'QQ', 11, 1, 'QQ', '2022-05-18 15:21:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (39, 0, 27, '0,27', '微信开放平台', 'WECHAT_OPEN', 12, 1, '微信开放平台', '2022-05-18 15:22:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (40, 0, 27, '0,27', '微信公众平台', 'WECHAT_MP', 13, 1, '微信公众平台', '2022-05-18 15:22:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (41, 0, 27, '0,27', '淘宝', 'TAOBAO', 14, 1, 'TAOBAO', '2022-05-18 15:22:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (42, 0, 27, '0,27', 'GOOGLE', 'GOOGLE', 15, 1, 'GOOGLE', '2022-05-18 15:23:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (43, 0, 27, '0,27', 'FACEBOOK', 'FACEBOOK', 16, 1, 'FACEBOOK', '2022-05-18 15:23:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (44, 0, 27, '0,27', '抖音', 'DOUYIN', 17, 1, 'DOUYIN', '2022-05-18 15:23:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (45, 0, 27, '0,27', '领英', 'LINKEDIN', 18, 1, 'LINKEDIN', '2022-05-18 15:23:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (46, 0, 27, '0,27', '微软', 'MICROSOFT', 19, 1, 'MICROSOFT', '2022-05-18 15:24:00', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (47, 0, 27, '0,27', '微软中国(世纪互联)', 'MICROSOFT_CN', 20, 1, 'MICROSOFT_CN', '2022-05-18 15:24:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (48, 0, 27, '0,27', '小米', 'MI', 21, 1, 'MI', '2022-05-18 15:24:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (49, 0, 27, '0,27', '今日头条', 'TOUTIAO', 22, 1, 'TOUTIAO', '2022-05-18 15:24:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (50, 0, 27, '0,27', 'TEAMBITION', 'TEAMBITION', 23, 1, 'TEAMBITION', '2022-05-18 15:24:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (51, 0, 27, '0,27', '人人网', 'RENREN', 24, 1, 'RENREN', '2022-05-18 15:25:14', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (52, 0, 27, '0,27', 'PINTEREST', 'PINTEREST', 25, 1, 'PINTEREST', '2022-05-18 15:25:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (53, 0, 27, '0,27', 'Stack Overflow', 'STACK_OVERFLOW', 26, 1, 'STACK_OVERFLOW', '2022-05-18 15:25:43', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (54, 0, 27, '0,27', '华为', 'HUAWEI', 27, 1, 'HUAWEI', '2022-05-18 15:26:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (55, 0, 27, '0,27', '企业微信二维码', 'WECHAT_ENTERPRISE', 28, 1, 'WECHAT_ENTERPRISE', '2022-05-18 15:27:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (56, 0, 27, '0,27', '企业微信二维码第三方', 'WECHAT_ENTERPRISE_QRCODE_THIRD', 29, 1, 'WECHAT_ENTERPRISE_QRCODE_THIRD', '2022-05-18 15:27:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (57, 0, 27, '0,27', '企业微信网页登录', 'WECHAT_ENTERPRISE_WEB', 30, 1, 'WECHAT_ENTERPRISE_WEB', '2022-05-18 15:28:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (58, 0, 27, '0,27', '酷家乐', 'KUJIALE', 31, 1, 'KUJIALE', '2022-05-18 15:28:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (59, 0, 27, '0,27', 'Gitlab', 'GITLAB', 32, 1, 'GITLAB', '2022-05-18 15:28:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (60, 0, 27, '0,27', '美团', 'MEITUAN', 33, 1, 'MEITUAN', '2022-05-18 15:29:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (61, 0, 27, '0,27', '饿了么', 'ELEME', 34, 1, 'ELEME', '2022-05-18 15:29:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (62, 0, 27, '0,27', 'Twitter', 'TWITTER', 35, 1, 'TWITTER', '2022-05-18 15:29:33', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (63, 0, 27, '0,27', '飞书', 'FEISHU', 36, 1, 'FEISHU', '2022-05-18 15:29:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (64, 0, 27, '0,27', '京东', 'JD', 37, 1, 'JD', '2022-05-18 15:29:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (65, 0, 27, '0,27', '阿里云', 'ALIYUN', 38, 1, 'ALIYUN', '2022-05-18 15:30:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (66, 0, 27, '0,27', '喜马拉雅', 'XMLY', 39, 1, 'XMLY', '2022-05-18 15:30:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (67, 0, 27, '0,27', 'Amazon', 'AMAZON', 40, 1, 'Amazon', '2022-05-18 15:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (68, 0, 27, '0,27', 'Slack', 'SLACK', 41, 1, 'SLACK', '2022-05-18 15:30:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (69, 0, 27, '0,27', 'LINE', 'LINE', 42, 1, 'LINE', '2022-05-18 15:31:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (70, 0, 27, '0,27', 'OKTA', 'OKTA', 43, 1, 'OKTA', '2022-05-18 15:31:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (71, 0, 27, '0,27', '程序员客栈', 'PROGINN', 44, 1, 'PROGINN', '2022-05-18 15:31:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (72, 0, 0, '0', '流程类型', 'PROCESS_MODEL_TYPE', 10, 1, '流程类型', '2022-07-10 21:48:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (73, 0, 72, '0,72', '财务', 'MODEL_TYPE_FINANCE', 1, 1, '财务', '2022-07-10 21:50:15', 1, '2022-07-11 16:46:07', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (74, 0, 72, '0,72', '人事', 'MODEL_TYPE_HUMAN_AFFAIRS', 2, 1, '人事', '2022-07-10 21:53:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (75, 0, 72, '0,72', '出勤休假', 'MODEL_TYPE_ATTENDANCE', 3, 1, '出勤休假', '2022-07-10 21:54:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (76, 0, 72, '0,72', '行政', 'MODEL_TYPE_ADMINISTRATION', 4, 1, '行政', '2022-07-10 21:55:40', 1, '2022-07-10 22:01:09', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (77, 0, 72, '0,72', '其他', 'MODEL_TYPE_OTHER', 5, 1, '其他', '2022-07-10 22:01:37', 1, '2022-07-10 22:01:58', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (78, 0, 0, '0', '性别', 'GENDER', 11, 1, '性别', '2022-11-03 19:29:44', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (79, 0, 78, '0,78', '男', '1', 1, 1, '男', '2022-11-03 19:29:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (80, 0, 78, '0,78', '女', '0', 2, 1, '女', '2022-11-03 19:30:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (81, 0, 78, '0,78', '保密', '2', 3, 1, '保密', '2022-11-03 19:31:34', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (82, 0, 0, '0', '用户状态', 'USER_STATUS', 12, 1, '用户状态', '2022-11-03 19:47:31', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (83, 0, 82, '0,82', '禁用', '0', 1, 1, '禁用', '2022-11-03 19:47:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (84, 0, 82, '0,82', '启用', '1', 2, 1, '启用', '2022-11-03 19:48:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (85, 0, 82, '0,82', '密码过期或初次未修改', '2', 3, 1, '密码过期或初次未修改', '2022-11-03 19:48:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (88, 0, 0, '0', 'API接口', 'API_DICT', 13, 1, 'API接口', '2022-12-07 20:20:31', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (122, 0, 0, '0', 'Tag渲染颜色SUCCESS', 'TAG_COLOR_SUCCESS', 15, 1, 'Tag渲染颜色', '2022-12-28 11:49:26', 1, '2022-12-28 11:52:54', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (123, 0, 122, '0,122', 'green', '1', 1, 1, '成功', '2022-12-28 11:49:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (124, 0, 122, '0,122', 'red', '0', 2, 1, '失败', '2022-12-28 11:50:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (125, 0, 0, '0', 'Tag渲染颜色', 'TAG_COLOR', 15, 1, 'Tag渲染颜色', '2022-12-28 11:49:26', 1, '2023-02-05 19:53:16', 1, 0);
INSERT INTO `t_sys_dict_business` VALUES (126, 0, 125, '0,125', 'green', '1', 1, 1, '成功', '2022-12-28 11:49:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_dict_business` VALUES (127, 0, 125, '0,125', 'orange', '0', 2, 1, '其他', '2022-12-28 11:50:18', 1, NULL, NULL, 0);


-- ----------------------------
-- Table structure for t_sys_district
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_district`;
CREATE TABLE `t_sys_district`  (
  `id` int(5) NULL DEFAULT NULL,
  `district_name` varchar(270) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` int(5) NULL DEFAULT NULL,
  `initial` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `initials` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pinyin` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `suffix` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `district_order` int(2) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_sys_district_bak_new
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_district_bak_new`;
CREATE TABLE `t_sys_district_bak_new`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `district_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行政区域编码',
  `district_name` varchar(270) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行政区域名称',
  `parent_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级行政区域编码',
  `district_level` int(2) NULL DEFAULT NULL COMMENT '行政区域级别：省、市、区',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_district_bak_new
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_log`;
CREATE TABLE `t_sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `in_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '入参',
  `out_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '出参',
  `log_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '日志类型 1、操作',
  `operation_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作名称',
  `operation_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作的IP',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_log
-- ----------------------------
INSERT INTO `t_sys_log` VALUES (1, 0, '1', '1', '1', '1', '1', '1', '2020-11-30 17:51:12', 1, '2020-11-30 17:51:15', 1, 0);

-- ----------------------------
-- Table structure for t_sys_log_api
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_log_api`;
CREATE TABLE `t_sys_log_api`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `request_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  `query_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'GET请求查询参数',
  `request_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `response_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '返回码',
  `response_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回参数',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `duration` bigint(20) NULL DEFAULT NULL COMMENT '耗费时长',
  `scheme` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT 'HTTP   HTTPS',
  `method` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT 'POST   GET',
  `client_host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端HOST',
  `client_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否删除 1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_log_api
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_mail_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_mail_channel`;
CREATE TABLE `t_sys_mail_channel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `channel_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道编码',
  `channel_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道名称',
  `host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SMTP服务器地址',
  `port` int(11) NULL DEFAULT NULL COMMENT 'SMTP服务器端口',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `protocol` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'smtp' COMMENT '协议',
  `default_encoding` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认编码',
  `jndi_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话JNDI名称',
  `properties` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JavaMail 配置',
  `channel_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '渠道状态 1有效 0禁用',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'MD5',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件渠道' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_sys_mail_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_mail_log`;
CREATE TABLE `t_sys_mail_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `channel_id` bigint(20) NULL DEFAULT NULL COMMENT 'mail渠道id',
  `template_id` bigint(20) NULL DEFAULT NULL COMMENT 'mail模板id',
  `mail_subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮件主题',
  `mail_from` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人',
  `mail_to` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '收件人',
  `mail_cc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '抄送',
  `mail_bcc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '密抄送',
  `mail_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '邮件内容',
  `attachment_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '附件名称',
  `attachment_size` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '附件大小',
  `send_time` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `send_result_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '发送结果码',
  `send_result_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送结果消息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否删除 1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件记录' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for t_sys_mail_template
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_mail_template`;
CREATE TABLE `t_sys_mail_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `template_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板编码',
  `template_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `sign_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板签名',
  `template_status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '模板状态',
  `template_type` tinyint(2) NULL DEFAULT NULL COMMENT '模板类型',
  `template_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模板内容',
  `cache_code_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存key',
  `cache_time_out` bigint(20) NULL DEFAULT 0 COMMENT '缓存有效期 值',
  `cache_time_out_unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存有效期 单位',
  `send_times_limit` bigint(20) NULL DEFAULT 0 COMMENT '发送次数限制',
  `send_times_limit_period` bigint(20) NULL DEFAULT 0 COMMENT '限制时间间隔',
  `send_times_limit_period_unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限制时间间隔 单位',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_mail_template
-- ----------------------------
INSERT INTO `t_sys_mail_template` VALUES (8, 0, 'normal_test', '测试普通模板', '测试', 1, 1, '阿斯顿发斯蒂芬{aaa},asdfasdf{bbb}', NULL, 0, NULL, 0, 0, NULL, 'asdfasdfasdf', '2022-06-24 18:04:36', 1, '2022-06-24 18:12:20', 1, 0);
INSERT INTO `t_sys_mail_template` VALUES (9, 0, 'register_code', '注册验证码', '【GitEgg】', 0, 2, '您正在注册GitEgg，验证码为: {code}', 'gitegg:register:mail:code:', 600, 'min', 5, 5, 'min', '验证码邮件', '2022-06-28 18:18:43', 1, '2022-12-07 10:44:40', 1, 0);
INSERT INTO `t_sys_mail_template` VALUES (10, 0, 'register_code', '注册验证码', '【GitEgg】', 1, 2, '您正在注册GitEgg，验证码为: {code}', 'gitegg:register:mail:code:', 600, 'min', 5, 5, 'min', '验证码邮件', '2022-12-07 11:35:08', 1, '2022-12-09 10:11:13', 1, 0);
INSERT INTO `t_sys_mail_template` VALUES (11, 0, 'normal_test', '测试普通模板', '测试', 1, 1, '阿斯顿发斯蒂芬{aaa},asdfasdf{bbb}', NULL, 0, NULL, 0, 0, NULL, 'asdfasdfasdf', '2022-12-07 11:35:08', 1, NULL, NULL, 1);


-- ----------------------------
-- Table structure for t_sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_organization`;
CREATE TABLE `t_sys_organization`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父组织id',
  `ancestors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有上级组织id的集合，便于机构查找',
  `organization_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织类型：1：事业部  2：机构',
  `organization_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `organization_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织编码',
  `organization_icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织图标',
  `organization_level` int(11) NULL DEFAULT NULL COMMENT '组织级别（排序）',
  `organization_status` tinyint(2) NULL DEFAULT 1 COMMENT '1有效，0禁用',
  `province` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `area` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `street` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '街道',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_ORG_NAME`(`organization_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_organization
-- ----------------------------
INSERT INTO `t_sys_organization` VALUES (1, 0, 0, '0', '1', '有限公司', '0001', '11', 1, 1, '140000', '140400', '140421', '111111', '有限公司', '2018-08-26 10:40:49', 1, '2020-12-27 16:31:22', 1, 0);
INSERT INTO `t_sys_organization` VALUES (7, 0, 1, '0,1', '3', '部门一', 'org_1', '111', 1, 1, NULL, NULL, NULL, '', '', '2020-12-04 00:39:25', 1, NULL, NULL, 1);
INSERT INTO `t_sys_organization` VALUES (9, 0, 16, '0,15,16', '1', '有限公司2', '221', '12221', 21, 1, '110000', '110100', '110101', '1', '1', '2020-12-24 18:18:46', 1, '2021-07-06 15:02:32', 1, 1);
INSERT INTO `t_sys_organization` VALUES (10, 0, 0, '0', '1', '有限公司3', '333', '22', 3, 1, '120000', '120100', '120101', '', '', '2020-12-24 18:19:00', 1, '2023-02-05 19:52:55', 1, 0);
INSERT INTO `t_sys_organization` VALUES (11, 0, 0, '0', '1', '有限公司42', '12312', '12312', 4, 1, '140000', '140300', '140311', '7777', '222', '2020-12-24 18:19:17', 1, '2022-11-26 14:52:51', 1, 0);
INSERT INTO `t_sys_organization` VALUES (12, 0, 1, '0,1', '2', '分公司', '121', '12', 1, 1, '130000', '130100', '130105', '', '', '2020-12-24 18:19:37', 1, NULL, NULL, 1);
INSERT INTO `t_sys_organization` VALUES (13, 0, 1, '0,1', '2', '分公司2', '32342', '232', 2, 1, '140000', '140200', '140203', '', '', '2020-12-24 18:20:09', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization` VALUES (14, 0, 10, '0,10', '1', '111', '222', '33', 2, 1, '120000', '120100', '120101', '111', '1111', '2021-03-01 14:00:44', 1, '2021-07-06 14:34:36', 1, 0);
INSERT INTO `t_sys_organization` VALUES (15, 0, 0, '0', '1', '222', '212', '222', 6, 1, '110000', '110100', '110101', '23423', '424234', '2021-07-06 14:41:12', 1, NULL, NULL, 1);
INSERT INTO `t_sys_organization` VALUES (16, 0, 15, '0,15', '1', '23', '22333', '22', 3, 1, '120000', '120100', '120101', '232', '2323', '2021-07-06 14:41:31', 1, NULL, NULL, 1);
INSERT INTO `t_sys_organization` VALUES (17, 0, 11, '0,11', '1', '555', '555', '555', 555, 1, '110000', '110100', '110101', '98', '888', '2021-07-06 17:46:41', 1, '2022-12-13 16:29:17', 1, 0);
INSERT INTO `t_sys_organization` VALUES (18, 0, 17, '0,11,17', '2', '345345345', '34534545', 'ant-design:align-right-outlined', 5, 0, '130000', '130300', '130304', '345345', '34534534', '2022-11-24 19:33:41', 1, '2022-11-24 19:40:15', 1, 0);

-- ----------------------------
-- Table structure for t_sys_organization_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_organization_role`;
CREATE TABLE `t_sys_organization_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `organization_id` bigint(20) NOT NULL COMMENT '组织机构id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '可以给组织权限，在该组织下的所有用户都有此权限' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_organization_role
-- ----------------------------
INSERT INTO `t_sys_organization_role` VALUES (1, 0, 1, 0, '2016-04-20 14:21:12', 1, '2016-04-20 14:21:12', 1, 0);

-- ----------------------------
-- Table structure for t_sys_organization_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_organization_user`;
CREATE TABLE `t_sys_organization_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `organization_id` bigint(20) NOT NULL COMMENT '机构id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_organization_user
-- ----------------------------
INSERT INTO `t_sys_organization_user` VALUES (1, 0, 1, 1, '2016-05-27 10:45:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (2, 0, 1, 2, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (3, 0, 1, 3, '2020-11-27 02:55:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (5, 0, 9, 4, '2020-12-25 15:19:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (6, 0, 12, 5, '2020-12-25 17:02:36', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (7, 0, 13, 6, '2021-01-11 09:55:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (8, 0, 14, 2, '2021-07-06 09:54:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (9, 0, 14, 7, '2021-07-06 18:17:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (10, 0, 12, 8, '2021-08-26 17:28:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (11, 0, 12, 7, '2021-12-22 17:06:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (12, 0, 12, 2, '2021-12-22 17:06:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (13, 0, 79, 9, '2022-05-29 21:39:05', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (14, 0, 79, 10, '2022-05-29 21:52:59', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (15, 0, 79, 11, '2022-05-29 21:57:37', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (16, 0, 79, 12, '2022-05-29 22:12:09', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (17, 0, 79, 13, '2022-05-29 22:30:56', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (18, 0, 79, 14, '2022-05-29 22:38:29', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (19, 0, 79, 15, '2022-05-29 23:04:55', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (20, 0, 79, 16, '2022-05-29 23:08:52', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (21, 0, 1, 17, '2022-05-29 23:12:29', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (22, 0, 1, 18, '2022-05-30 10:28:54', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (23, 0, 1, 19, '2022-05-30 14:10:44', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (24, 0, 1, 20, '2022-05-30 14:12:58', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (25, 0, 1, 21, '2022-05-30 14:25:16', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (26, 0, 1, 22, '2022-05-30 14:27:28', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (27, 0, 1, 23, '2022-05-30 14:31:36', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (28, 0, 1, 24, '2022-05-30 14:34:06', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (29, 0, 1, 25, '2022-06-01 18:39:10', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (30, 0, 1, 26, '2022-06-01 18:45:59', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (31, 0, 1, 27, '2022-06-01 18:58:06', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (32, 0, 1, 28, '2022-06-02 18:18:35', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (33, 0, 1, 29, '2022-06-02 18:21:02', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (34, 0, 1, 30, '2022-06-02 18:34:12', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (35, 0, 1, 31, '2022-06-02 19:11:00', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (36, 0, 1, 32, '2022-06-03 10:22:26', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (37, 0, 1, 33, '2022-06-03 12:22:08', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (38, 0, 1, 34, '2022-06-03 12:34:35', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (39, 0, 1, 35, '2022-06-03 12:46:13', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (40, 0, 1, 36, '2022-06-05 20:23:36', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (41, 0, 1, 37, '2022-06-17 16:47:06', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (42, 0, 1, 38, '2022-06-20 14:28:54', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (43, 0, 1, 39, '2022-07-05 17:24:27', 1, NULL, NULL, 1);
INSERT INTO `t_sys_organization_user` VALUES (44, 0, 12, 40, '2022-11-03 20:12:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (45, 0, 11, 41, '2022-11-07 15:13:16', 1, NULL, NULL, 1);
INSERT INTO `t_sys_organization_user` VALUES (46, 0, 17, 42, '2022-11-22 13:45:46', 1, NULL, NULL, 1);
INSERT INTO `t_sys_organization_user` VALUES (47, 0, 17, 43, '2022-11-24 17:47:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (48, 0, 11, 44, '2022-12-16 14:21:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (49, 0, 11, 45, '2022-12-16 14:22:05', 1, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (50, 0, 1, 46, '2023-01-29 17:48:33', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (51, 0, 1, 47, '2023-02-01 23:03:31', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (52, 0, 1, 48, '2023-02-02 15:34:16', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (53, 0, 1, 49, '2023-02-02 23:26:17', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_organization_user` VALUES (54, 0, 1, 50, '2023-02-02 23:28:35', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_resource`;
CREATE TABLE `t_sys_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父id',
  `ancestors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有上级资源id的集合，便于查找',
  `resource_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `resource_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源标识',
  `resource_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源类型 1、模块 2、菜单 3、按钮 4、链接',
  `resource_icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源图标',
  `resource_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源路径：浏览器访问路径',
  `resource_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源链接: 前端页面路径或接口地址',
  `resource_level` int(11) NULL DEFAULT NULL COMMENT '资源级别',
  `resource_show` tinyint(1) NULL DEFAULT NULL COMMENT '是否显示',
  `current_active_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当类型为菜单且不展示时，左侧菜单的选中项',
  `resource_cache` tinyint(1) NULL DEFAULT NULL COMMENT '是否缓存',
  `resource_page_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源页面名称',
  `tab_show` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否在tab中显示',
  `resource_status` tinyint(2) NULL DEFAULT 1 COMMENT '1有效，0禁用',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_PERM_NAME`(`resource_name`) USING BTREE,
  INDEX `INDEX_PERM_PID`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 349 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_resource
-- ----------------------------
INSERT INTO `t_sys_resource` VALUES (1, 0, 0, '0', '基础系统管理', 'system:mgr', '1', 'xitongguanli', 'system', 'system', 1000, 1, NULL, 1, NULL, 1, 1, '基础系统管理', '2016-04-22 10:43:19', 1, '2023-02-05 19:52:48', 1, 0);
INSERT INTO `t_sys_resource` VALUES (2, 0, 1, '0,1', '系统管理', 'system:cfg', '2', 'xitongguanli', 'system/cfg', 'Layout', 1000, 1, NULL, 1, NULL, 1, 1, '配置管理', '2016-04-22 10:43:19', 1, '2018-11-10 11:19:02', 1, 0);
INSERT INTO `t_sys_resource` VALUES (3, 0, 2, '0,1,2', '权限管理', 'system:permission', '2', 'quanxianguanli', 'permission', 'nested', 1, 1, NULL, 1, NULL, 1, 1, '权限管理', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (4, 0, 3, '0,1,2,3', '用户管理', 'system:user:table', '2', 'yonghuguanli', 'system/user/table', 'system/user/UserTable', 1, 1, NULL, 1, 'userTable', 1, 1, '用户管理菜单', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:42', 1, 0);
INSERT INTO `t_sys_resource` VALUES (5, 0, 3, '0,1,2,3', '角色管理', 'system:role:table', '2', 'jiaoseguanli', 'system/role/table', 'system/role/RoleTable', 2, 1, NULL, 1, 'roleTable', 1, 1, '角色管理', '2016-04-22 10:43:19', 1, '2018-11-02 14:50:45', 1, 0);
INSERT INTO `t_sys_resource` VALUES (6, 0, 3, '0,1,2,3', '资源管理', 'system:resource:table', '2', 'ziyuanguanli', 'system/resource/table', 'system/resource/ResourceTable', 3, 1, NULL, 1, 'resourceTable', 1, 1, '资源管理', '2016-04-22 10:43:19', 1, '2018-11-02 14:50:54', 1, 0);
INSERT INTO `t_sys_resource` VALUES (7, 0, 3, '0,1,2,3', '组织管理', 'system:organization:table', '2', 'zuzhiguanli', 'system/organization/table', 'system/organization/OrganizationTable', 4, 1, NULL, 1, 'organizationTable', 1, 1, '组织管理', '2016-04-22 10:43:19', 1, '2018-11-02 14:50:06', 1, 0);
INSERT INTO `t_sys_resource` VALUES (8, 0, 2, '0,1,2', '基础配置', 'system:base:cfg', '2', 'jichupeizhi', 'system/base', 'nested', 2, 1, NULL, 1, 'systemBase', 1, 1, '系统配置', '2016-04-22 11:03:14', 1, '2018-11-10 10:17:11', 1, 0);
INSERT INTO `t_sys_resource` VALUES (9, 0, 8, '0,1,2,8', '系统字典', 'system:config:dict', '2', 'shujuzidian', 'system/dict/table', 'system/base/dict/DictTable', 1, 1, NULL, 1, 'dictTable', 1, 1, '系统数据字典', '2018-10-27 17:53:49', 1, '2021-09-30 16:52:04', 1, 0);
INSERT INTO `t_sys_resource` VALUES (10, 0, 8, '0,1,2,8', '定时任务', 'system:task', '2', 'timingtask', 'https://github.com/xuxueli/xxl-job', 'system/task', 3, 1, NULL, 1, 'XXLJob', 1, 1, '', '2018-10-27 19:09:06', 1, '2021-09-30 16:36:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (11, 0, 2, '0,1,2', '日志管理', 'system:log:mgr', '2', 'rizhiguanli', 'system/log', 'nested', 100, 1, NULL, 1, NULL, 1, 1, '', '2018-10-27 17:37:17', 1, '2022-05-16 14:13:24', 1, 0);
INSERT INTO `t_sys_resource` VALUES (12, 0, 11, '0,1,2,11', '操作日志', 'system:log:operation', '2', 'caozuorizhi', 'system/log/operation/table', 'system/base/log/LogTable', 1, 1, NULL, 1, 'logTable', 1, 1, '管理员操作日志，只记录重要关键日志，请勿频繁记录，系统运行日志记录到log文件。', '2018-10-27 17:39:33', 1, '2020-11-29 20:16:01', 1, 0);
INSERT INTO `t_sys_resource` VALUES (13, 0, 11, '0,1,2,11', '系统日志', 'system:log:run', '2', 'xitongrizhi', 'https://my.oschina.net/feinik/blog/1580625', 'system', 2, 1, NULL, 1, NULL, 1, 1, '运行日志使用ELK', '2018-10-27 17:40:14', 1, '2021-05-14 17:13:49', 1, 0);
INSERT INTO `t_sys_resource` VALUES (14, 0, 4, '0,1,2,3,4', '获取用户信息', 'system:auth:user:info', '4', 'xitongrizhi', 'system/user/info', '/gitegg-service-system/auth/user/info', 1, 1, NULL, 1, NULL, 1, 1, '登录用户获取登录信息', '2018-10-27 17:40:14', 1, '2021-05-14 17:46:43', 1, 0);
INSERT INTO `t_sys_resource` VALUES (15, 0, 4, '0,1,2,3,4', '获取用户列表', 'system:user:list', '4', 'xitongrizhi', 'system/user/list', '/gitegg-service-system/user/list', 2, 1, NULL, 1, NULL, 1, 1, '获取用户列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (16, 0, 7, '0,1,2,3,7', '获取组织机构列表', 'system:organization:list', '4', 'xitongrizhi', 'system/organization/list', '/gitegg-service-system/organization/tree', 2, 1, NULL, 1, NULL, 1, 1, '获取组织机构列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (17, 0, 6, '0,1,2,3,6', '获取资源权限列表', 'system:resource:list', '4', 'xitongrizhi', 'system/resource/list', '/gitegg-service-system/resource/tree', 2, 1, NULL, 1, NULL, 1, 1, '获取资源权限列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (18, 0, 5, '0,1,2,3,5', '获取角色列表', 'system:role:list', '4', 'xitongrizhi', 'system/role/list', '/gitegg-service-system/role/list', 2, 1, NULL, 1, NULL, 1, 1, '获取角色列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (19, 0, 5, '0,1,2,3,5', '获取所有角色', 'system:role:all', '4', 'xitongrizhi', 'system/role/all', '/gitegg-service-system/role/all', 2, 1, NULL, 1, NULL, 1, 1, '获取所有角色数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (20, 0, 4, '0,1,2,3,4', '创建用户接口', 'system:user:create', '4', 'xitongrizhi', 'system/user/create', '/gitegg-service-system/user/create', 3, 1, NULL, 1, NULL, 1, 1, '创建用户接口', '2018-10-27 17:40:14', 1, '2021-05-14 17:46:48', 1, 0);
INSERT INTO `t_sys_resource` VALUES (21, 0, 4, '0,1,2,3,4', '更新用户接口', 'system:user:update', '4', 'xitongrizhi', 'system/user/update', '/gitegg-service-system/user/update', 4, 1, NULL, 1, NULL, 1, 1, '更新用户接口', '2018-10-27 17:40:14', 1, '2021-05-14 17:46:52', 1, 0);
INSERT INTO `t_sys_resource` VALUES (22, 0, 4, '0,1,2,3,4', '删除用户接口', 'system:user:delete', '4', 'xitongrizhi', 'system/user/delete', '/gitegg-service-system/user/delete/{userId}', 5, 1, NULL, 1, NULL, 1, 1, '删除用户接口', '2018-10-27 17:40:14', 1, '2021-05-14 17:46:56', 1, 0);
INSERT INTO `t_sys_resource` VALUES (23, 0, 4, '0,1,2,3,4', '批量删除用户接口', 'system:user:batch:delete', '4', 'xitongrizhi', 'system/user/batch/delete', '/gitegg-service-system/user/batch/delete', 6, 1, NULL, 1, NULL, 1, 1, '批量删除用户接口', '2018-10-27 17:40:14', 1, '2021-05-14 17:46:59', 1, 0);
INSERT INTO `t_sys_resource` VALUES (24, 0, 4, '0,1,2,3,4', '用户修改自己密码', 'system:user:password:change', '4', 'xitongrizhi', 'system/user/password/change', '/gitegg-service-system/user/password/change', 7, 1, NULL, 1, NULL, 1, 1, '用户修改自己密码', '2018-10-27 17:40:14', 1, '2021-05-14 17:47:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (25, 0, 4, '0,1,2,3,4', '管理员重置密码', 'system:user:password:reset', '4', 'xitongrizhi', 'system/user/password/reset', '/gitegg-service-system/user/password/reset/{userId}', 8, 1, NULL, 1, NULL, 1, 1, '管理员重置密码', '2018-10-27 17:40:14', 1, '2021-05-14 17:47:07', 1, 0);
INSERT INTO `t_sys_resource` VALUES (26, 0, 4, '0,1,2,3,4', '修改用户状态', 'system:user:status', '4', 'xitongrizhi', 'system/user/status', '/gitegg-service-system/user/status/{userId}/{status}', 9, 1, NULL, 1, NULL, 1, 1, '修改用户状态', '2018-10-27 17:40:14', 1, '2021-05-14 17:47:11', 1, 0);
INSERT INTO `t_sys_resource` VALUES (27, 0, 4, '0,1,2,3,4', '用户自己修改信息', 'system:user:update:info', '4', 'xitongrizhi', 'system/user/update/info', '/gitegg-service-system/user/update/info', 10, 1, NULL, 1, NULL, 1, 1, '用户自己修改信息', '2018-10-27 17:40:14', 1, '2021-05-14 17:47:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (29, 0, 4, '0,1,2,3,4', '检查用户是否存在', 'system:user:check', '4', 'xitongrizhi', 'system/role/check', '/gitegg-service-system/user/check', 11, 1, NULL, 1, NULL, 1, 1, '检查用户是否存在', '2018-10-27 17:40:14', 1, '2021-05-14 17:47:21', 1, 0);
INSERT INTO `t_sys_resource` VALUES (30, 0, 5, '0,1,2,3,5', '添加角色', 'system:role:create', '4', 'xitongrizhi', 'system/role/create', '/gitegg-service-system/role/create', 2, 1, NULL, 1, NULL, 1, 1, '添加角色', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (31, 0, 5, '0,1,2,3,5', '更新角色', 'system:role:update', '4', 'xitongrizhi', 'system/role/update', '/gitegg-service-system/role/update', 2, 1, NULL, 1, NULL, 1, 1, '更新角色', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (32, 0, 5, '0,1,2,3,5', '删除角色', 'system:role:delete', '4', 'xitongrizhi', 'system/role/delete', '/gitegg-service-system/role/delete/{roleId}', 2, 1, NULL, 1, NULL, 1, 1, '删除角色', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (33, 0, 5, '0,1,2,3,5', '批量删除角色', 'system:role:batch:delete', '4', 'xitongrizhi', 'system/role/batch/delete', '/gitegg-service-system/role/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除角色', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (34, 0, 5, '0,1,2,3,5', '修改角色状态', 'system:role:status', '4', 'xitongrizhi', 'system/role/status', '/gitegg-service-system/role/status/{roleId}/{roleStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改角色状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (35, 0, 5, '0,1,2,3,5', '获取角色的资源权限', 'system:role:resource:list', '4', 'xitongrizhi', 'system/role/resource/list', '/gitegg-service-system/role/resource/{roleId}', 2, 1, NULL, 1, NULL, 1, 1, '获取角色的资源权限', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (36, 0, 5, '0,1,2,3,5', '修改角色的资源权限', 'system:role:resource:update', '4', 'xitongrizhi', 'system/role/resource/update', '/gitegg-service-system/role/resource/update', 2, 1, NULL, 1, NULL, 1, 1, '修改角色的资源权限', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (37, 0, 5, '0,1,2,3,5', '查询所有角色列表', 'system:role:all', '4', 'xitongrizhi', 'system/role/all', '/gitegg-service-system/role/all', 2, 1, NULL, 1, NULL, 1, 1, '查询所有角色列表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (38, 0, 5, '0,1,2,3,5', '校验角色是否存在', 'system:role:check', '4', 'xitongrizhi', 'system/role/check', '/gitegg-service-system/role/check', 2, 1, NULL, 1, NULL, 1, 1, '校验角色是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (39, 0, 6, '0,1,2,3,6', '添加资源', 'system:resource:create', '4', 'xitongrizhi', 'system/resource/create', '/gitegg-service-system/resource/create', 2, 1, NULL, 1, NULL, 1, 1, '添加资源', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (40, 0, 6, '0,1,2,3,6', '更新资源', 'system:resource:update', '4', 'xitongrizhi', 'system/resource/update', '/gitegg-service-system/resource/update', 2, 1, NULL, 1, NULL, 1, 1, '更新资源', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (41, 0, 6, '0,1,2,3,6', '删除资源', 'system:resource:delete', '4', 'xitongrizhi', 'system/resource/delete', '/gitegg-service-system/resource/delete/{resourceId}', 2, 1, NULL, 1, NULL, 1, 1, '删除资源', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (42, 0, 6, '0,1,2,3,6', '修改资源状态', 'system:resource:status', '4', 'xitongrizhi', 'system/resource/status', '/gitegg-service-system/resource/status/{resourceId}/{resourceStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改资源状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (43, 0, 6, '0,1,2,3,6', '获取用户权限菜单', 'system:resource:menu', '4', 'xitongrizhi', 'system/resource/menu', '/gitegg-service-system/resource/menu', 2, 1, NULL, 1, NULL, 1, 1, '获取用户权限菜单', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (44, 0, 6, '0,1,2,3,6', '校验资源是否存在', 'system:resource:check', '4', 'xitongrizhi', 'system/resource/check', '/gitegg-service-system/resource/check', 2, 1, NULL, 1, NULL, 1, 1, '校验资源是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (48, 0, 7, '0,1,2,3,7', '添加组织', 'system:organization:create', '4', 'xitongrizhi', 'system/organization/create', '/gitegg-service-system/organization/create', 2, 1, NULL, 1, NULL, 1, 1, '添加组织', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (49, 0, 7, '0,1,2,3,7', '更新组织', 'system:organization:update', '4', 'xitongrizhi', 'system/organization/update', '/gitegg-service-system/organization/update', 2, 1, NULL, 1, NULL, 1, 1, '更新组织', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (50, 0, 7, '0,1,2,3,7', '删除组织', 'system:organization:delete', '4', 'xitongrizhi', 'system/organization/delete', '/gitegg-service-system/organization/delete/{organizationId}', 2, 1, NULL, 1, NULL, 1, 1, '删除组织', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (51, 0, 7, '0,1,2,3,7', '修改组织状态', 'system:organization:status', '4', 'xitongrizhi', 'system/organization/status', '/gitegg-service-system/organization/status/{organizationId}/{organizationStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改组织状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (52, 0, 7, '0,1,2,3,7', '查询组织树', 'system:organization:tree', '4', 'xitongrizhi', 'system/organization/tree', '/gitegg-service-system/organization/tree', 2, 1, NULL, 1, NULL, 1, 1, '查询组织树', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (53, 0, 7, '0,1,2,3,7', '校验组织是否存在', 'system:organization:check', '4', 'xitongrizhi', 'system/organization/check', '/gitegg-service-system/organization/check', 2, 1, NULL, 1, NULL, 1, 1, '校验组织是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (54, 0, 9, '0,1,2,8,9', '添加系统数据字典', 'base:dict:create', '4', 'xitongrizhi', 'base/dict/create', '/gitegg-service-base/dict/create', 2, 1, NULL, 1, NULL, 1, 1, '添加数据字典', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (55, 0, 9, '0,1,2,8,9', '更新系统数据字典', 'base:dict:update', '4', 'xitongrizhi', 'base/dict/update', '/gitegg-service-base/dict/update', 2, 1, NULL, 1, NULL, 1, 1, '更新数据字典', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (56, 0, 9, '0,1,2,8,9', '删除系统数据字典', 'base:dict:delete', '4', 'xitongrizhi', 'base/dict/delete', '/gitegg-service-base/dict/delete/{dictId}', 2, 1, NULL, 1, NULL, 1, 1, '删除数据字典', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (57, 0, 9, '0,1,2,8,9', '修改系统数据字典状态', 'base:dict:status', '4', 'xitongrizhi', 'base/dict/status', '/gitegg-service-base/dict/status/{dictId}/{dictStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改数据字典状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (58, 0, 9, '0,1,2,8,9', '查询系统数据字典树', 'base:dict:tree', '4', 'xitongrizhi', 'base/dict/tree', '/gitegg-service-base/dict/list', 2, 1, NULL, 1, NULL, 1, 1, '查询数据字典树', '2018-10-27 17:40:14', 1, '2021-01-04 18:20:56', 1, 0);
INSERT INTO `t_sys_resource` VALUES (59, 0, 9, '0,1,2,8,9', '校验系统数据字典是否存在', 'base:dict:check', '4', 'xitongrizhi', 'base/dict/check', '/gitegg-service-base/dict/check', 2, 1, NULL, 1, NULL, 1, 1, '校验数据字典是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (60, 0, 12, '0,1,2,11,12', '查询操作日志', 'base:log:list', '4', 'xitongrizhi', 'base/log/list', '/gitegg-service-base/log/list', 2, 1, NULL, 1, NULL, 1, 1, '查询操作日志', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (61, 0, 8, '0,1,2,8', '租户管理', 'base:tenant:table', '2', 'jiaoseguanli', 'tenant', 'system/base/tenant/TenantTable', 4, 1, NULL, 1, 'tenantTable', 1, 1, '租户信息表', '2016-04-22 10:43:19', 1, '2021-09-30 16:36:58', 1, 0);
INSERT INTO `t_sys_resource` VALUES (62, 0, 61, '0,1,2,8,61', '获取租户列表', 'base:tenant:list', '4', 'xitongrizhi', 'list', '/gitegg-service-base/base/tenant/list', 2, 1, NULL, 1, NULL, 1, 1, '获取租户信息表列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (63, 0, 61, '0,1,2,8,61', '添加租户', 'base:tenant:create', '4', 'xitongrizhi', 'create', '/gitegg-service-base/base/tenant/create', 2, 1, NULL, 1, NULL, 1, 1, '添加租户信息表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (64, 0, 61, '0,1,2,8,61', '更新租户', 'base:tenant:update', '4', 'xitongrizhi', 'update', '/gitegg-service-base/base/tenant/update', 2, 1, NULL, 1, NULL, 1, 1, '更新租户信息表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (65, 0, 61, '0,1,2,8,61', '删除租户', 'base:tenant:delete', '4', 'xitongrizhi', 'delete', '/gitegg-service-base/base/tenant/delete/{tenantId}', 2, 1, NULL, 1, NULL, 1, 1, '删除租户信息表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (66, 0, 61, '0,1,2,8,61', '批量删除租户', 'base:tenant:batch:delete', '4', 'xitongrizhi', 'batch/delete', '/gitegg-service-base/base/tenant/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除租户信息表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (67, 0, 9, '0,1,2,8,9', '批量删除数据字典', 'base:dict:batch:delete', '4', 'xitongrizhi', 'base/dict/batch/delete', '/gitegg-service-base/dict/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除数据字典', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (68, 0, 9, '0,1,2,8,9', '校验字典code是否存在', 'base:dict:check', '4', 'xitongrizhi', 'base/dict/check', '/gitegg-service-base/dict/check', 2, 1, NULL, 1, NULL, 1, 1, '校验字典code是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (69, 0, 9, '0,1,2,8,9', '修改数据字典状态', 'base:dict:status', '4', 'xitongrizhi', 'base/dict/status', '/gitegg-service-base/dict/status/{dictId}/{dictStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改数据字典状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (70, 0, 2, '0,1,2', '短信配置', 'extension:sms:table', '2', 'duanxinpeizhi', 'extension/sms/table', 'nested', 4, 1, NULL, 1, 'smsTable', 1, 1, '短信配置表', '2016-04-22 10:43:19', 1, '2021-05-06 11:10:33', 1, 0);
INSERT INTO `t_sys_resource` VALUES (76, 0, 70, '0,1,2,70', '短信渠道', 'extension:sms:channel:table', '2', 'duanxinqudao', 'extension/sms/channel/table', 'system/extension/sms/SmsChannelTable', 2, 1, NULL, 1, 'smsChannelTable', 1, 1, '短信渠道表', '2016-04-22 10:43:19', 1, '2018-11-02 14:50:45', 1, 0);
INSERT INTO `t_sys_resource` VALUES (77, 0, 76, '0,1,2,70,76', '获取短信渠道表列表', 'extension:sms:channel:list', '4', 'xitongrizhi', 'extension/sms/channel/list', '/gitegg-service-extension/extension/sms/channel/list', 2, 1, NULL, 1, NULL, 1, 1, '获取短信渠道表列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (78, 0, 76, '0,1,2,70,76', '添加短信渠道表', 'extension:sms:channel:create', '4', 'xitongrizhi', 'extension/sms/channel/create', '/gitegg-service-extension/extension/sms/channel/create', 2, 1, NULL, 1, NULL, 1, 1, '添加短信渠道表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (79, 0, 76, '0,1,2,70,76', '更新短信渠道表', 'extension:sms:channel:update', '4', 'xitongrizhi', 'extension/sms/channel/update', '/gitegg-service-extension/extension/sms/channel/update', 2, 1, NULL, 1, NULL, 1, 1, '更新短信渠道表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (80, 0, 76, '0,1,2,70,76', '删除短信渠道表', 'extension:sms:channel:delete', '4', 'xitongrizhi', 'extension/sms/channel/delete', '/gitegg-service-extension/extension/sms/channel/delete/{smsChannelId}', 2, 1, NULL, 1, NULL, 1, 1, '删除短信渠道表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (81, 0, 70, '0,1,2,70', '短信模板', 'extension:sms:template:table', '2', 'duanxinmuban', 'extension/sms/template/table', 'system/extension/sms/SmsTemplateTable', 1, 1, NULL, 1, 'smsTemplateTable', 1, 1, '短信配置表', '2016-04-22 10:43:19', 1, '2021-05-06 11:11:11', 1, 0);
INSERT INTO `t_sys_resource` VALUES (82, 0, 81, '0,1,2,70,81', '获取短信模板列表', 'extension:sms:template:list', '4', 'xitongrizhi', 'extension/sms/template/list', '/gitegg-service-extension/extension/sms/template/list', 2, 1, NULL, 1, NULL, 1, 1, '获取短信配置表列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (83, 0, 81, '0,1,2,70,81', '添加短信模板表', 'extension:sms:template:create', '4', 'xitongrizhi', 'extension/sms/template/create', '/gitegg-service-extension/extension/sms/template/create', 2, 1, NULL, 1, NULL, 1, 1, '添加短信配置表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (84, 0, 81, '0,1,2,70,81', '更新短信模板表', 'extension:sms:template:update', '4', 'xitongrizhi', 'extension/sms/template/update', '/gitegg-service-extension/extension/sms/template/update', 2, 1, NULL, 1, NULL, 1, 1, '更新短信配置表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (85, 0, 81, '0,1,2,70,81', '删除短信模板表', 'extension:sms:template:delete', '4', 'xitongrizhi', 'extension/sms/template/delete', '/gitegg-service-extension/extension/sms/template/delete/{smsTemplateId}', 2, 1, NULL, 1, NULL, 1, 1, '删除短信配置表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (86, 0, 100, '0,1,2,100', '存储配置', 'extension:dfs:table', '2', 'cunchupeizhi', 'extension/dfs/table', 'system/extension/dfs/DfsTable', 5, 1, NULL, 1, 'dfsTable', 1, 1, '分布式存储配置表', '2016-04-22 10:43:19', 1, '2021-05-06 11:10:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (87, 0, 86, '0,1,2,100,86', '存储配置列表', 'extension:dfs:list', '4', 'xitongrizhi', 'extension/dfs/list', '/gitegg-service-extension/extension/dfs/list', 2, 1, NULL, 1, NULL, 1, 1, '存储配置列表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (88, 0, 86, '0,1,2,100,86', '添加分布式存储配置', 'extension:dfs:create', '4', 'xitongrizhi', 'extension/dfs/create', '/gitegg-service-extension/extension/dfs/create', 2, 1, NULL, 1, NULL, 1, 1, '添加分布式存储配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (89, 0, 86, '0,1,2,100,86', '更新分布式存储配置表', 'extension:dfs:update', '4', 'xitongrizhi', 'extension/dfs/update', '/gitegg-service-extension/extension/dfs/update', 2, 1, NULL, 1, NULL, 1, 1, '更新分布式存储配置表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (90, 0, 86, '0,1,2,100,86', '删除分布式存储配置表', 'extension:dfs:delete', '4', 'xitongrizhi', 'extension/dfs/delete', '/gitegg-service-extension/extension/dfs/delete/{dfsId}', 2, 1, NULL, 1, NULL, 1, 1, '删除分布式存储配置表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (91, 0, 86, '0,1,2,100,86', '批量删除分布式存储配置表', 'extension:dfs:batch:delete', '4', 'xitongrizhi', 'extension/dfs/batch/delete', '/gitegg-service-extension/extension/dfs/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除分布式存储配置表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (92, 0, 86, '0,1,2,100,86', '修改分布式存储状态', 'extension:dfs:status', '4', 'xitongrizhi', 'extension/dfs/status', '/gitegg-service-extension/extension/dfs/status/{dfsId}/{dfsStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改存储状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (93, 0, 86, '0,1,2,100,86', '修改分布式存储默认', 'extension:dfs:default', '4', 'xitongrizhi', 'extension/dfs/default', '/gitegg-service-extension/extension/dfs/default/{dfsId}', 2, 1, NULL, 1, NULL, 1, 1, '修改存储默认', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (94, 0, 100, '0,1,2,100', '上传记录', 'extension:dfs:dfs:file:table', '2', 'shangchuanjilu', 'extension/dfs/file/table', 'system/extension/dfs/DfsFileTable', 6, 1, NULL, 1, 'dfsFileTable', 1, 1, '分布式存储文件记录表', '2016-04-22 10:43:19', 1, '2018-11-02 14:50:45', 1, 0);
INSERT INTO `t_sys_resource` VALUES (95, 0, 94, '0,1,2,100,94', '获取分布式存储文件记录表列表', 'extension:dfs:file:list', '4', 'xitongrizhi', 'extension/dfs/file/list', '/gitegg-service-extension/extension/dfs/file/list', 2, 1, NULL, 1, NULL, 1, 1, '获取分布式存储文件记录表列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (96, 0, 94, '0,1,2,100,94', '添加分布式存储文件记录表', 'extension:dfs:file:create', '4', 'xitongrizhi', 'extension/dfs/file/create', '/gitegg-service-extension/extension/dfs/file/create', 2, 1, NULL, 1, NULL, 1, 1, '添加分布式存储文件记录表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (97, 0, 94, '0,1,2,100,94', '更新分布式存储文件记录表', 'extension:dfs:file:update', '4', 'xitongrizhi', 'extension/dfs/file/update', '/gitegg-service-extension/extension/dfs/file/update', 2, 1, NULL, 1, NULL, 1, 1, '更新分布式存储文件记录表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (98, 0, 94, '0,1,2,100,94', '删除分布式存储文件记录表', 'extension:dfs:file:delete', '4', 'xitongrizhi', 'extension/dfs/file/delete', '/gitegg-service-extension/extension/dfs/file/delete/{dfsFileId}', 2, 1, NULL, 1, NULL, 1, 1, '删除分布式存储文件记录表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (99, 0, 94, '0,1,2,100,94', '批量删除分布式存储文件记录表', 'extension:dfs:file:batch:delete', '4', 'xitongrizhi', 'extension/dfs/file/batch/delete', '/gitegg-service-extension/extension/dfs/file/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除分布式存储文件记录表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (100, 0, 2, '0,1,2', '文件存储', 'extension:dfs:file', '2', 'wenjiancunchu', 'extension/dfs/file', 'nested', 3, 1, NULL, 1, 'dfsFileConfigTable', 1, 1, '文件存储', '2016-04-22 10:43:19', 1, '2021-05-16 00:54:01', 1, 0);
INSERT INTO `t_sys_resource` VALUES (101, 0, 94, '0,1,2,100,94', '获取分布式存储文件链接', 'extension:dfs:file:url', '4', 'xitongrizhi', 'extension/dfs/file/url', '/gitegg-service-extension/extension/get/file/url', 2, 1, NULL, 1, NULL, 1, 1, '获取分布式存储文件链接', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (102, 0, 94, '0,1,2,100,94', '下载分布式存储文件', 'extension:dfs:file:download', '4', 'xitongrizhi', 'extension/dfs/file/download', '/gitegg-service-extension/extension/get/file/download', 2, 1, NULL, 1, NULL, 1, 1, '下载分布式存储文件', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (105, 0, 3, '0,1,2,3', '组织权限', 'system:data:permission:user', '2', 'zuzhiquanxian', 'system/data/permission/user', 'system/dataPermission/DataPermissionUser', 6, 1, NULL, 1, 'dataPermissionUserTable', 1, 1, '', '2016-04-22 10:43:19', 1, '2021-05-14 19:49:12', 1, 0);
INSERT INTO `t_sys_resource` VALUES (111, 0, 3, '0,1,2,3', '数据权限', 'system:data:permission:role', '2', 'shujuquanxian', 'system/data/permission/role', 'system/dataPermission/DataPermissionRole', 5, 1, NULL, 1, 'dataPermissionRoleTable', 1, 1, '数据权限', '2016-04-22 10:43:19', 1, '2021-05-13 14:15:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (112, 0, 111, '0,1,2,3,111', '获取数据权限列表', 'system:data:permission:role:list', '4', 'xitongrizhi', 'system/data/permission/role/list', '/gitegg-service-system/data/permission/role/list', 1, 1, NULL, 1, NULL, 1, 1, '获取数据权限列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (113, 0, 111, '0,1,2,3,111', '添加数据权限', 'system:data:permission:role:create', '4', 'xitongrizhi', 'system/data/permission/role/create', '/gitegg-service-system/data/permission/role/create', 2, 1, NULL, 1, NULL, 1, 1, '添加数据权限', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (114, 0, 111, '0,1,2,3,111', '更新数据权限', 'system:data:permission:role:update', '4', 'xitongrizhi', 'system/data/permission/role/update', '/gitegg-service-system/data/permission/role/update', 3, 1, NULL, 1, NULL, 1, 1, '更新数据权限', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (115, 0, 111, '0,1,2,3,111', '删除数据权限', 'system:data:permission:role:delete', '4', 'xitongrizhi', 'system/data/permission/role/delete', '/gitegg-service-system/data/permission/role/delete/{dataPermissionRoleId}', 4, 1, NULL, 1, NULL, 1, 1, '删除数据权限', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (116, 0, 111, '0,1,2,3,111', '批量删除数据权限', 'system:data:permission:role:batch:delete', '4', 'xitongrizhi', 'system/data/permission/role/batch/delete', '/gitegg-service-system/data/permission/role/batch/delete', 5, 1, NULL, 1, NULL, 1, 1, '批量删除数据权限', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (117, 0, 105, '0,1,2,3,105', '检查多机构数据权限列表是否存在', 'system:data:permission:user:check', '4', 'xitongrizhi', 'system/data/permission/user/check', '/gitegg-service-system/data/permission/user/check', 6, 1, NULL, 1, NULL, 1, 1, '字段校验', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (118, 0, 111, '0,1,2,3,111', '检查数据权限是否存在', 'system:data:permission:role:check', '4', 'xitongrizhi', 'system/data/permission/role/check', '/gitegg-service-system/data/permission/role/check', 6, 1, NULL, 1, NULL, 1, 1, '字段校验', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (119, 0, 111, '0,1,2,3,111', '查询拥有数据权限的角色', 'system:data:permission:role:get:roles', '4', 'xitongrizhi', 'system/data/permission/role/get/roles', '/gitegg-service-system/data/permission/role/get/roles/{currentDataPermissionId}', 7, 1, NULL, 1, NULL, 1, 1, '查询拥有数据权限的角色', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (120, 0, 111, '0,1,2,3,111', '批量更新拥有数据权限的角色', 'system:data:permission:role:batch:role:update', '4', 'xitongrizhi', 'system/data/permission/role/batch/role/update', '/gitegg-service-system/data/permission/role/batch/role/update', 8, 1, NULL, 1, NULL, 1, 1, '批量更新拥有数据权限的角色', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (121, 0, 111, '0,1,2,3,111', '修改数据权限状态', 'extension:data:permission:role:status', '4', 'xitongrizhi', 'system/data/permission/role/status', '/gitegg-service-system/data/permission/role/status/{dpId}/{dpStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改数据权限状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (122, 0, 105, '0,1,2,3,105', '修改用户机构数据权限', 'system:user:update:organization:data:permission', '4', 'xitongguanli', 'system/user/update/organization/data/permission', '/gitegg-service-system/user/update/organization/data/permission', 12, 0, NULL, 1, '', 1, 1, '', '2021-05-14 17:46:32', 1, '2021-05-14 17:47:28', 1, 0);
INSERT INTO `t_sys_resource` VALUES (123, 0, 105, '0,1,2,3,105', '获取拥有机构权限的用户列表', 'system:user:organization:data:permission:list', '4', 'xitongrizhi', 'system/user/organization/data/permission/list', '/gitegg-service-system/user/organization/data/permission/list', 13, 0, NULL, 1, NULL, 1, 1, '获取拥有机构权限的用户列表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (124, 0, 105, '0,1,2,3,105', '批量删除用户机构权限接口', 'system:user:organization:data:permission:batch:delete', '4', 'xitongrizhi', 'system/user/organization/data/permission/batch/delete', '/gitegg-service-system/user/organization/data/permission/batch/delete', 14, 0, NULL, 1, NULL, 1, 1, '批量删除用户机构权限接口', '2018-10-27 17:40:14', 1, '2021-05-14 17:46:59', 1, 0);
INSERT INTO `t_sys_resource` VALUES (125, 0, 1, '0,1', '代码生成', 'plugin:code', '2', 'daimashengcheng', 'plugin/code/generator', 'Layout', 999, 1, NULL, 1, NULL, 1, 1, '代码生成', '2016-04-22 10:43:19', 1, '2021-09-30 18:06:28', 1, 0);
INSERT INTO `t_sys_resource` VALUES (126, 0, 125, '0,1,125', '代码定制', 'plugin:code:config', '2', 'daimadingzhi', 'config', 'redirect:plugin/code/generator/config/table', 1, 1, NULL, 1, 'generationTable', 1, 1, '代码配置', '2016-04-22 10:43:19', 1, '2022-12-07 00:21:57', 1, 0);
INSERT INTO `t_sys_resource` VALUES (127, 0, 126, '0,1,125,126', '查询代码配置列表', 'plugin:code:config:list', '4', 'quanxianguanli', 'list', '/gitegg-code-generator/code/generator/config/list', 1, 1, NULL, 1, NULL, 1, 1, '代码配置列表', '2016-04-22 10:43:19', 1, '2022-12-01 16:32:09', 1, 0);
INSERT INTO `t_sys_resource` VALUES (128, 0, 126, '0,1,125,126', '创建代码配置', 'plugin:code:config:create', '4', 'xitongrizhi', 'create', '/gitegg-code-generator/code/generator/config/create', 2, 1, NULL, 1, NULL, 1, 1, '添加代码配置', '2018-10-27 17:40:14', 1, '2021-11-17 11:23:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (129, 0, 126, '0,1,125,126', '更新代码配置', 'plugin:code:config:update', '4', 'xitongrizhi', 'update', '/gitegg-code-generator/code/generator/config/update', 3, 1, NULL, 1, NULL, 1, 1, '更新代码配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (130, 0, 126, '0,1,125,126', '删除代码配置', 'plugin:code:config:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/config/delete/{cfgId}', 4, 1, NULL, 1, NULL, 1, 1, '删除代码配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (131, 0, 126, '0,1,125,126', '批量删除代码配置', 'plugin:code:config:batch:delete', '4', 'xitongrizhi', 'batch/delete', '/gitegg-code-generator/code/generator/config/batch/delete', 5, 1, NULL, 1, NULL, 1, 1, '批量删除代码配置', '2018-10-27 17:40:14', 1, '2021-11-17 11:22:11', 1, 0);
INSERT INTO `t_sys_resource` VALUES (132, 0, 125, '0,1,125', '数据源配置', 'plugin:code:datasource', '2', 'shujuyuanpeizhi', 'datasource', 'plugin/codeGenerator/dataSource/DataSourceTable', 5, 1, NULL, 1, 'datasourceTable', 1, 1, '数据源配置', '2016-04-22 10:43:19', 1, '2022-12-12 12:49:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (133, 0, 132, '0,1,125,132', '数据源配置列表', 'plugin:code:datasource:list', '4', 'quanxianguanli', 'list', '/gitegg-code-generator/code/generator/datasource/list', 1, 1, NULL, 1, NULL, 1, 1, '数据源配置列表', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (134, 0, 132, '0,1,125,132', '创建数据源配置', 'plugin:code:datasource:create', '4', 'xitongrizhi', 'create', '/gitegg-code-generator/code/generator/datasource/create', 2, 1, NULL, 1, NULL, 1, 1, '添加数据源配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (135, 0, 132, '0,1,125,132', '更新数据源配置', 'plugin:code:datasource:update', '4', 'xitongrizhi', 'update', '/gitegg-code-generator/code/generator/datasource/update', 3, 1, NULL, 1, NULL, 1, 1, '更新数据源配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (136, 0, 132, '0,1,125,132', '删除数据源配置', 'plugin:code:datasource:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/datasource/delete/{cfgId}', 4, 1, NULL, 1, NULL, 1, 1, '删除数据源配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (137, 0, 132, '0,1,125,132', '批量删除数据源配置', 'plugin:code:datasource:batch:delete', '4', 'xitongrizhi', 'batch/delete', '/gitegg-code-generator/code/generator/datasource/batch/delete', 5, 1, NULL, 1, NULL, 1, 1, '批量删除数据源配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (138, 0, 132, '0,1,125,132', '检查数据源配置是否存在', 'plugin:code:datasource:check', '4', 'xitongrizhi', 'check', '/gitegg-code-generator/code/generator/datasource/check', 6, 1, NULL, 1, NULL, 1, 1, '校验数据源配置是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (139, 0, 132, '0,1,125,132', '下载数据源配置列表', 'plugin:code:datasource:download', '4', 'quanxianguanli', 'download', '/gitegg-code-generator/code/generator/datasource/download', 7, 1, NULL, 1, NULL, 1, 1, '数据源配置列表导出', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (140, 0, 132, '0,1,125,132', '下载数据源配置列表模板', 'plugin:code:datasource:export:template', '4', 'quanxianguanli', 'download/template', '/gitegg-code-generator/code/generator/datasource/download/template', 8, 1, NULL, 1, NULL, 1, 1, '下载数据源配置列表模板', '2016-04-22 10:43:19', 1, '2021-08-27 17:59:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (141, 0, 132, '0,1,125,132', '上传数据源配置', 'plugin:code:datasource:upload', '4', 'quanxianguanli', 'upload', '/gitegg-code-generator/code/generator/datasource/upload', 9, 1, NULL, 1, NULL, 1, 1, '上传数据源配置', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (142, 0, 126, '0,1,125,126', '代码配置表', 'plugin:code:config:table', '2', 'quanxianguanli', 'table', 'plugin/codeGenerator/config/ConfigTable', 1, 0, '/plugin/code/generator/config', 1, 'generationTable', 1, 1, '代码配置表', '2016-04-22 10:43:19', 1, '2021-09-07 19:11:57', 1, 0);
INSERT INTO `t_sys_resource` VALUES (143, 0, 126, '0,1,125,126', '代码配置规则', 'plugin:code:config:to:edit', '2', 'xitongrizhi', 'edit/:id(\\d+)', 'plugin/codeGenerator/config/ConfigEdit', 10, 0, '/plugin/code/generator/config', 0, 'ConfigEdit', 1, 1, '编辑代码配置', '2021-09-06 09:12:28', 1, '2021-10-11 14:09:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (145, 0, 9, '0,1,2,8,9', '批量查询数据字典', 'base:dict:batch:query', '4', 'system', 'base/dict/batch/query', '/gitegg-service-base/dict/batch/query', 10, 1, NULL, 1, '', 1, 1, '', '2021-09-14 11:27:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_resource` VALUES (146, 0, 9, '0,1,2,8,9', '查询数据字典', 'base:dict:query', '4', 'system', 'base/dict/query', '/gitegg-service-base/dict/query/{dictCode}', 10, 1, NULL, 1, '', 1, 1, '', '2021-09-14 11:27:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_resource` VALUES (147, 0, 125, '0,1,125', '联表配置', 'plugin:code:table', '2', 'quanxianguanli', 'table/join', 'plugin/codeGenerator/TableJoin', 2, 0, NULL, 1, 'tableJoin', 1, 1, '联表配置', '2016-04-22 10:43:19', 1, '2021-09-16 10:29:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (148, 0, 147, '0,1,125,147', '联表配置列表', 'plugin:code:table:list', '4', 'quanxianguanli', 'list', '/gitegg-code-generator/code/generator/table/join/list', 1, 1, NULL, 1, NULL, 1, 1, '联表配置列表', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (149, 0, 147, '0,1,125,147', '创建联表配置', 'plugin:code:table:create', '4', 'xitongrizhi', 'create', '/gitegg-code-generator/code/generator/table/join/create', 2, 1, NULL, 1, NULL, 1, 1, '创建联表配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (150, 0, 147, '0,1,125,147', '更新联表配置', 'plugin:code:table:update', '4', 'xitongrizhi', 'update', '/gitegg-code-generator/code/generator/table/join/update', 3, 1, NULL, 1, NULL, 1, 1, '更新联表配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (151, 0, 147, '0,1,125,147', '删除联表配置', 'plugin:code:table:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/table/join/delete/{cfgId}', 4, 1, NULL, 1, NULL, 1, 1, '删除联表配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (152, 0, 125, '0,1,125', '执行器配置', 'plugin:code:engine', '2', 'quanxianguanli', 'engine', 'plugin/codeGenerator/engine', 2, 0, NULL, 1, 'engine', 1, 1, '执行器配置', '2016-04-22 10:43:19', 1, '2021-09-16 10:29:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (153, 0, 152, '0,1,125,152', '查询所有表', 'plugin:code:engine:table:list', '4', 'quanxianguanli', 'table/list', '/gitegg-code-generator/code/generator/engine/table/list', 1, 1, NULL, 1, NULL, 1, 1, '查询数据表列表', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (154, 0, 152, '0,1,125,152', '查询表字段', 'plugin:code:engine:table:field:list', '4', 'quanxianguanli', 'table/field/list', '/gitegg-code-generator/code/generator/engine/table/field/list', 2, 1, NULL, 1, NULL, 1, 1, '查询数据表列表', '2016-04-22 10:43:19', 1, '2021-10-11 19:25:12', 1, 0);
INSERT INTO `t_sys_resource` VALUES (155, 0, 126, '0,1,125,126', '查询单个代码配置', 'plugin:code:config:query', '4', 'quanxianguanli', 'config/query', '/gitegg-code-generator/code/generator/config/query', 1, 1, NULL, 1, NULL, 1, 1, '查询单个代码配置', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (156, 0, 125, '0,1,125', '字段配置', 'plugin:code:field', '2', 'quanxianguanli', 'field', 'plugin/codeGenerator/TableField', 2, 0, NULL, 1, 'tableField', 1, 1, '字段配置', '2016-04-22 10:43:19', 1, '2021-09-16 10:29:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (157, 0, 156, '0,1,125,156', '创建字段配置', 'plugin:code:field:create', '4', 'xitongrizhi', 'create', '/gitegg-code-generator/code/generator/field/create', 2, 1, NULL, 1, NULL, 1, 1, '创建字段配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (158, 0, 156, '0,1,125,156', '更新字段配置', 'plugin:code:field:update', '4', 'xitongrizhi', 'update', '/gitegg-code-generator/code/generator/field/update', 3, 1, NULL, 1, NULL, 1, 1, '更新字段配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (159, 0, 156, '0,1,125,156', '删除字段配置', 'plugin:code:field:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/field/delete/{cfgId}', 4, 1, NULL, 1, NULL, 1, 1, '删除字段配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (160, 0, 156, '0,1,125,156', '字段配置列表', 'plugin:code:field:list', '4', 'quanxianguanli', 'list', '/gitegg-code-generator/code/generator/field/list', 1, 1, NULL, 1, NULL, 1, 1, '字段配置列表', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (161, 0, 156, '0,1,125,156', '所有字段配置列表', 'plugin:code:field:list:all', '4', 'quanxianguanli', 'list/all', '/gitegg-code-generator/code/generator/field/list/all', 1, 1, NULL, 1, NULL, 1, 1, '所有字段配置列表', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (162, 0, 156, '0,1,125,156', '编辑字段配置', 'plugin:code:field:edit', '4', 'xitongrizhi', 'edit', '/gitegg-code-generator/code/generator/field/edit', 2, 1, NULL, 1, NULL, 1, 1, '编辑字段配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (163, 0, 8, '0,1,2,8', '业务字典', 'system:config:dict:business', '2', 'shujuzidian', 'system/dict/business/table', 'system/base/dict/DictBusinessTable', 2, 1, NULL, 1, 'dictBusinessTable', 1, 1, '业务数据字典', '2018-10-27 17:53:49', 1, '2021-09-30 16:52:12', 1, 0);
INSERT INTO `t_sys_resource` VALUES (164, 0, 163, '0,1,2,8,163', '添加业务数据字典', 'base:dict:business:create', '4', 'xitongrizhi', 'base/dict/business/create', '/gitegg-service-base/business/dict/create', 2, 1, NULL, 1, NULL, 1, 1, '添加业务数据字典', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (165, 0, 163, '0,1,2,8,163', '更新业务数据字典', 'base:dict:business:update', '4', 'xitongrizhi', 'base/dict/business/update', '/gitegg-service-base/business/dict/update', 2, 1, NULL, 1, NULL, 1, 1, '更新业务数据字典', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (166, 0, 163, '0,1,2,8,163', '删除业务数据字典', 'base:dict:business:delete', '4', 'xitongrizhi', 'base/dict/business/delete', '/gitegg-service-base/business/dict/delete/{dictId}', 2, 1, NULL, 1, NULL, 1, 1, '删除业务数据字典', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (167, 0, 163, '0,1,2,8,163', '修改业务数据字典状态', 'base:dict:business:status', '4', 'xitongrizhi', 'base/dict/business/status', '/gitegg-service-base/business/dict/status/{dictId}/{dictStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改业务数据字典状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (168, 0, 163, '0,1,2,8,163', '查询业务数据字典树', 'base:dict:business:tree', '4', 'xitongrizhi', 'base/dict/business/tree', '/gitegg-service-base/business/dict/list', 2, 1, NULL, 1, NULL, 1, 1, '查询业务数据字典树', '2018-10-27 17:40:14', 1, '2021-01-04 18:20:56', 1, 0);
INSERT INTO `t_sys_resource` VALUES (169, 0, 163, '0,1,2,8,163', '校验业务数据字典是否存在', 'base:dict:business:check', '4', 'xitongrizhi', 'base/dict/business/check', '/gitegg-service-base/business/dict/check', 2, 1, NULL, 1, NULL, 1, 1, '校验业务数据字典是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (170, 0, 125, '0,1,125', '基础数据', 'plugin:code:dict', '2', 'jichushuju', 'dict', 'plugin/codeGenerator/dict/GeneratorDictTable', 2, 1, NULL, 1, 'generatorDictTable', 1, 1, '代码基础配置', '2018-10-27 17:53:49', 1, '2022-12-07 00:22:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (171, 0, 170, '0,1,125,170', '添加代码基础配置', 'plugin:code:dict:create', '4', 'xitongrizhi', 'create', '/gitegg-code-generator/code/generator/dict/create', 2, 1, NULL, 1, NULL, 1, 1, '添加代码基础配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (172, 0, 170, '0,1,125,170', '更新代码基础配置', 'plugin:code:dict:update', '4', 'xitongrizhi', 'update', '/gitegg-code-generator/code/generator/dict/update', 2, 1, NULL, 1, NULL, 1, 1, '更新代码基础配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (173, 0, 170, '0,1,125,170', '删除代码基础配置', 'plugin:code:dict:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/dict/delete/{dictId}', 2, 1, NULL, 1, NULL, 1, 1, '删除代码基础配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (174, 0, 170, '0,1,125,170', '修改代码基础配置状态', 'plugin:code:dict:status', '4', 'xitongrizhi', 'status', '/gitegg-code-generator/code/generator/dict/status/{dictId}/{dictStatus}', 2, 1, NULL, 1, NULL, 1, 1, '修改代码基础配置状态', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (175, 0, 170, '0,1,125,170', '查询代码基础配置树', 'plugin:code:dict:tree', '4', 'xitongrizhi', 'tree', '/gitegg-code-generator/code/generator/dict/list', 2, 1, NULL, 1, NULL, 1, 1, '查询代码基础配置树', '2018-10-27 17:40:14', 1, '2021-01-04 18:20:56', 1, 0);
INSERT INTO `t_sys_resource` VALUES (176, 0, 170, '0,1,125,170', '校验代码基础配置是否存在', 'plugin:code:dict:check', '4', 'xitongrizhi', 'check', '/gitegg-code-generator/code/generator/dict/check', 2, 1, NULL, 1, NULL, 1, 1, '校验代码基础配置是否存在', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (177, 0, 163, '0,1,2,8,163', '批量查询业务数据字典', 'base:dict:business:batch:query', '4', 'system', 'base/dict/business/batch/query', '/gitegg-service-base/business/dict/batch/query', 10, 1, NULL, 1, '', 1, 1, '批量查询业务数据字典', '2021-09-14 11:27:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_resource` VALUES (178, 0, 163, '0,1,2,8,163', '查询业务数据字典', 'base:dict:business:query', '4', 'system', 'base/dict/business/query', '/gitegg-service-base/business/dict/query/{dictCode}', 10, 1, NULL, 1, '', 1, 1, '查询业务数据字典', '2021-09-14 11:27:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_resource` VALUES (179, 0, 170, '0,1,125,170', '批量查询代码基础配置', 'plugin:code:dict:query', '4', 'system', 'plugin/code/generator/dict/batch/query', '/gitegg-code-generator/code/generator/dict/batch/query', 10, 1, NULL, 1, '', 1, 1, '批量查询代码基础配置', '2021-09-14 11:27:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_resource` VALUES (180, 0, 170, '0,1,125,170', '查询代码基础配置', 'plugin:code:dict:query', '4', 'system', 'plugin/code/generator/dict/query', '/gitegg-code-generator/code/generator/dict/query/{dictCode}', 10, 1, NULL, 1, '', 1, 1, '查询代码基础配置', '2021-09-14 11:27:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_resource` VALUES (181, 0, 163, '0,1,2,8,163', '查询所有业务数据字典树', 'base:dict:business:list:all', '4', 'xitongrizhi', 'base/dict/business/list/all', '/gitegg-service-base/business/dict/list/all', 2, 1, NULL, 1, NULL, 1, 1, '查询所有业务数据字典树', '2018-10-27 17:40:14', 1, '2021-01-04 18:20:56', 1, 0);
INSERT INTO `t_sys_resource` VALUES (182, 0, 126, '0,1,125,126', '复制代码配置', 'plugin:code:config:copy', '4', 'quanxianguanli', 'plugin/code/generator/config/copy', '/gitegg-code-generator/code/generator/config/copy', 6, 1, NULL, 1, NULL, 1, 1, '复制代码配置', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (183, 0, 152, '0,1,125,152', '执行代码生成', 'plugin:code:engine:process:generate:code', '4', 'quanxianguanli', 'plugin/code/generator/engine/process/generate/code', '/gitegg-code-generator/code/generator/engine/process/generate/code', 3, 1, NULL, 1, NULL, 1, 1, '执行代码生成', '2016-04-22 10:43:19', 1, '2021-10-11 19:25:17', 1, 0);
INSERT INTO `t_sys_resource` VALUES (184, 0, 125, '0,1,125', '校验规则', 'code:generator:validate:table', '2', 'jiaoyanguize', 'validate/table', 'plugin/codeGenerator/validate/ValidateTable', 3, 1, NULL, 1, 'validateTable', 1, 1, '字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (185, 0, 184, '0,1,125,184', '获取字段校验规则配置表列表', 'code:generator:validate:list', '4', 'xitongrizhi', 'list', '/gitegg-code-generator/code/generator/validate/list', 2, 1, NULL, 1, NULL, 1, 1, '获取字段校验规则配置表列表数据', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (186, 0, 184, '0,1,125,184', '添加字段校验规则配置表', 'code:generator:validate:create', '4', 'xitongrizhi', 'create', '/gitegg-code-generator/code/generator/validate/create', 2, 1, NULL, 1, NULL, 1, 1, '添加字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (187, 0, 184, '0,1,125,184', '更新字段校验规则配置表', 'code:generator:validate:update', '4', 'xitongrizhi', 'update', '/gitegg-code-generator/code/generator/validate/update', 2, 1, NULL, 1, NULL, 1, 1, '更新字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (188, 0, 184, '0,1,125,184', '删除字段校验规则配置表', 'code:generator:validate:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/validate/delete/{validateId}', 2, 1, NULL, 1, NULL, 1, 1, '删除字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (189, 0, 184, '0,1,125,184', '批量删除字段校验规则配置表', 'code:generator:validate:batch:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/validate/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (190, 0, 184, '0,1,125,184', '字段校验规则配置表状态修改', 'code:generator:validate:status', '4', 'xitongrizhi', 'status', '/gitegg-code-generator/code/generator/validate/status/{validateId}/{validateStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (191, 0, 184, '0,1,125,184', '字段校验规则配置表字段校验是否存在', 'code:generator:validate:check', '4', 'xitongrizhi', 'check', '/gitegg-code-generator/code/generator/validate/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (192, 0, 184, '0,1,125,184', '字段校验规则配置表数据导出', 'code:generator:validate:download', '4', 'xitongrizhi', 'download', '/gitegg-code-generator/code/generator/validate/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (193, 0, 184, '0,1,125,184', '字段校验规则配置表数据导入模板下载', 'code:generator:validate:download:template', '4', 'xitongrizhi', 'download/template', '/gitegg-code-generator/code/generator/validate/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (194, 0, 184, '0,1,125,184', '字段校验规则配置表数据导入', 'code:generator:validate:upload', '4', 'xitongrizhi', 'validate/upload', '/gitegg-code-generator/code/generator/validate/upload', 2, 1, NULL, 1, NULL, 1, 1, '数据导入字段校验规则配置表', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (195, 0, 184, '0,1,125,184', '获取所有字段校验规则配置表列表', 'code:generator:validate:list:all', '4', 'xitongrizhi', 'validate/list/all', '/gitegg-code-generator/code/generator/validate/list/all', 2, 1, NULL, 1, NULL, 1, 1, '获取所有字段校验规则配置表列表数据', '2021-10-15 11:55:03', 1, '2021-10-15 11:55:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (196, 0, 207, '0,1,2,207', '第三方配置', 'gitegg:service:extension:extension:justauth:table', '2', 'disanfangpeizhi', 'gitegg/service/extension/extension/justauth/config/table', 'system/extension/justauth/JustauthConfigTable.vue', 1, 1, NULL, 1, 'justAuthConfigTable', 1, 1, '租户第三方登录功能配置表', '2022-05-16 14:05:34', 1, '2022-05-19 18:56:18', 1, 0);
INSERT INTO `t_sys_resource` VALUES (197, 0, 196, '0,1,2,207,196', '获取租户第三方登录功能配置表列表', 'gitegg:service:extension:extension:justauth:list', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/list', '/gitegg-service-extension/extension/justauth/config/list', 2, 1, NULL, 1, NULL, 1, 1, '获取租户第三方登录功能配置表列表数据', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (198, 0, 196, '0,1,2,207,196', '添加租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:create', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/create', '/gitegg-service-extension/extension/justauth/config/create', 2, 1, NULL, 1, NULL, 1, 1, '添加租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (199, 0, 196, '0,1,2,207,196', '更新租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:update', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/update', '/gitegg-service-extension/extension/justauth/config/update', 2, 1, NULL, 1, NULL, 1, 1, '更新租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (200, 0, 196, '0,1,2,207,196', '删除租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/delete', '/gitegg-service-extension/extension/justauth/config/delete/{justAuthConfigId}', 2, 1, NULL, 1, NULL, 1, 1, '删除租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (201, 0, 196, '0,1,2,207,196', '批量删除租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/batch/delete', '/gitegg-service-extension/extension/justauth/config/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (202, 0, 196, '0,1,2,207,196', '租户第三方登录功能配置表状态修改', 'gitegg:service:extension:extension:justauth:status', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/status', '/gitegg-service-extension/extension/justauth/config/status/{justAuthConfigId}/{justAuthConfigStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (203, 0, 196, '0,1,2,207,196', '租户第三方登录功能配置表字段校验是否存在', 'gitegg:service:extension:extension:justauth:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/check', '/gitegg-service-extension/extension/justauth/config/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (204, 0, 196, '0,1,2,207,196', '租户第三方登录功能配置表数据导出', 'gitegg:service:extension:extension:justauth:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/download', '/gitegg-service-extension/extension/justauth/config/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (205, 0, 196, '0,1,2,207,196', '租户第三方登录功能配置表数据导入模板下载', 'gitegg:service:extension:extension:justauth:download:template', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/download/template', '/gitegg-service-extension/extension/justauth/config/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (206, 0, 196, '0,1,2,207,196', '租户第三方登录功能配置表数据导入', 'gitegg:service:extension:extension:justauth:upload', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/config/upload', '/gitegg-service-extension/extension/justauth/config/upload', 2, 1, NULL, 1, NULL, 1, 1, '数据导入租户第三方登录功能配置表', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (207, 0, 2, '0,1,2', '第三方登录', 'social:login', '2', 'disanfang', 'social', 'nested', 6, 1, NULL, 1, 'social', 1, 1, '', '2022-05-16 14:12:52', 1, '2022-06-24 16:11:56', 1, 0);
INSERT INTO `t_sys_resource` VALUES (208, 0, 207, '0,1,2,207', '第三方列表', 'gitegg:service:extension:extension:justauth:source:table', '2', 'disanfangliebiao', 'gitegg/service/extension/extension/justauth/source/table', 'system/extension/justauth/JustauthSourceTable.vue', 2, 1, NULL, 1, 'justAuthSourceTable', 1, 1, '租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:22:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (209, 0, 208, '0,1,2,207,208', '获取租户第三方登录信息配置表列表', 'gitegg:service:extension:extension:justauth:source:list', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/list', '/gitegg-service-extension/extension/justauth/source/list', 2, 1, NULL, 1, NULL, 1, 1, '获取租户第三方登录信息配置表列表数据', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (210, 0, 208, '0,1,2,207,208', '添加租户第三方登录信息配置表', 'gitegg:service:extension:extension:justauth:source:create', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/create', '/gitegg-service-extension/extension/justauth/source/create', 2, 1, NULL, 1, NULL, 1, 1, '添加租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (211, 0, 208, '0,1,2,207,208', '更新租户第三方登录信息配置表', 'gitegg:service:extension:extension:justauth:source:update', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/update', '/gitegg-service-extension/extension/justauth/source/update', 2, 1, NULL, 1, NULL, 1, 1, '更新租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (212, 0, 208, '0,1,2,207,208', '删除租户第三方登录信息配置表', 'gitegg:service:extension:extension:justauth:source:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/delete', '/gitegg-service-extension/extension/justauth/source/delete/{justAuthSourceId}', 2, 1, NULL, 1, NULL, 1, 1, '删除租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (213, 0, 208, '0,1,2,207,208', '批量删除租户第三方登录信息配置表', 'gitegg:service:extension:extension:justauth:source:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/batch/delete', '/gitegg-service-extension/extension/justauth/source/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (214, 0, 208, '0,1,2,207,208', '租户第三方登录信息配置表状态修改', 'gitegg:service:extension:extension:justauth:source:status', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/status', '/gitegg-service-extension/extension/justauth/source/status/{justAuthSourceId}/{justAuthSourceStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (215, 0, 208, '0,1,2,207,208', '租户第三方登录信息配置表字段校验是否存在', 'gitegg:service:extension:extension:justauth:source:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/check', '/gitegg-service-extension/extension/justauth/source/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (216, 0, 208, '0,1,2,207,208', '租户第三方登录信息配置表数据导出', 'gitegg:service:extension:extension:justauth:source:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/download', '/gitegg-service-extension/extension/justauth/source/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (217, 0, 208, '0,1,2,207,208', '租户第三方登录信息配置表数据导入模板下载', 'gitegg:service:extension:extension:justauth:source:download:template', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/download/template', '/gitegg-service-extension/extension/justauth/source/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (218, 0, 208, '0,1,2,207,208', '租户第三方登录信息配置表数据导入', 'gitegg:service:extension:extension:justauth:source:upload', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/source/upload', '/gitegg-service-extension/extension/justauth/source/upload', 2, 1, NULL, 1, NULL, 1, 1, '数据导入租户第三方登录信息配置表', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (219, 0, 207, '0,1,2,207', '第三方用户', 'gitegg:service:extension:extension:justauth:social:table', '2', 'disanfangyonghu', 'gitegg/service/extension/extension/justauth/social/table', 'system/extension/justauth/JustauthSocialTable.vue', 3, 1, NULL, 1, 'justAuthSocialTable', 1, 1, '租户第三方登录功能配置表', '2022-05-19 18:45:55', 1, '2022-05-19 18:56:25', 1, 0);
INSERT INTO `t_sys_resource` VALUES (220, 0, 219, '0,1,2,207,219', '获取租户第三方登录功能配置表列表', 'gitegg:service:extension:extension:justauth:social:list', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/list', '/gitegg-service-extension/extension/justauth/social/list', 2, 1, NULL, 1, NULL, 1, 1, '获取租户第三方登录功能配置表列表数据', '2022-05-19 18:45:55', 1, '2022-05-19 18:45:55', 1, 0);
INSERT INTO `t_sys_resource` VALUES (221, 0, 219, '0,1,2,207,219', '添加租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:create', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/create', '/gitegg-service-extension/extension/justauth/social/create', 2, 1, NULL, 1, NULL, 1, 1, '添加租户第三方登录功能配置表', '2022-05-19 18:45:55', 1, '2022-05-19 18:45:55', 1, 0);
INSERT INTO `t_sys_resource` VALUES (222, 0, 219, '0,1,2,207,219', '更新租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:update', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/update', '/gitegg-service-extension/extension/justauth/social/update', 2, 1, NULL, 1, NULL, 1, 1, '更新租户第三方登录功能配置表', '2022-05-19 18:45:55', 1, '2022-05-19 18:45:55', 1, 0);
INSERT INTO `t_sys_resource` VALUES (223, 0, 219, '0,1,2,207,219', '删除租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/delete', '/gitegg-service-extension/extension/justauth/social/delete/{justAuthSocialId}', 2, 1, NULL, 1, NULL, 1, 1, '删除租户第三方登录功能配置表', '2022-05-19 18:45:55', 1, '2022-05-19 18:45:55', 1, 0);
INSERT INTO `t_sys_resource` VALUES (224, 0, 219, '0,1,2,207,219', '批量删除租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/batch/delete', '/gitegg-service-extension/extension/justauth/social/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除租户第三方登录功能配置表', '2022-05-19 18:45:55', 1, '2022-05-19 18:45:55', 1, 0);
INSERT INTO `t_sys_resource` VALUES (225, 0, 219, '0,1,2,207,219', '租户第三方登录功能配置表字段校验是否存在', 'gitegg:service:extension:extension:justauth:social:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/check', '/gitegg-service-extension/extension/justauth/social/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在租户第三方登录功能配置表', '2022-05-19 18:45:55', 1, '2022-05-19 18:45:55', 1, 0);
INSERT INTO `t_sys_resource` VALUES (226, 0, 219, '0,1,2,207,219', '租户第三方登录功能配置表数据导出', 'gitegg:service:extension:extension:justauth:social:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/download', '/gitegg-service-extension/extension/justauth/social/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出租户第三方登录功能配置表', '2022-05-19 18:45:55', 1, '2022-05-19 18:45:55', 1, 0);
INSERT INTO `t_sys_resource` VALUES (227, 0, 207, '0,1,2,207', '用户关联', 'gitegg:service:extension:extension:justauth:social:user:table', '2', 'jiaoseguanli', 'gitegg/service/extension/extension/justauth/social/user/table', 'gitegg/service/extension/extension/justauth/social/user/table', 4, 0, NULL, 1, 'justAuthSocialUserTable', 1, 1, '租户第三方登录功能配置表', '2022-05-19 19:14:16', 1, '2022-05-19 19:15:44', 1, 0);
INSERT INTO `t_sys_resource` VALUES (228, 0, 227, '0,1,2,207,227', '获取租户第三方登录功能配置表列表', 'gitegg:service:extension:extension:justauth:social:user:list', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/user/list', '/gitegg-service-extension/extension/justauth/social/user/list', 2, 1, NULL, 1, NULL, 1, 1, '获取租户第三方登录功能配置表列表数据', '2022-05-19 19:14:16', 1, '2022-05-19 19:14:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (229, 0, 227, '0,1,2,207,227', '添加租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:user:create', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/user/create', '/gitegg-service-extension/extension/justauth/social/user/create', 2, 1, NULL, 1, NULL, 1, 1, '添加租户第三方登录功能配置表', '2022-05-19 19:14:16', 1, '2022-05-19 19:14:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (230, 0, 227, '0,1,2,207,227', '更新租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:user:update', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/user/update', '/gitegg-service-extension/extension/justauth/social/user/update', 2, 1, NULL, 1, NULL, 1, 1, '更新租户第三方登录功能配置表', '2022-05-19 19:14:16', 1, '2022-05-19 19:14:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (231, 0, 227, '0,1,2,207,227', '删除租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:user:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/user/delete', '/gitegg-service-extension/extension/justauth/social/user/delete/{justAuthSocialUserId}', 2, 1, NULL, 1, NULL, 1, 1, '删除租户第三方登录功能配置表', '2022-05-19 19:14:16', 1, '2022-05-19 19:14:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (232, 0, 227, '0,1,2,207,227', '批量删除租户第三方登录功能配置表', 'gitegg:service:extension:extension:justauth:social:user:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/user/batch/delete', '/gitegg-service-extension/extension/justauth/social/user/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除租户第三方登录功能配置表', '2022-05-19 19:14:16', 1, '2022-05-19 19:14:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (233, 0, 227, '0,1,2,207,227', '租户第三方登录功能配置表字段校验是否存在', 'gitegg:service:extension:extension:justauth:social:user:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/justauth/social/user/check', '/gitegg-service-extension/extension/justauth/social/user/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在租户第三方登录功能配置表', '2022-05-19 19:14:16', 1, '2022-05-19 19:14:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (234, 0, 76, '0,1,2,70,76', '批量删除短信渠道表', 'gitegg:service:extension:extension:sms:channel:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/channel/batch/delete', '/gitegg-service-extension/extension/sms/channel/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除短信渠道表', '2022-05-24 16:47:03', 1, '2022-05-24 16:47:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (235, 0, 76, '0,1,2,70,76', '短信渠道表状态修改', 'gitegg:service:extension:extension:sms:channel:status', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/channel/status', '/gitegg-service-extension/extension/sms/channel/status/{smsChannelId}/{smsChannelStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除短信渠道表', '2022-05-24 16:47:03', 1, '2022-05-24 16:47:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (236, 0, 76, '0,1,2,70,76', '短信渠道表字段校验是否存在', 'gitegg:service:extension:extension:sms:channel:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/channel/check', '/gitegg-service-extension/extension/sms/channel/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在短信渠道表', '2022-05-24 16:47:03', 1, '2022-05-24 16:47:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (237, 0, 76, '0,1,2,70,76', '短信渠道表数据导出', 'gitegg:service:extension:extension:sms:channel:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/channel/download', '/gitegg-service-extension/extension/sms/channel/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出短信渠道表', '2022-05-24 16:47:03', 1, '2022-05-24 16:47:03', 1, 0);
INSERT INTO `t_sys_resource` VALUES (238, 0, 81, '0,1,2,70,81', '批量删除短信配置表', 'gitegg:service:extension:extension:sms:template:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/template/batch/delete', '/gitegg-service-extension/extension/sms/template/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除短信配置表', '2022-05-24 18:42:19', 1, '2022-05-24 18:42:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (239, 0, 81, '0,1,2,70,81', '短信配置表状态修改', 'gitegg:service:extension:extension:sms:template:status', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/template/status', '/gitegg-service-extension/extension/sms/template/status/{smsTemplateId}/{smsTemplateStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除短信配置表', '2022-05-24 18:42:19', 1, '2022-05-24 18:42:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (240, 0, 81, '0,1,2,70,81', '短信配置表字段校验是否存在', 'gitegg:service:extension:extension:sms:template:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/template/check', '/gitegg-service-extension/extension/sms/template/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在短信配置表', '2022-05-24 18:42:19', 1, '2022-05-24 18:42:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (241, 0, 81, '0,1,2,70,81', '短信配置表数据导出', 'gitegg:service:extension:extension:sms:template:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/template/download', '/gitegg-service-extension/extension/sms/template/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出短信配置表', '2022-05-24 18:42:19', 1, '2022-05-24 18:42:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (242, 0, 81, '0,1,2,70,81', '短信配置表数据导入模板下载', 'gitegg:service:extension:extension:sms:template:download:template', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/template/download/template', '/gitegg-service-extension/extension/sms/template/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载短信配置表', '2022-05-24 18:42:19', 1, '2022-05-24 18:42:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (243, 0, 81, '0,1,2,70,81', '短信配置表数据导入', 'gitegg:service:extension:extension:sms:template:upload', '4', 'xitongrizhi', 'gitegg/service/extension/extension/sms/template/upload', '/gitegg-service-extension/extension/sms/template/upload', 2, 1, NULL, 1, NULL, 1, 1, '数据导入短信配置表', '2022-05-24 18:42:19', 1, '2022-05-24 18:42:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (244, 0, 76, '0,1,2,70,76', '获取所有短信渠道列表', 'extension:sms:channel:list:all', '4', 'xitongrizhi', 'extension/sms/channel/list/all', '/gitegg-service-extension/extension/sms/channel/list/all', 2, 1, NULL, 1, NULL, 1, 1, '获取所有短信渠道列表数据', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (245, 0, 1, '0,1', '协同办公', 'plugin:workflow:oa', '2', 'xietongbangong', 'plugin/workflow/oa', 'Layout', 2, 1, NULL, 1, NULL, 1, 1, '协同办公', '2016-04-22 10:43:19', 1, '2022-06-16 18:00:17', 1, 0);
INSERT INTO `t_sys_resource` VALUES (246, 0, 1, '0,1', '流程管理', 'plugin:workflow:manager', '2', 'gongzuoliu', 'plugin/workflow/manager', 'Layout', 998, 1, NULL, 1, NULL, 1, 1, '流程管理', '2016-04-22 10:43:19', 1, '2022-06-16 18:00:12', 1, 0);
INSERT INTO `t_sys_resource` VALUES (247, 0, 245, '0,1,245', '发起申请', 'plugin:workflow:oa:task:new:my', '2', 'faqishenqing', 'plugin/workflow/oa/task/my/new', 'plugin/workflow/oa/task/my/new', 1, 1, NULL, 1, 'myTaskNew', 1, 1, '发起新任务', '2016-04-22 10:43:19', 1, '2021-10-25 19:33:36', 1, 0);
INSERT INTO `t_sys_resource` VALUES (248, 0, 245, '0,1,245', '待处理的', 'plugin:workflow:oa:task:todo', '2', 'daichuli', 'plugin/workflow/oa/task/todo', 'plugin/workflow/oa/task/todo', 2, 1, NULL, 1, 'todoTask', 1, 1, '待处理的任务', '2016-04-22 10:43:19', 1, '2021-10-25 19:33:36', 1, 0);
INSERT INTO `t_sys_resource` VALUES (249, 0, 245, '0,1,245', '我发起的', 'plugin:workflow:oa:task:my', '2', 'wofaqide', 'plugin/workflow/oa/task/my', 'plugin/workflow/oa/task/my', 3, 1, NULL, 1, 'myTask', 1, 1, '我发起的任务', '2016-04-22 10:43:19', 1, '2021-10-25 19:33:36', 1, 0);
INSERT INTO `t_sys_resource` VALUES (250, 0, 245, '0,1,245', '已处理的', 'plugin:workflow:oa:task:todo:my', '2', 'yichuli', 'plugin/workflow/oa/task/todo/my', 'plugin/workflow/oa/task/todo/my', 4, 1, NULL, 1, 'myTodoTask', 1, 1, '我收到的任务', '2016-04-22 10:43:19', 1, '2021-10-25 19:33:36', 1, 0);
INSERT INTO `t_sys_resource` VALUES (251, 0, 246, '0,1,246', '流程设计', 'plugin:workflow:manager:definition', '2', 'quanxianguanli', 'plugin/workflow/manager/definition/list', 'plugin/workflow/manager/definitionTable', 1, 0, NULL, 1, 'processDefinition', 1, 1, '流程设计', '2016-04-22 10:43:19', 1, '2022-07-10 12:30:05', 1, 0);
INSERT INTO `t_sys_resource` VALUES (252, 0, 246, '0,1,246', '流程模型', 'plugin:workflow:manager:bpmn', '2', 'quanxianguanli', 'plugin/workflow/manager/bpmn', 'plugin/workflow/manager/bpmnTable', 2, 0, NULL, 1, 'processDesign', 1, 1, '流程模型转换', '2016-04-22 10:43:19', 1, '2022-07-10 12:29:44', 1, 0);
INSERT INTO `t_sys_resource` VALUES (253, 0, 247, '0,1,245,247', '我的申请列表', 'plugin:workflow:oa:task:new:my:list', '4', 'quanxianguanli', 'plugin/workflow/oa/task/my/new/list', '/gitegg-plugin-workflow/workflow/oa/task/my/new/list', 1, 1, NULL, 1, '', 1, 1, '我的申请列表', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (254, 0, 246, '0,1,246', '流程设计器', 'plugin:workflow:manager:design:modeler', '2', 'quanxianguanli', 'plugin/workflow/manager/design/modeler', 'plugin/workflow/manager/modeler/Index', 1, 0, NULL, 1, 'processDesignModeler', 1, 1, '流程设计器', '2016-04-22 10:43:19', 1, '2022-06-17 15:03:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (255, 0, 251, '0,1,246,251', '基础配置', 'plugin:workflow:manager:design:modeler:base', '2', 'quanxianguanli', 'plugin/workflow/manager/design/modeler/base', 'plugin/workflow/manager/modeler/settings/baseSetting', 1, 0, NULL, 1, 'processDesignBase', 1, 1, '基础配置', '2016-04-22 10:43:19', 1, '2022-06-17 14:55:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (256, 0, 298, '0,1,246,298', '表单设计', 'plugin:workflow:manager:process:model:form', '2', 'quanxianguanli', 'plugin/workflow/manager/design/modeler/form', 'plugin/workflow/manager/modeler/settings/formSetting', 4, 0, NULL, 1, 'ModelDesignForm', 1, 1, '表单配置', '2016-04-22 10:43:19', 1, '2022-07-13 15:12:24', 1, 0);
INSERT INTO `t_sys_resource` VALUES (257, 0, 298, '0,1,246,298', '模型设计', 'plugin:workflow:manager:process:model:design', '2', 'quanxianguanli', 'plugin/workflow/manager/design/modeler/process', 'plugin/workflow/manager/modeler/settings/processSetting', 5, 0, NULL, 1, 'ModelDesignProcess', 1, 1, '流程配置', '2016-04-22 10:43:19', 1, '2022-07-13 15:12:31', 1, 0);
INSERT INTO `t_sys_resource` VALUES (258, 0, 251, '0,1,246,251', '高级设计', 'plugin:workflow:manager:design:modeler:pro', '2', 'quanxianguanli', 'plugin/workflow/manager/design/modeler/pro', 'plugin/workflow/manager/modeler/settings/proSetting', 1, 0, NULL, 1, 'processDesignPro', 1, 1, '高级配置', '2016-04-22 10:43:19', 1, '2022-06-17 14:55:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (259, 0, 270, '0,1,2,270', '邮件渠道', 'gitegg:service:extension:extension:mail:channel:table', '2', 'youjianqudao', 'gitegg/service/extension/extension/mail/channel/table', 'system/extension/mail/MailChannelTable.vue', 1, 1, NULL, 1, 'mailChannelTable', 1, 1, '邮件渠道表', '2022-06-24 15:58:39', 1, '2022-07-05 23:37:19', 1, 0);
INSERT INTO `t_sys_resource` VALUES (260, 0, 259, '0,1,2,270,259', '获取邮件渠道表列表', 'gitegg:service:extension:extension:mail:channel:list', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/list', '/gitegg-service-extension/extension/mail/channel/list', 2, 1, NULL, 1, NULL, 1, 1, '获取邮件渠道表列表数据', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (261, 0, 259, '0,1,2,270,259', '添加邮件渠道表', 'gitegg:service:extension:extension:mail:channel:create', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/create', '/gitegg-service-extension/extension/mail/channel/create', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (262, 0, 259, '0,1,2,270,259', '更新邮件渠道表', 'gitegg:service:extension:extension:mail:channel:update', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/update', '/gitegg-service-extension/extension/mail/channel/update', 2, 1, NULL, 1, NULL, 1, 1, '更新邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (263, 0, 259, '0,1,2,270,259', '删除邮件渠道表', 'gitegg:service:extension:extension:mail:channel:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/delete', '/gitegg-service-extension/extension/mail/channel/delete/{mailChannelId}', 2, 1, NULL, 1, NULL, 1, 1, '删除邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (264, 0, 259, '0,1,2,270,259', '批量删除邮件渠道表', 'gitegg:service:extension:extension:mail:channel:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/batch/delete', '/gitegg-service-extension/extension/mail/channel/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (265, 0, 259, '0,1,2,270,259', '邮件渠道表状态修改', 'gitegg:service:extension:extension:mail:channel:status', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/status', '/gitegg-service-extension/extension/mail/channel/status/{mailChannelId}/{mailChannelStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (266, 0, 259, '0,1,2,270,259', '邮件渠道表字段校验是否存在', 'gitegg:service:extension:extension:mail:channel:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/check', '/gitegg-service-extension/extension/mail/channel/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (267, 0, 259, '0,1,2,270,259', '邮件渠道表数据导出', 'gitegg:service:extension:extension:mail:channel:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/download', '/gitegg-service-extension/extension/mail/channel/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (268, 0, 259, '0,1,2,270,259', '邮件渠道表数据导入模板下载', 'gitegg:service:extension:extension:mail:channel:download:template', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/download/template', '/gitegg-service-extension/extension/mail/channel/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (269, 0, 259, '0,1,2,270,259', '邮件渠道表数据导入', 'gitegg:service:extension:extension:mail:channel:upload', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/channel/upload', '/gitegg-service-extension/extension/mail/channel/upload', 2, 1, NULL, 1, NULL, 1, 1, '数据导入邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (270, 0, 2, '0,1,2', '邮件配置', 'extension:mail:table', '2', 'youjianpeizhi', 'extension/mail/table', 'nested', 5, 1, NULL, 1, 'mailTable', 1, 1, '邮箱配置', '2016-04-22 10:43:19', 1, '2022-07-05 23:40:42', 1, 0);
INSERT INTO `t_sys_resource` VALUES (271, 0, 270, '0,1,2,270', '邮件模板', 'gitegg:service:extension:extension:mail:template:table', '2', 'youjianmuban', 'gitegg/service/extension/extension/mail/template/table', 'system/extension/mail/MailTemplateTable.vue', 2, 1, NULL, 1, 'mailTemplateTable', 1, 1, '邮件模板', '2022-06-24 17:32:23', 1, '2022-07-05 23:37:30', 1, 0);
INSERT INTO `t_sys_resource` VALUES (272, 0, 271, '0,1,2,270,271', '获取邮件模板列表', 'gitegg:service:extension:extension:mail:template:list', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/list', '/gitegg-service-extension/extension/mail/template/list', 2, 1, NULL, 1, NULL, 1, 1, '获取邮件模板列表数据', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (273, 0, 271, '0,1,2,270,271', '添加邮件模板', 'gitegg:service:extension:extension:mail:template:create', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/create', '/gitegg-service-extension/extension/mail/template/create', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (274, 0, 271, '0,1,2,270,271', '更新邮件模板', 'gitegg:service:extension:extension:mail:template:update', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/update', '/gitegg-service-extension/extension/mail/template/update', 2, 1, NULL, 1, NULL, 1, 1, '更新邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (275, 0, 271, '0,1,2,270,271', '删除邮件模板', 'gitegg:service:extension:extension:mail:template:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/delete', '/gitegg-service-extension/extension/mail/template/delete/{mailTemplateId}', 2, 1, NULL, 1, NULL, 1, 1, '删除邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (276, 0, 271, '0,1,2,270,271', '批量删除邮件模板', 'gitegg:service:extension:extension:mail:template:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/batch/delete', '/gitegg-service-extension/extension/mail/template/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (277, 0, 271, '0,1,2,270,271', '邮件模板状态修改', 'gitegg:service:extension:extension:mail:template:status', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/status', '/gitegg-service-extension/extension/mail/template/status/{mailTemplateId}/{mailTemplateStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (278, 0, 271, '0,1,2,270,271', '邮件模板字段校验是否存在', 'gitegg:service:extension:extension:mail:template:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/check', '/gitegg-service-extension/extension/mail/template/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (279, 0, 271, '0,1,2,270,271', '邮件模板数据导出', 'gitegg:service:extension:extension:mail:template:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/download', '/gitegg-service-extension/extension/mail/template/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (280, 0, 271, '0,1,2,270,271', '邮件模板数据导入模板下载', 'gitegg:service:extension:extension:mail:template:download:template', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/download/template', '/gitegg-service-extension/extension/mail/template/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (281, 0, 271, '0,1,2,270,271', '邮件模板数据导入', 'gitegg:service:extension:extension:mail:template:upload', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/template/upload', '/gitegg-service-extension/extension/mail/template/upload', 2, 1, NULL, 1, NULL, 1, 1, '数据导入邮件模板', '2022-06-24 17:32:23', 1, '2022-06-24 17:32:23', 1, 0);
INSERT INTO `t_sys_resource` VALUES (282, 0, 270, '0,1,2,270', '邮件记录', 'gitegg:service:extension:extension:mail:log:table', '2', 'youjianjilu', 'gitegg/service/extension/extension/mail/log/table', 'system/extension/mail/MailLogTable.vue', 3, 1, NULL, 1, 'mailLogTable', 1, 1, '邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:40:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (283, 0, 282, '0,1,2,270,282', '获取邮件记录列表', 'gitegg:service:extension:extension:mail:log:list', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/list', '/gitegg-service-extension/extension/mail/log/list', 2, 1, NULL, 1, NULL, 1, 1, '获取邮件记录列表数据', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (284, 0, 282, '0,1,2,270,282', '添加邮件记录', 'gitegg:service:extension:extension:mail:log:create', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/create', '/gitegg-service-extension/extension/mail/log/create', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (285, 0, 282, '0,1,2,270,282', '更新邮件记录', 'gitegg:service:extension:extension:mail:log:update', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/update', '/gitegg-service-extension/extension/mail/log/update', 2, 1, NULL, 1, NULL, 1, 1, '更新邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (286, 0, 282, '0,1,2,270,282', '删除邮件记录', 'gitegg:service:extension:extension:mail:log:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/delete', '/gitegg-service-extension/extension/mail/log/delete/{mailLogId}', 2, 1, NULL, 1, NULL, 1, 1, '删除邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (287, 0, 282, '0,1,2,270,282', '批量删除邮件记录', 'gitegg:service:extension:extension:mail:log:batch:delete', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/batch/delete', '/gitegg-service-extension/extension/mail/log/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (288, 0, 282, '0,1,2,270,282', '邮件记录字段校验是否存在', 'gitegg:service:extension:extension:mail:log:check', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/check', '/gitegg-service-extension/extension/mail/log/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (289, 0, 282, '0,1,2,270,282', '邮件记录数据导出', 'gitegg:service:extension:extension:mail:log:download', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/download', '/gitegg-service-extension/extension/mail/log/download', 2, 1, NULL, 1, NULL, 1, 1, '数据导出邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (290, 0, 282, '0,1,2,270,282', '邮件记录数据导入模板下载', 'gitegg:service:extension:extension:mail:log:download:template', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/download/template', '/gitegg-service-extension/extension/mail/log/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (291, 0, 282, '0,1,2,270,282', '邮件记录数据导入', 'gitegg:service:extension:extension:mail:log:upload', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/log/upload', '/gitegg-service-extension/extension/mail/log/upload', 2, 1, NULL, 1, NULL, 1, 1, '数据导入邮件记录', '2022-06-24 18:31:40', 1, '2022-06-24 18:31:40', 1, 0);
INSERT INTO `t_sys_resource` VALUES (292, 0, 259, '0,1,2,270,259', '测试发送简单邮件', 'gitegg:service:extension:extension:mail:test:send:simple', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/test/send/simple', '/gitegg-service-extension/extension/mail/test/send/simple', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (293, 0, 259, '0,1,2,270,259', '测试发送Html邮件', 'gitegg:service:extension:extension:mail:test:send:html', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/test/send/html', '/gitegg-service-extension/extension/mail/test/send/html', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (294, 0, 259, '0,1,2,270,259', '测试发送模板邮件', 'gitegg:service:extension:extension:mail:test:send:template', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/test/send/template', '/gitegg-service-extension/extension/mail/test/send/template', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (295, 0, 259, '0,1,2,270,259', '测试发送发送带附件邮件', 'gitegg:service:extension:extension:mail:test:send:attachment', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/test/send/attachment', '/gitegg-service-extension/extension/mail/test/send/attachment', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (296, 0, 259, '0,1,2,270,259', '测试异步发送带附件邮件', 'gitegg:service:extension:extension:mail:test:send:async:attachment', '4', 'xitongrizhi', 'gitegg/service/extension/extension/mail/test/send/async/attachment', '/gitegg-service-extension/extension/mail/test/send/async/attachment', 2, 1, NULL, 1, NULL, 1, 1, '添加邮件渠道表', '2022-06-24 15:58:39', 1, '2022-06-24 15:58:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (297, 0, 246, '0,1,246', '简单流程', 'plugin:workflow:manager:simple', '2', 'jiandanliucheng', 'plugin/workflow/manager/modeler/simple', 'nested', 1, 1, NULL, 1, NULL, 1, 1, '简单流程设计', '2016-04-22 10:43:19', 1, '2023-01-13 23:21:10', 1, 0);
INSERT INTO `t_sys_resource` VALUES (298, 0, 246, '0,1,246', '经典流程', 'plugin:workflow:manager:advanced', '2', 'jingdianliucheng', 'plugin/workflow/manager/modeler/advanced', 'nested', 2, 1, NULL, 1, NULL, 1, 1, '经典流程设计', '2016-04-22 10:43:19', 1, '2023-01-13 23:21:21', 1, 0);
INSERT INTO `t_sys_resource` VALUES (299, 0, 297, '0,1,246,297', '流程设计', 'plugin:workflow:manager:simple:process', '2', 'liuchengsheji', 'plugin/workflow/manager/simple/process', 'plugin/workflow/manager/simple/processDefinition', 1, 1, NULL, 1, 'SimpleProcess', 1, 1, '简单流程设计', '2016-04-22 10:43:19', 1, '2022-06-17 14:55:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (300, 0, 297, '0,1,246,297', '流程列表', 'plugin:workflow:manager:simple:process:list', '2', 'liuchengliebiao', 'plugin/workflow/manager/simple/process/list', 'plugin/workflow/manager/simple/processTable', 2, 1, NULL, 1, 'SimpleProcessList', 1, 1, '简单流程列表', '2016-04-22 10:43:19', 1, '2022-06-17 14:55:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (301, 0, 298, '0,1,246,298', '流程管理', 'plugin:workflow:manager:advanced:process', '2', 'liuchengsheji', 'plugin/workflow/manager/advanced/process', 'plugin/workflow/manager/advanced/processTable', 1, 1, NULL, 1, 'AdvancedProcess', 1, 1, '经典流程配置', '2016-04-22 10:43:19', 1, '2022-06-17 14:55:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (302, 0, 298, '0,1,246,298', '表单管理', 'plugin:workflow:manager:advanced:form', '2', 'biaodanguanli', 'plugin/workflow/manager/advanced/form', 'plugin/workflow/manager/advanced/formTable', 2, 1, NULL, 1, 'AdvancedForm', 1, 1, '经典表单配置', '2016-04-22 10:43:19', 1, '2022-06-17 14:55:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (303, 0, 298, '0,1,246,298', '模型管理', 'plugin:workflow:manager:advanced:model', '2', 'moxingguanli', 'plugin/workflow/manager/advanced/model', 'plugin/workflow/manager/advanced/modelTable', 3, 1, NULL, 1, 'AdvancedModel', 1, 1, '经典流程模型', '2016-04-22 10:43:19', 1, '2021-10-25 19:33:36', 1, 0);
INSERT INTO `t_sys_resource` VALUES (304, 0, 303, '0,1,246,298,303', '模型设计', 'plugin:workflow:manager:advanced:model:design', '2', 'quanxianguanli', 'plugin/workflow/manager/advanced/model/design', 'plugin/workflow/manager/advanced/modelDesign', 1, 0, NULL, 1, 'AdvancedModelDesign', 1, 1, '经典流程模型设计', '2016-04-22 10:43:19', 1, '2022-07-10 23:29:39', 1, 0);
INSERT INTO `t_sys_resource` VALUES (305, 0, 3, '0,1,2,3', '用户详情页', 'system:auth:user:detail', '2', 'xitongrizhi', 'system/user/detail/:id', 'system/user/UserDetail', 12, 0, '/system/cfg/permission/system/user/table', 0, 'UserDetail', 1, 1, '编辑代码配置', '2021-09-06 09:12:28', 1, '2022-11-06 00:35:27', 1, 0);
INSERT INTO `t_sys_resource` VALUES (306, 0, 4, '0,1,2,3,4', '用户数据导出', 'system:user:batch:export', '4', 'xitongrizhi', 'system/user/batch/export', '/gitegg-service-system/user/export', 4, 1, NULL, 1, NULL, 1, 1, '用户数据导出', '2022-05-18 16:01:16', 1, '2022-11-14 18:21:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (307, 0, 4, '0,1,2,3,4', '用户导入模板下载', 'system:user:batch:download:template', '4', 'xitongrizhi', 'system/user/batch/download/template', '/gitegg-service-system/user/download/template', 5, 1, NULL, 1, NULL, 1, 1, '下载用户导入模板', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (308, 0, 4, '0,1,2,3,4', '用户数据导入', 'system:user:batch:import', '4', 'xitongrizhi', 'system/user/batch/import', '/gitegg-service-system/user/import', 6, 1, NULL, 1, NULL, 1, 1, '用户数据导入', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (309, 0, 5, '0,1,2,3,5', '角色数据导出', 'system:role:batch:export', '4', 'xitongrizhi', 'system/role/batch/export', '/gitegg-service-system/role/export', 4, 1, NULL, 1, NULL, 1, 1, '角色数据导出', '2022-05-18 16:01:16', 1, '2022-11-14 18:21:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (310, 0, 5, '0,1,2,3,5', '角色导入模板下载', 'system:role:batch:download:template', '4', 'xitongrizhi', 'system/role/batch/download/template', '/gitegg-service-system/role/download/template', 5, 1, NULL, 1, NULL, 1, 1, '角色用户导入模板', '2022-05-16 14:05:35', 1, '2022-05-16 14:05:35', 1, 0);
INSERT INTO `t_sys_resource` VALUES (311, 0, 5, '0,1,2,3,5', '角色数据导入', 'system:role:batch:import', '4', 'xitongrizhi', 'system/role/batch/import', '/gitegg-service-system/role/import', 6, 1, NULL, 1, NULL, 1, 1, '角色数据导入', '2022-05-18 16:01:16', 1, '2022-05-18 16:01:16', 1, 0);
INSERT INTO `t_sys_resource` VALUES (320, 0, 170, '0,1,125,170', '批量删除代码基础配置', 'plugin:code:dict:batch:delete', '4', 'xitongrizhi', 'plugin/code/generator/dict/batch/delete', '/gitegg-code-generator/code/generator/dict/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除代码基础配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (321, 0, 132, '0,1,125,132', '所有数据源配置列表', 'plugin:code:datasource:all', '4', 'quanxianguanli', 'plugin/code/generator/datasource/all', '/gitegg-code-generator/code/generator/datasource/all', 1, 1, NULL, 1, NULL, 1, 1, '数据源配置列表不分页', '2016-04-22 10:43:19', 1, '2018-11-02 14:48:15', 1, 0);
INSERT INTO `t_sys_resource` VALUES (322, 0, 147, '0,1,125,147', '批量删除联表配置', 'plugin:code:table:batch:delete', '4', 'xitongrizhi', 'batch/delete', '/gitegg-code-generator/code/generator/table/join/batch/delete', 4, 1, NULL, 1, NULL, 1, 1, '删除联表配置', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);
INSERT INTO `t_sys_resource` VALUES (323, 0, 125, '0,1,125', '接口配置', 'plugin:code:api:table', '2', 'jiekoupeizhi', 'api', 'plugin/codeGenerator/api/ApiTable.vue', 4, 1, NULL, 1, 'generatorApiTable', 1, 1, '接口配置表', '2022-12-11 22:44:50', 1, '2022-12-12 12:49:41', 1, 0);
INSERT INTO `t_sys_resource` VALUES (324, 0, 323, '0,1,125,323', '获取接口配置表列表', 'plugin:code:api:list', '4', 'xitongrizhi', 'list', '/gitegg-code-generator/code/generator/api/list', 2, 1, NULL, 1, NULL, 1, 1, '获取接口配置表列表数据', '2022-12-11 22:44:50', 1, '2022-12-11 22:44:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (325, 0, 323, '0,1,125,323', '添加接口配置表', 'plugin:code:api:create', '4', 'xitongrizhi', 'create', '/gitegg-code-generator/code/generator/api/create', 2, 1, NULL, 1, NULL, 1, 1, '添加接口配置表', '2022-12-11 22:44:50', 1, '2022-12-11 22:44:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (326, 0, 323, '0,1,125,323', '更新接口配置表', 'plugin:code:api:update', '4', 'xitongrizhi', 'update', '/gitegg-code-generator/code/generator/api/update', 2, 1, NULL, 1, NULL, 1, 1, '更新接口配置表', '2022-12-11 22:44:50', 1, '2022-12-11 22:44:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (327, 0, 323, '0,1,125,323', '删除接口配置表', 'plugin:code:api:delete', '4', 'xitongrizhi', 'delete', '/gitegg-code-generator/code/generator/api/delete/{generatorApiId}', 2, 1, NULL, 1, NULL, 1, 1, '删除接口配置表', '2022-12-11 22:44:50', 1, '2022-12-11 22:44:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (328, 0, 323, '0,1,125,323', '批量删除接口配置表', 'plugin:code:api:batch:delete', '4', 'xitongrizhi', 'batch/delete', '/gitegg-code-generator/code/generator/api/batch/delete', 2, 1, NULL, 1, NULL, 1, 1, '批量删除接口配置表', '2022-12-11 22:44:51', 1, '2022-12-11 22:44:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (329, 0, 323, '0,1,125,323', '接口配置表状态修改', 'plugin:code:api:status', '4', 'xitongrizhi', 'status', '/gitegg-code-generator/code/generator/api/status/{generatorApiId}/{generatorApiStatus}', 2, 1, NULL, 1, NULL, 1, 1, '批量删除接口配置表', '2022-12-11 22:44:51', 1, '2022-12-11 22:44:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (330, 0, 323, '0,1,125,323', '接口配置表字段校验是否存在', 'plugin:code:api:check', '4', 'xitongrizhi', 'check', '/gitegg-code-generator/code/generator/api/check', 2, 1, NULL, 1, NULL, 1, 1, '字段校验是否存在接口配置表', '2022-12-11 22:44:51', 1, '2022-12-11 22:44:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (331, 0, 323, '0,1,125,323', '接口配置表数据导出', 'plugin:code:api:export', '4', 'xitongrizhi', 'export', '/gitegg-code-generator/code/generator/api/export', 2, 1, NULL, 1, NULL, 1, 1, '数据导出接口配置表', '2022-12-11 22:44:51', 1, '2022-12-11 22:44:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (332, 0, 323, '0,1,125,323', '接口配置表数据导入模板下载', 'plugin:code:api:download:template', '4', 'xitongrizhi', 'download/template', '/gitegg-code-generator/code/generator/api/download/template', 2, 1, NULL, 1, NULL, 1, 1, '数据导入模板下载接口配置表', '2022-12-11 22:44:51', 1, '2022-12-11 22:44:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (333, 0, 323, '0,1,125,323', '接口配置表数据导入', 'plugin:code:api:import', '4', 'xitongrizhi', 'import', '/gitegg-code-generator/code/generator/api/import', 2, 1, NULL, 1, NULL, 1, 1, '数据导入接口配置表', '2022-12-11 22:44:51', 1, '2022-12-11 22:44:51', 1, 0);
INSERT INTO `t_sys_resource` VALUES (334, 0, 323, '0,1,125,323', '获取全部接口配置表列表', 'plugin:code:api:list:all', '4', 'xitongrizhi', 'all', '/gitegg-code-generator/code/generator/api/all', 2, 1, NULL, 1, NULL, 1, 1, '获取全部接口配置表列表数据', '2022-12-11 22:44:50', 1, '2022-12-11 22:44:50', 1, 0);
INSERT INTO `t_sys_resource` VALUES (335, 0, 61, '0,1,2,8,61', '更新租户状态', 'base:tenant:status', '4', 'xitongrizhi', 'status', '/gitegg-service-base/base/tenant/status/{tenantId}/{tenantStatus}', 2, 1, NULL, 1, NULL, 1, 1, '更新租户信息表', '2018-10-27 17:40:14', 1, '2018-11-02 14:53:38', 1, 0);

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父id',
  `role_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色标识',
  `role_level` int(11) NULL DEFAULT NULL COMMENT '角色级别',
  `role_status` tinyint(2) NULL DEFAULT 1 COMMENT '1有效，0禁用',
  `data_permission_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'DATA_PERMISSION_SELF' COMMENT '角色数据权限类型,默认只能查询本人数据',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_ROLE_NAME`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES (1, 0, 0, '超级管理员', 'SPADMIN', 1, 0, '3', '保留，不启用', '2016-07-01 11:30:31', 1, '2022-12-16 23:15:00', 1, 0);
INSERT INTO `t_sys_role` VALUES (2, 0, 0, '系统管理员', 'SYSADMIN', 2, 1, '3', '管理系统权限资源等后台用户', '2016-07-01 11:30:28', 1, '2021-07-23 17:53:09', 1, 0);
INSERT INTO `t_sys_role` VALUES (3, 0, 0, '业务管理员', 'BUSINESS_ADMIN', 3, 1, '3', '管理业务相关系统操作', '2016-07-01 11:27:48', 1, '2018-05-27 16:10:08', 1, 0);
INSERT INTO `t_sys_role` VALUES (4, 0, 0, '测试用户', 'test', 4, 1, '1', '管理业务相关系统操作', '2016-07-01 11:27:48', 1, '2022-12-08 11:23:36', 1, 0);
INSERT INTO `t_sys_role` VALUES (5, 0, 0, '注册用户', 'WECHAT_MEMBER', 4, 1, '3', '管理业务相关系统操作', '2016-07-01 11:27:48', 1, '2020-11-29 19:34:34', 1, 0);

-- ----------------------------
-- Table structure for t_sys_role_data_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_data_permission`;
CREATE TABLE `t_sys_role_data_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `data_permission_id` bigint(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和数据权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_role_data_permission
-- ----------------------------
INSERT INTO `t_sys_role_data_permission` VALUES (84, 0, 89, 3, '2021-05-14 16:20:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (85, 0, 83, 3, '2021-05-14 16:20:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (86, 0, 87, 2, '2021-05-14 16:20:42', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (87, 0, 83, 2, '2021-05-14 16:20:42', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (88, 0, 81, 1, '2021-05-14 16:20:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (89, 0, 82, 1, '2021-05-14 16:20:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (90, 0, 8, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (91, 0, 7, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (92, 0, 20, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (93, 0, 19, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (94, 0, 18, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (95, 0, 17, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (96, 0, 16, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (97, 0, 15, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (98, 0, 14, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (99, 0, 13, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (100, 0, 12, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (101, 0, 11, 3, '2021-05-14 16:45:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (102, 0, 47, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (103, 0, 46, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (104, 0, 45, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (105, 0, 44, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (106, 0, 43, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (107, 0, 42, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (108, 0, 41, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (109, 0, 15, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (110, 0, 14, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (111, 0, 13, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (112, 0, 12, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (113, 0, 11, 3, '2021-05-14 16:46:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (114, 0, 80, 3, '2021-05-14 16:52:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (115, 0, 75, 3, '2021-05-14 16:52:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (116, 0, 74, 3, '2021-05-14 16:52:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (117, 0, 73, 3, '2021-05-14 16:52:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (118, 0, 72, 3, '2021-05-14 16:52:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (119, 0, 71, 3, '2021-05-14 16:52:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (120, 0, 86, 1, '2021-05-14 16:57:14', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (121, 0, 89, 6, '2021-05-14 17:28:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (122, 0, 90, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (123, 0, 89, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (124, 0, 88, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (125, 0, 87, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (126, 0, 86, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (127, 0, 85, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (128, 0, 84, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (129, 0, 83, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (130, 0, 82, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (131, 0, 81, 5, '2021-05-14 17:30:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (132, 0, 65, 5, '2021-05-14 20:00:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (133, 0, 66, 5, '2021-05-14 20:00:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (134, 0, 87, 6, '2021-05-14 20:00:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (135, 0, 66, 5, '2021-05-14 20:00:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (136, 0, 84, 3, '2022-05-29 23:17:51', 17, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (137, 0, 93, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (138, 0, 88, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (139, 0, 87, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (140, 0, 70, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (141, 0, 71, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (142, 0, 72, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (143, 0, 69, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (144, 0, 68, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (145, 0, 67, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (146, 0, 66, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (147, 0, 65, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (148, 0, 64, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (149, 0, 63, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (150, 0, 62, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (151, 0, 61, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (152, 0, 60, 9, '2022-11-26 14:48:32', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_data_permission` VALUES (153, 0, 80, 9, '2022-11-26 14:49:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (154, 0, 81, 9, '2022-11-26 14:49:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (155, 0, 82, 9, '2022-11-26 14:49:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (156, 0, 79, 9, '2022-11-26 14:49:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (157, 0, 78, 9, '2022-11-26 14:49:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_data_permission` VALUES (158, 0, 77, 9, '2022-11-26 14:49:12', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_resource`;
CREATE TABLE `t_sys_role_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `resource_id` bigint(20) NOT NULL COMMENT '资源id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 485 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_role_resource
-- ----------------------------
INSERT INTO `t_sys_role_resource` VALUES (1, 0, 2, 1, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (2, 0, 2, 3, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (3, 0, 2, 4, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (4, 0, 2, 15, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (5, 0, 2, 5, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (6, 0, 2, 6, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (7, 0, 2, 7, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (8, 0, 2, 2, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (9, 0, 2, 8, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (10, 0, 2, 9, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (11, 0, 2, 10, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (12, 0, 2, 11, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (13, 0, 2, 12, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (14, 0, 2, 13, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (16, 0, 3, 14, '2018-11-09 21:26:59', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (17, 0, 3, 16, '2018-11-09 21:26:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (18, 0, 3, 17, '2018-11-09 21:26:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (19, 0, 3, 18, '2018-11-09 21:26:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (21, 0, 3, 20, '2018-11-09 21:26:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (64, 0, 3, 37, '2018-12-24 14:05:22', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (65, 0, 3, 39, '2018-12-27 14:49:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (69, 0, 3, 19, '2019-01-11 11:54:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (70, 0, 2, 14, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (71, 0, 2, 15, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (72, 0, 2, 16, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (73, 0, 2, 17, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (74, 0, 2, 18, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (75, 0, 2, 19, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (76, 0, 2, 20, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (77, 0, 2, 21, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (78, 0, 2, 22, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (79, 0, 2, 23, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (80, 0, 2, 24, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (81, 0, 2, 25, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (82, 0, 2, 26, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (83, 0, 2, 27, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (85, 0, 2, 29, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (86, 0, 2, 30, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (87, 0, 2, 31, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (88, 0, 2, 32, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (89, 0, 2, 33, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (90, 0, 2, 34, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (91, 0, 2, 35, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (92, 0, 2, 36, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (93, 0, 2, 37, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (94, 0, 2, 38, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (95, 0, 2, 39, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (96, 0, 2, 40, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (97, 0, 2, 41, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (98, 0, 2, 42, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (99, 0, 2, 43, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (100, 0, 2, 44, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (101, 0, 4, 4, '2020-11-29 19:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (102, 0, 4, 14, '2020-11-29 19:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (103, 0, 4, 15, '2020-11-29 19:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (104, 0, 4, 16, '2020-11-29 19:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (105, 0, 4, 17, '2020-11-29 19:34:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (106, 0, 3, 15, '2020-11-29 19:34:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (107, 0, 2, 48, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (108, 0, 2, 49, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (109, 0, 2, 50, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (110, 0, 2, 51, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (111, 0, 2, 52, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (112, 0, 2, 53, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (113, 0, 2, 54, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (114, 0, 2, 55, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (115, 0, 2, 56, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (116, 0, 2, 57, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (117, 0, 2, 58, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (118, 0, 2, 59, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (119, 0, 2, 60, '2018-10-27 19:24:13', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (120, 0, 2, 61, '2020-12-17 21:54:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (121, 0, 2, 62, '2020-12-17 21:54:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (122, 0, 2, 63, '2020-12-17 21:54:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (123, 0, 2, 64, '2020-12-17 21:54:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (124, 0, 2, 65, '2020-12-17 21:54:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (125, 0, 2, 66, '2020-12-17 21:54:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (135, 0, 3, 31, '2020-12-27 14:55:31', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (136, 0, 2, 67, '2021-01-05 16:33:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (137, 0, 2, 68, '2021-01-05 16:33:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (138, 0, 2, 69, '2021-01-05 18:01:23', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (139, 0, 2, 70, '2021-01-26 11:49:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (145, 0, 2, 76, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (146, 0, 2, 77, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (147, 0, 2, 78, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (148, 0, 2, 79, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (149, 0, 2, 80, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (150, 0, 2, 81, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (151, 0, 2, 82, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (152, 0, 2, 83, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (153, 0, 2, 84, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (154, 0, 2, 85, '2021-01-26 14:45:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (155, 0, 2, 86, '2021-05-06 10:52:27', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (156, 0, 2, 87, '2021-05-06 10:52:27', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (157, 0, 2, 88, '2021-05-06 10:52:27', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (158, 0, 2, 89, '2021-05-06 10:52:27', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (159, 0, 2, 90, '2021-05-06 10:52:27', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (160, 0, 2, 91, '2021-05-06 10:52:27', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (161, 0, 2, 92, '2021-05-06 15:14:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (162, 0, 2, 93, '2021-05-07 10:21:07', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (163, 0, 2, 93, '2021-05-07 10:54:44', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (164, 0, 2, 94, '2021-05-08 15:58:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (165, 0, 2, 95, '2021-05-08 15:58:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (166, 0, 2, 96, '2021-05-08 15:58:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (167, 0, 2, 97, '2021-05-08 15:58:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (168, 0, 2, 98, '2021-05-08 15:58:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (169, 0, 2, 99, '2021-05-08 15:58:03', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (170, 0, 2, 100, '2021-05-08 16:26:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (171, 0, 2, 101, '2021-05-10 14:31:56', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (172, 0, 2, 102, '2021-05-10 14:31:56', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (173, 0, 2, 105, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (174, 0, 2, 106, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (175, 0, 2, 107, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (176, 0, 2, 108, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (177, 0, 2, 109, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (178, 0, 2, 110, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (179, 0, 2, 111, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (180, 0, 2, 112, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (181, 0, 2, 113, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (182, 0, 2, 114, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (183, 0, 2, 115, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (184, 0, 2, 116, '2021-05-13 14:13:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (185, 0, 2, 118, '2021-05-13 16:55:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (186, 0, 2, 117, '2021-05-13 16:55:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (187, 0, 1, 1, '2021-05-13 18:46:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (188, 0, 2, 119, '2021-05-14 15:15:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (189, 0, 2, 120, '2021-05-14 15:15:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (190, 0, 2, 121, '2021-05-14 17:08:19', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (191, 0, 2, 122, '2021-05-14 17:47:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (192, 0, 2, 123, '2021-05-14 18:55:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (193, 0, 2, 124, '2021-05-14 18:55:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (194, 0, 60, 1, '2021-05-14 20:00:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (195, 0, 91, 2, '2021-07-06 18:14:05', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (196, 0, 91, 3, '2021-07-06 18:14:05', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (197, 0, 91, 8, '2021-07-06 18:14:05', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (198, 0, 2, 125, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (199, 0, 2, 126, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (200, 0, 2, 127, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (201, 0, 2, 128, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (202, 0, 2, 129, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (203, 0, 2, 130, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (204, 0, 2, 131, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (205, 0, 2, 132, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (206, 0, 2, 133, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (207, 0, 2, 134, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (208, 0, 2, 135, '2021-08-24 11:07:11', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (209, 0, 2, 136, '2021-08-24 17:24:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (210, 0, 2, 137, '2021-08-24 17:24:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (211, 0, 2, 138, '2021-08-26 11:17:43', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (212, 0, 2, 139, '2021-08-26 17:23:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (213, 0, 2, 140, '2021-08-26 17:23:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (214, 0, 2, 141, '2021-08-26 17:23:52', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (215, 0, 2, 141, '2021-08-27 18:07:33', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (216, 0, 2, 142, '2021-09-06 16:52:16', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (217, 0, 2, 143, '2021-09-07 19:08:33', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (218, 0, 2, 146, '2021-09-14 11:39:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (219, 0, 2, 145, '2021-09-14 11:39:59', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (220, 0, 2, 147, '2021-09-16 10:29:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (221, 0, 2, 148, '2021-09-16 10:29:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (222, 0, 2, 149, '2021-09-16 10:29:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (223, 0, 2, 150, '2021-09-16 10:29:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (224, 0, 2, 151, '2021-09-16 10:29:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (225, 0, 2, 152, '2021-09-18 13:47:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (226, 0, 2, 153, '2021-09-18 13:47:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (227, 0, 2, 154, '2021-09-18 13:47:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (228, 0, 2, 155, '2021-09-18 14:36:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (229, 0, 2, 156, '2021-09-22 16:03:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (230, 0, 2, 157, '2021-09-22 16:03:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (231, 0, 2, 158, '2021-09-22 16:03:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (232, 0, 2, 159, '2021-09-22 16:03:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (233, 0, 2, 160, '2021-09-22 16:03:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (234, 0, 2, 161, '2021-09-26 11:06:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (235, 0, 2, 162, '2021-09-28 15:54:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (236, 0, 2, 163, '2021-09-30 16:37:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (237, 0, 2, 164, '2021-09-30 16:37:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (238, 0, 2, 165, '2021-09-30 16:37:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (239, 0, 2, 166, '2021-09-30 16:37:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (240, 0, 2, 167, '2021-09-30 16:37:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (241, 0, 2, 168, '2021-09-30 16:37:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (242, 0, 2, 169, '2021-09-30 16:37:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (243, 0, 2, 170, '2021-09-30 17:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (244, 0, 2, 171, '2021-09-30 17:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (245, 0, 2, 172, '2021-09-30 17:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (246, 0, 2, 173, '2021-09-30 17:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (247, 0, 2, 174, '2021-09-30 17:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (248, 0, 2, 175, '2021-09-30 17:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (249, 0, 2, 176, '2021-09-30 17:30:45', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (250, 0, 2, 177, '2021-09-30 18:02:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (251, 0, 2, 178, '2021-09-30 18:02:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (252, 0, 2, 179, '2021-09-30 18:02:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (253, 0, 2, 180, '2021-09-30 18:02:52', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (254, 0, 2, 180, '2021-09-30 18:06:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (255, 0, 2, 181, '2021-09-30 18:18:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (256, 0, 2, 182, '2021-10-11 18:57:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (257, 0, 2, 183, '2021-10-11 19:25:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (258, 0, 2, 184, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (259, 0, 2, 185, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (260, 0, 2, 186, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (261, 0, 2, 187, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (262, 0, 2, 188, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (263, 0, 2, 189, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (264, 0, 2, 190, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (265, 0, 2, 191, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (266, 0, 2, 192, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (267, 0, 2, 193, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (268, 0, 2, 194, '2021-10-15 11:57:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (281, 0, 2, 195, '2021-12-17 16:33:58', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (282, 0, 2, 207, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (283, 0, 2, 196, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (284, 0, 2, 197, '2022-05-16 14:15:21', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (285, 0, 2, 198, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (286, 0, 2, 199, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (287, 0, 2, 200, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (288, 0, 2, 201, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (289, 0, 2, 202, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (290, 0, 2, 203, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (291, 0, 2, 204, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (292, 0, 2, 205, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (293, 0, 2, 206, '2022-05-16 14:15:21', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (294, 0, 2, 208, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (295, 0, 2, 209, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (296, 0, 2, 210, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (297, 0, 2, 211, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (298, 0, 2, 212, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (299, 0, 2, 213, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (300, 0, 2, 214, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (301, 0, 2, 215, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (302, 0, 2, 216, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (303, 0, 2, 217, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (304, 0, 2, 218, '2022-05-18 16:07:01', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (305, 0, 2, 197, '2022-05-18 16:29:54', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (306, 0, 2, 219, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (307, 0, 2, 220, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (308, 0, 2, 221, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (309, 0, 2, 222, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (310, 0, 2, 223, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (311, 0, 2, 224, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (312, 0, 2, 225, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (313, 0, 2, 226, '2022-05-19 18:51:04', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (314, 0, 2, 227, '2022-05-19 19:15:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (315, 0, 2, 228, '2022-05-19 19:15:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (316, 0, 2, 229, '2022-05-19 19:15:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (317, 0, 2, 230, '2022-05-19 19:15:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (318, 0, 2, 231, '2022-05-19 19:15:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (319, 0, 2, 232, '2022-05-19 19:15:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (320, 0, 2, 233, '2022-05-19 19:15:35', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (321, 0, 2, 234, '2022-05-24 16:48:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (322, 0, 2, 235, '2022-05-24 16:48:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (323, 0, 2, 236, '2022-05-24 16:48:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (324, 0, 2, 237, '2022-05-24 16:48:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (325, 0, 2, 238, '2022-05-24 18:43:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (326, 0, 2, 239, '2022-05-24 18:43:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (327, 0, 2, 240, '2022-05-24 18:43:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (328, 0, 2, 241, '2022-05-24 18:43:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (329, 0, 2, 242, '2022-05-24 18:43:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (330, 0, 2, 243, '2022-05-24 18:43:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (331, 0, 2, 244, '2022-05-25 10:07:55', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (332, 0, 2, 245, '2022-06-16 17:54:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (333, 0, 2, 247, '2022-06-16 17:54:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (334, 0, 2, 248, '2022-06-16 17:54:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (335, 0, 2, 249, '2022-06-16 17:54:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (336, 0, 2, 250, '2022-06-16 17:54:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (337, 0, 2, 246, '2022-06-16 17:54:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (338, 0, 2, 251, '2022-06-16 17:54:49', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (339, 0, 2, 252, '2022-06-16 17:54:49', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (340, 0, 2, 253, '2022-06-16 17:59:31', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (341, 0, 2, 252, '2022-06-16 18:25:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (342, 0, 2, 251, '2022-06-16 18:25:29', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (343, 0, 2, 253, '2022-06-16 18:25:29', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (344, 0, 2, 254, '2022-06-17 14:56:16', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (345, 0, 2, 270, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (346, 0, 2, 259, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (347, 0, 2, 260, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (348, 0, 2, 261, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (349, 0, 2, 262, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (350, 0, 2, 263, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (351, 0, 2, 264, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (352, 0, 2, 265, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (353, 0, 2, 266, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (354, 0, 2, 267, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (355, 0, 2, 268, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (356, 0, 2, 269, '2022-06-24 16:12:48', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (357, 0, 2, 271, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (358, 0, 2, 272, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (359, 0, 2, 273, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (360, 0, 2, 275, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (361, 0, 2, 274, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (362, 0, 2, 276, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (363, 0, 2, 277, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (364, 0, 2, 278, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (365, 0, 2, 279, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (366, 0, 2, 280, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (367, 0, 2, 281, '2022-06-24 17:35:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (368, 0, 2, 282, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (369, 0, 2, 283, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (370, 0, 2, 284, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (371, 0, 2, 285, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (372, 0, 2, 286, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (373, 0, 2, 287, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (374, 0, 2, 288, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (375, 0, 2, 289, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (376, 0, 2, 290, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (377, 0, 2, 291, '2022-06-24 18:34:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (378, 0, 2, 292, '2022-06-28 23:05:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (379, 0, 2, 293, '2022-06-28 23:05:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (380, 0, 2, 294, '2022-06-28 23:05:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (381, 0, 2, 295, '2022-06-28 23:05:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (382, 0, 2, 296, '2022-06-28 23:05:39', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (383, 0, 2, 297, '2022-07-10 12:28:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (384, 0, 2, 298, '2022-07-10 12:28:38', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (385, 0, 2, 299, '2022-07-10 12:28:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (386, 0, 2, 300, '2022-07-10 12:28:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (387, 0, 2, 301, '2022-07-10 12:28:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (388, 0, 2, 302, '2022-07-10 12:28:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (389, 0, 2, 303, '2022-07-10 12:28:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (390, 0, 2, 256, '2022-07-10 16:35:51', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (391, 0, 2, 257, '2022-07-10 16:35:51', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (392, 0, 2, 304, '2022-07-10 17:01:12', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (393, 0, 2, 257, '2022-07-10 23:32:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (394, 0, 2, 254, '2022-07-13 15:13:18', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (395, 0, 2, 305, '2022-11-05 20:08:32', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (396, 0, 2, 306, '2022-11-14 18:22:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (397, 0, 2, 307, '2022-11-14 18:22:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (398, 0, 2, 308, '2022-11-14 18:22:15', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (399, 0, 2, 309, '2022-11-22 15:30:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (400, 0, 2, 310, '2022-11-22 15:30:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (401, 0, 2, 311, '2022-11-22 15:30:50', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (402, 0, 92, 1, '2022-11-22 17:38:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (403, 0, 92, 245, '2022-11-22 17:38:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (404, 0, 92, 247, '2022-11-22 17:38:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (405, 0, 92, 248, '2022-11-22 17:38:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (406, 0, 92, 249, '2022-11-22 17:38:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (407, 0, 92, 250, '2022-11-22 17:38:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (408, 0, 93, 1, '2022-11-22 17:45:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (409, 0, 93, 245, '2022-11-22 17:45:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (410, 0, 93, 247, '2022-11-22 17:45:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (411, 0, 93, 248, '2022-11-22 17:45:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (412, 0, 93, 249, '2022-11-22 17:45:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (413, 0, 93, 250, '2022-11-22 17:45:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (414, 0, 93, 254, '2022-11-22 17:45:17', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (415, 0, 93, 298, '2022-11-22 17:45:17', 1, NULL, NULL, 1);
INSERT INTO `t_sys_role_resource` VALUES (416, 0, 93, 251, '2022-11-22 17:45:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (417, 0, 93, 297, '2022-11-22 17:45:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (418, 0, 93, 156, '2022-11-22 17:45:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (419, 0, 93, 152, '2022-11-22 17:45:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (420, 0, 93, 147, '2022-11-22 17:45:40', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (421, 0, 93, 300, '2022-11-22 17:46:55', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (422, 0, 93, 299, '2022-11-22 17:46:55', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (423, 0, 2, 320, '2022-11-30 00:12:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (424, 0, 2, 321, '2022-11-30 17:13:18', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (425, 0, 2, 322, '2022-12-05 11:14:27', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (426, 0, 4, 2, '2022-12-08 11:23:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (427, 0, 4, 1, '2022-12-08 11:23:46', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (428, 0, 2, 323, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (429, 0, 2, 324, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (430, 0, 2, 325, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (431, 0, 2, 326, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (432, 0, 2, 327, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (433, 0, 2, 328, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (434, 0, 2, 329, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (435, 0, 2, 330, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (436, 0, 2, 331, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (437, 0, 2, 332, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (438, 0, 2, 333, '2022-12-11 22:48:41', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (439, 0, 2, 334, '2022-12-12 16:46:20', 1, NULL, NULL, 0);
INSERT INTO `t_sys_role_resource` VALUES (474, 0, 2, 335, '2022-12-29 11:33:24', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_sms_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_sms_channel`;
CREATE TABLE `t_sys_sms_channel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `channel_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道编码',
  `channel_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道名称',
  `secret_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信发送的key id',
  `secret_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信发送的秘钥',
  `region_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'regionId仅阿里云需要',
  `channel_status` tinyint(2) NULL DEFAULT 1 COMMENT '渠道状态 1有效 0禁用',
  `sms_quantity` bigint(20) NOT NULL DEFAULT 0 COMMENT '渠道下可发送的短信数量',
  `sms_validity` datetime(0) NULL DEFAULT NULL COMMENT '短信可发送的有效期',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信渠道表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_sms_channel
-- ----------------------------
INSERT INTO `t_sys_sms_channel` VALUES (1, 0, 'aliyun', '阿里云短信', 'xxxxxxxxxxxx', 'xxxxxxxxxx', 'cn-hangzhou', 1, 10000000, '2122-12-31 23:59:59', '阿里云短信', '2021-01-26 15:19:11', 1, '2022-12-29 13:59:09', 1, 0);
INSERT INTO `t_sys_sms_channel` VALUES (5, 0, 'tencent', '腾讯云短信', '111', '111', '111', 1, 1000000, '2122-12-31 23:59:59', '腾讯云短信', '2022-05-25 13:50:36', 1, '2022-12-29 13:59:07', 1, 0);
INSERT INTO `t_sys_sms_channel` VALUES (6, 0, 'qiniu', '七牛云短信', '222', '222', '222', 1, 1000000, '2122-12-31 23:59:59', '七牛云短信', '2022-05-25 13:51:15', 1, '2022-12-02 17:00:32', 1, 0);
INSERT INTO `t_sys_sms_channel` VALUES (7, 0, 'aliyun', NULL, '345', '345', '345', 1, 3, '2022-12-18 23:59:59', '345345345', '2022-12-29 14:21:25', 1, NULL, NULL, 0);
INSERT INTO `t_sys_sms_channel` VALUES (8, 0, 'tencent', '腾讯云短信', '345', '345', '345', 1, 0, '2022-12-29 23:59:59', '345345', '2022-12-29 14:32:11', 1, '2022-12-29 14:45:50', 1, 0);
INSERT INTO `t_sys_sms_channel` VALUES (9, 0, 'tencent', '腾讯云短信', '3454', '5345', '345345', 1, 0, '2022-12-29 23:59:59', '345345', '2022-12-29 15:11:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_sms_channel` VALUES (10, 0, 'qiniu', '七牛云短信', '3245', '234', '234', 1, 0, '2022-12-30 23:59:59', '234234', '2022-12-29 15:16:28', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_sms_template`;
CREATE TABLE `t_sys_sms_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `channel_id` bigint(20) NULL DEFAULT NULL COMMENT '短信渠道id',
  `sms_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信编码',
  `sms_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信名称',
  `template_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信模板ID',
  `sign_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信签名',
  `template_status` tinyint(2) NULL DEFAULT 1 COMMENT '短信状态 1有效 0禁用',
  `template_type` tinyint(2) NULL DEFAULT 1 COMMENT '短信类型',
  `cache_code_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存key',
  `cache_time_out` bigint(20) NOT NULL DEFAULT 0 COMMENT '缓存有效期 值',
  `cache_time_out_unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缓存有效期 单位',
  `send_times_limit` bigint(20) NOT NULL DEFAULT 0 COMMENT '发送次数限制',
  `send_times_limit_period` bigint(20) NOT NULL DEFAULT 0 COMMENT '限制时间间隔',
  `send_times_limit_period_unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限制时间间隔 单位',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_sms_template
-- ----------------------------
INSERT INTO `t_sys_sms_template` VALUES (1, 0, 1, 'sms_login_code', '登录验证码', 'SMS_152420163', 'xxxxxx', 1, 1, 'SMS:CODE:LOGIN', 5, 'MINUTES', 10, 24, 'HOURS', '验证码${code}，您正在登录，若非本人操作，请勿泄露。', '2022-05-25 13:39:21', 1, '2022-05-25 13:40:11', 1, 0);
INSERT INTO `t_sys_sms_template` VALUES (2, 0, 1, 'sms_register_code', '注册验证码', 'SMS_152420161', 'xxxxxx', 1, 1, 'SMS:CODE:REGISTER', 5, 'MINUTES', 10, 24, 'HOURS', '验证码${code}，您正在登录，若非本人操作，请勿泄露。', '2022-05-25 13:41:37', 1, '2022-05-25 13:41:46', 1, 0);
INSERT INTO `t_sys_sms_template` VALUES (3, 0, 1, 'sms_change_pwd_code', '修改密码验证码', 'SMS_152420160', 'xxxxxx', 1, 1, 'SMS:CODE:CHANGE:PWD', 5, 'MINUTES', 10, 24, 'HOURS', '验证码${code}，您正在尝试修改登录密码，请妥善保管账户信息。', '2022-05-25 13:43:35', 1, '2022-05-25 13:46:19', 1, 1);
INSERT INTO `t_sys_sms_template` VALUES (4, 0, 1, 'sms_change_info_code', '信息变更验证码', 'SMS_152420159', 'xxxxxx', 1, 1, 'SMS:CODE:CHANGE:INFO', 5, 'MINUTES', 10, 24, 'HOURS', '验证码${code}，您正在尝试变更重要信息，请妥善保管账户信息。', '2022-05-25 13:45:52', 1, '2022-05-25 14:44:47', 1, 0);


-- ----------------------------
-- Table structure for t_sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_tenant`;
CREATE TABLE `t_sys_tenant`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `domain_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '域名',
  `background_image` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '背景图片',
  `contacts` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `account_limit` bigint(20) NULL DEFAULT 0 COMMENT '账号限额',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `license_key` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权码',
  `tenant_status` tinyint(2) NULL DEFAULT 1 COMMENT '租户状态 \'0\'禁用，\'1\' 启用,',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_tenant
-- ----------------------------
INSERT INTO `t_sys_tenant` VALUES (1, '1', '1', '1', '1', '1', '1', 1, '2020-12-17 10:00:00', '1', 1, '1', '2020-12-18 04:15:57', 1, NULL, NULL, 0);
INSERT INTO `t_sys_tenant` VALUES (2, '2', '2', '2', '2', '2', '2', 2, '2020-12-20 10:00:00', '2', 2, '2', '2020-12-21 00:48:08', 1, NULL, NULL, 0);
INSERT INTO `t_sys_tenant` VALUES (3, '3', '3', '3', '3', '3', '3', 3, '2020-12-20 10:22:33', '3', 3, '3', '2020-12-21 00:48:52', 1, NULL, NULL, 0);
INSERT INTO `t_sys_tenant` VALUES (4, '4', '4', '4', '4', '4', '4', 4, '2020-12-20 10:03:00', '4', 4, '4', '2020-12-21 00:53:17', 1, NULL, NULL, 0);
INSERT INTO `t_sys_tenant` VALUES (5, '5', '5', '5', '5', '5', '5', 5, '2020-12-21 10:00:01', '5', 5, '5', '2020-12-21 00:56:14', 1, NULL, NULL, 0);
INSERT INTO `t_sys_tenant` VALUES (6, '6', '6', '6', '6', '6', '6', 6, '2020-12-20 11:00:00', '1', 1, '1', '2020-12-21 04:20:12', 1, NULL, NULL, 0);
-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `nickname` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '2' COMMENT '1 : 男，0 : 女  2：保密',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `status` tinyint(2) NULL DEFAULT 1 COMMENT '\'0\'禁用，\'1\' 启用, \'2\' 密码过期或初次未修改',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `country` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `area` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `street` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '街道详细地址',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_USER_NAME`(`real_name`) USING BTREE,
  INDEX `INDEX_USER_PHONE`(`mobile`) USING BTREE,
  INDEX `INDEX_USER_EMAIL`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 0, 'admin', '管理员', '管理员', '1', 'giteegg@giteegg.com', '15851682222', '{bcrypt}$2a$10$iAjtODKqNKcBeingrTn1dOW3M.9i6MlzibdcAsAFbtxdFL80/dlaC', 1, 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', NULL, '110000', '110101', ' ', NULL, '系统管理员', '2018-05-19 17:55:36', 1, '2023-01-04 17:16:57', 1, 0);
INSERT INTO `t_sys_user` VALUES (2, 0, 'test001', 'test001', 'test001', '0', 'test001@qq.com', '15851683333', '$2a$10$wm3EX5HnBslChBBYTgfP8e8B6jslFUreh/8bblJ1TRsSA2mOnDpoi', 1, NULL, NULL, '320000', '320300', '320303', '2342342343', '2342343', '2020-11-27 16:43:06', 1, '2021-12-22 17:06:16', 1, 0);

-- ----------------------------
-- Table structure for t_sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_info`;
CREATE TABLE `t_sys_user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '上级ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '系统用户表用户ID',
  `wechat_open_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '小程序用户openid',
  `wechat_platform_open_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '公众号用户openid',
  `wechat_union_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '微信用户union id',
  `telephone` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `wechat_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `qq_number` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ号',
  `user_type` smallint(1) NULL DEFAULT 1 COMMENT '用户类型1、普通用户',
  `work_unit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作单位',
  `duties` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职务',
  `education` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
  `card_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
  `card_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号码',
  `card_front` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '正面照片',
  `card_reverse` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '反面照片',
  `graduated` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业院校',
  `gender` int(1) NULL DEFAULT NULL COMMENT '性别',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '出生日期',
  `graduated_date` date NULL DEFAULT NULL COMMENT '毕业时间',
  `register_time` datetime(0) NULL DEFAULT NULL COMMENT '注册日期',
  `register_ip` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册ip',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录日期',
  `last_login_ip` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录ip',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信注册会员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_user_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_USER_ID`(`user_id`) USING BTREE,
  INDEX `INDEX_ROLE_ID`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
INSERT INTO `t_sys_user_role` VALUES (1, 0, 1, 2, '2018-09-17 16:49:16', 1, '2018-09-17 16:49:19', 1, 0);
INSERT INTO `t_sys_user_role` VALUES (2, 0, 2, 2, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (3, 0, 3, 2, '2020-11-27 02:55:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (4, 0, 4, 1, '2020-12-24 16:26:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (5, 0, 4, 2, '2020-12-24 16:26:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (6, 0, 4, 3, '2020-12-24 16:26:10', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (7, 0, 2, 3, '2020-12-25 15:19:42', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (8, 0, 5, 2, '2020-12-25 17:02:36', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (9, 0, 5, 1, '2020-12-25 17:02:36', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (10, 0, 6, 2, '2021-01-11 09:55:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (11, 0, 6, 3, '2021-01-11 09:55:02', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (12, 0, 7, 2, '2021-07-06 18:17:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (13, 0, 7, 1, '2021-07-06 18:17:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (14, 0, 7, 3, '2021-07-06 18:17:26', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (15, 0, 8, 2, '2021-08-26 17:28:30', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (16, 0, 9, 4, '2022-05-29 21:39:06', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (17, 0, 10, 4, '2022-05-29 21:52:59', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (18, 0, 11, 4, '2022-05-29 21:57:37', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (19, 0, 12, 4, '2022-05-29 22:12:10', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (20, 0, 13, 4, '2022-05-29 22:30:56', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (21, 0, 14, 4, '2022-05-29 22:38:29', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (22, 0, 15, 4, '2022-05-29 23:04:56', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (23, 0, 16, 4, '2022-05-29 23:09:00', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (24, 0, 17, 2, '2022-05-29 23:12:30', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (25, 0, 18, 2, '2022-05-30 10:28:55', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (26, 0, 19, 2, '2022-05-30 14:10:44', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (27, 0, 20, 2, '2022-05-30 14:12:58', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (28, 0, 21, 2, '2022-05-30 14:25:16', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (29, 0, 22, 2, '2022-05-30 14:27:28', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (30, 0, 23, 2, '2022-05-30 14:31:36', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (31, 0, 24, 2, '2022-05-30 14:34:06', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (32, 0, 25, 2, '2022-06-01 18:39:10', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (33, 0, 26, 2, '2022-06-01 18:45:59', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (34, 0, 27, 2, '2022-06-01 18:58:07', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (35, 0, 28, 2, '2022-06-02 18:18:35', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (36, 0, 29, 2, '2022-06-02 18:21:02', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (37, 0, 30, 2, '2022-06-02 18:34:12', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (38, 0, 31, 2, '2022-06-02 19:11:00', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (39, 0, 32, 2, '2022-06-03 10:22:26', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (40, 0, 33, 2, '2022-06-03 12:22:08', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (41, 0, 34, 2, '2022-06-03 12:34:35', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (42, 0, 35, 2, '2022-06-03 12:46:13', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (43, 0, 36, 2, '2022-06-05 20:23:36', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (44, 0, 37, 2, '2022-06-17 16:47:06', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (45, 0, 38, 2, '2022-06-20 14:28:54', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (46, 0, 39, 2, '2022-07-05 17:24:29', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (47, 0, 40, 1, '2022-11-03 20:12:45', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (48, 0, 40, 2, '2022-11-03 20:12:45', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (49, 0, 40, 3, '2022-11-03 20:12:45', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (50, 0, 41, 3, '2022-11-07 15:13:16', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (51, 0, 41, 2, '2022-11-07 15:13:16', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (52, 0, 42, 2, '2022-11-22 13:45:47', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (53, 0, 42, 3, '2022-11-22 13:45:47', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (54, 0, 43, 2, '2022-11-24 17:47:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (55, 0, 43, 3, '2022-11-24 17:47:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (56, 0, 43, 4, '2022-11-24 17:47:07', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (57, 0, 42, 4, '2022-12-08 11:24:06', 1, NULL, NULL, 1);
INSERT INTO `t_sys_user_role` VALUES (58, 0, 40, 4, '2022-12-12 17:25:49', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (59, 0, 44, 1, '2022-12-16 14:21:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (60, 0, 44, 2, '2022-12-16 14:21:24', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (61, 0, 45, 4, '2022-12-16 14:22:06', 1, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (62, 0, 46, 2, '2023-01-29 17:48:33', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (63, 0, 47, 2, '2023-02-01 23:03:32', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (64, 0, 48, 2, '2023-02-02 15:34:16', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (65, 0, 49, 2, '2023-02-02 23:26:18', NULL, NULL, NULL, 0);
INSERT INTO `t_sys_user_role` VALUES (66, 0, 50, 2, '2023-02-02 23:28:35', NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for t_sys_wechat
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_wechat`;
CREATE TABLE `t_sys_wechat`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sex` int(1) NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `headimgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `privilege` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unionid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '最后修改人',
  `del_flag` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_sys_wechat
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
