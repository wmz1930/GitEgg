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

 Date: 10/06/2022 10:56:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for system_table
-- ----------------------------
DROP TABLE IF EXISTS `system_table`;
CREATE TABLE `system_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户第三方登录功能配置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '第三方用户信息表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '第三方用户关联关系' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户第三方登录信息配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_brand
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_brand`;
CREATE TABLE `t_mall_goods_brand`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '品牌名称',
  `image` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '品牌图片地址',
  `letter` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '品牌的首字母',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '品牌表，一个品牌下有多个商品（spu），一对多关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_brand_category
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_brand_category`;
CREATE TABLE `t_mall_goods_brand_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌id',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `brand_id` bigint(20) NOT NULL COMMENT '品牌id',
  `category_id` bigint(20) NOT NULL COMMENT '商品类目id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `key_category_id`(`category_id`) USING BTREE,
  INDEX `key_brand_id`(`brand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品分类和品牌的中间表，两者是多对多关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_category
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_category`;
CREATE TABLE `t_mall_goods_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '类目id',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类目名称',
  `parent_id` bigint(20) NOT NULL COMMENT '父类目id,顶级类目填0',
  `is_parent` tinyint(2) NOT NULL COMMENT '是否为父节点，0为否，1为是',
  `sort` tinyint(2) NOT NULL COMMENT '排序指数，越小越靠前',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_sku`;
CREATE TABLE `t_mall_goods_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `spu_id` bigint(20) NOT NULL COMMENT 'spu id',
  `title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品标题',
  `images` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '商品的图片，多个图片以‘,’分割',
  `stock` int(8) UNSIGNED NULL DEFAULT 0 COMMENT '库存',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '销售价格',
  `indexes` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '特有规格属性在spu属性模板中的对应下标组合',
  `own_spec` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'sku的特有规格参数键值对，json格式，反序列化时请使用linkedHashMap，保证有序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效，0无效，1有效',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_spu_id`(`spu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'sku表,该表表示具体的商品实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_spec_group
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_spec_group`;
CREATE TABLE `t_mall_goods_spec_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `category_id` bigint(20) NOT NULL COMMENT '商品分类id，一个分类下有多个规格组',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规格组的名称',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `key_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '规格参数的分组表，每个商品分类下有多个规格参数组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_spec_param
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_spec_param`;
CREATE TABLE `t_mall_goods_spec_param`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `category_id` bigint(20) NOT NULL COMMENT '商品分类id',
  `group_id` bigint(20) NOT NULL COMMENT '所属组的id',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '参数名',
  `numeric` tinyint(1) NOT NULL COMMENT '是否是数字类型参数，true或false',
  `unit` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数字类型参数的单位，非数字类型可以为空',
  `generic` tinyint(1) NOT NULL COMMENT '是否是sku通用属性，true或false',
  `searching` tinyint(1) NOT NULL COMMENT '是否用于搜索过滤，true或false',
  `segments` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `key_category_id`(`category_id`) USING BTREE,
  INDEX `key_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '规格参数组下的参数名' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_spu
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_spu`;
CREATE TABLE `t_mall_goods_spu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `brand_id` bigint(20) NOT NULL COMMENT '商品所属品牌id',
  `category_id` bigint(20) NOT NULL COMMENT '商品分类id',
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `sub_title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '副标题，一般是促销信息',
  `on_sale` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否上架，0下架，1上架',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '售价',
  `use_spec` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否使用规格：0=不使用，1=使用',
  `spec_groups` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品规格组',
  `goods_stock` int(11) NOT NULL DEFAULT 0 COMMENT '商品库存',
  `virtual_sales` int(11) NOT NULL DEFAULT 0 COMMENT '虚拟销售数量',
  `confine_count` int(11) NOT NULL DEFAULT -1 COMMENT '购物数量限制',
  `pieces` int(11) NOT NULL DEFAULT 0 COMMENT '满件包邮',
  `forehead` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '满额包邮',
  `freight_id` int(11) NOT NULL COMMENT '运费模板ID',
  `give_integral` int(11) NOT NULL DEFAULT 0 COMMENT '赠送积分',
  `give_integral_type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '赠送积分类型1固定值 2百分比',
  `deductible_integral` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '可抵扣积分',
  `deductible_integral_type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '可抵扣积分类型1固定值 2百分比',
  `accumulative` tinyint(2) NOT NULL DEFAULT 0 COMMENT '允许多件累计折扣 0否 1是',
  `individual_share` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否单独分销设置：0否 1是',
  `share_setting_type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '分销设置类型 0普通设置 1详细设置',
  `share_commission_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '佣金配比 0 固定金额 1 百分比',
  `membership_price` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否享受会员价购买',
  `membership_price_single` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否单独设置会员价',
  `share_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '自定义分享图片',
  `share_title` varchar(65) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '自定义分享标题',
  `is_default_services` tinyint(2) NOT NULL DEFAULT 1 COMMENT '默认服务 0否  1是',
  `sort` int(11) NOT NULL DEFAULT 100 COMMENT '排序',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_tenant_id`(`tenant_id`) USING BTREE,
  INDEX `key_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'spu表，该表描述的是一个抽象性的商品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_goods_spu_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_goods_spu_detail`;
CREATE TABLE `t_mall_goods_spu_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `spu_id` bigint(20) NOT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述信息',
  `generic_spec` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '通用规格参数数据',
  `special_spec` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '特有规格参数及可选值信息，json格式',
  `packing_list` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '包装清单',
  `after_service` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '售后服务',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_order
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_order`;
CREATE TABLE `t_mall_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '主键',
  `store_id` int(11) NOT NULL DEFAULT 0 COMMENT '店铺id',
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '订单号',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额(含运费)',
  `total_pay_price` decimal(10, 2) NOT NULL COMMENT '实际支付总费用(含运费）',
  `express_original_price` decimal(10, 2) NOT NULL COMMENT '运费',
  `express_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '修改后运费',
  `total_goods_original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单商品总金额',
  `total_goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠后订单商品总金额',
  `store_discount_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '商家改价优惠',
  `member_discount_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员优惠价格',
  `coupon_id` int(11) NULL DEFAULT NULL COMMENT '优惠券id',
  `coupon_discount_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券优惠金额',
  `integral` int(11) NULL DEFAULT NULL COMMENT '使用的积分数量',
  `integral_deduction_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '积分抵扣金额',
  `name` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '收件人姓名',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '收件人手机号',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '收件人地址',
  `comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户订单备注',
  `order_form` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '自定义表单（JSON）',
  `leaving_message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '留言',
  `store_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '商家订单备注',
  `pay_status` tinyint(2) NULL DEFAULT 0 COMMENT '是否支付：0.未支付 1.已支付',
  `pay_type` tinyint(2) NULL DEFAULT 1 COMMENT '支付方式：1.在线支付 2.货到付款 3.余额支付',
  `pay_time` timestamp(0) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '支付时间',
  `deliver_status` tinyint(2) NULL DEFAULT 0 COMMENT '是否发货：0.未发货 1.已发货',
  `deliver_time` timestamp(0) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '发货时间',
  `express` varchar(65) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '物流公司',
  `express_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '物流订单号',
  `confirm_receipt` tinyint(2) NULL DEFAULT 0 COMMENT '收货状态：0.未收货 1.已收货',
  `confirm_receipt_time` timestamp(0) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '确认收货时间',
  `cancel_status` tinyint(2) NULL DEFAULT 0 COMMENT '订单取消状态：0.未取消 1.已取消 2.申请取消',
  `cancel_time` timestamp(0) NULL DEFAULT '0000-00-00 00:00:00' COMMENT '订单取消时间',
  `recycle_status` tinyint(2) NULL DEFAULT 0 COMMENT '是否加入回收站 0.否 1.是',
  `offline` tinyint(2) NULL DEFAULT 0 COMMENT '是否到店自提：0.否 1.是',
  `offline_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '核销码',
  `verifier_id` int(11) NULL DEFAULT 0 COMMENT '核销员ID',
  `verifier_store_id` int(11) NULL DEFAULT 0 COMMENT '自提门店ID',
  `support_pay_types` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支持的支付方式',
  `evaluation_status` tinyint(2) NULL DEFAULT 0 COMMENT '是否评价 0.否 1.是',
  `evaluation_time` timestamp(0) NULL DEFAULT '0000-00-00 00:00:00',
  `after_sales_out` tinyint(2) NULL DEFAULT 0 COMMENT '是否过售后时间 0.否 1.是',
  `after_sales_status` tinyint(2) NULL DEFAULT 0 COMMENT '是否申请售后 0.否 1.是',
  `status` tinyint(2) NULL DEFAULT 1 COMMENT '订单状态 1.已完成 0.进行中',
  `auto_cancel_time` timestamp(0) NULL DEFAULT NULL COMMENT '自动取消时间',
  `auto_confirm_verifier_time` timestamp(0) NULL DEFAULT NULL COMMENT '自动确认收货时间',
  `auto_after_sales_time` timestamp(0) NULL DEFAULT NULL COMMENT '自动售后时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE,
  INDEX `INDEX_USER_ID`(`user_id`) USING BTREE,
  INDEX `INDEX_STORE_ID`(`store_id`) USING BTREE,
  INDEX `INDEX_ORDER_NO`(`order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_order_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_order_sku`;
CREATE TABLE `t_mall_order_sku`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `goods_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '购买商品id',
  `goods_sku_number` int(11) NULL DEFAULT NULL COMMENT '购买商品数量',
  `goods_sku_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `total_original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品总价',
  `total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠后商品总价',
  `member_discount_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员优惠金额',
  `store_discount_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '商家改价优惠',
  `goods_sku_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '购买商品信息',
  `refund_status` tinyint(1) NULL DEFAULT 0 COMMENT '是否退款',
  `after_sales_status` tinyint(1) NULL DEFAULT 0 COMMENT '售后状态 0--未售后 1--售后中 2--售后结束',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE,
  INDEX `INDEX_ORDER_ID`(`order_id`) USING BTREE,
  INDEX `INDEX_GOODS_SKU_ID`(`goods_sku_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_pay_record`;
CREATE TABLE `t_mall_pay_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  `amount` decimal(9, 2) NOT NULL,
  `pay_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '支付状态：0=未支付，1=已支付, 2=已退款',
  `pay_type` tinyint(2) NOT NULL DEFAULT 3 COMMENT '支付方式：1=微信支付，2=货到付款，3=余额支付，4=支付宝支付,  5=银行卡支付',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `refund` decimal(9, 2) NOT NULL DEFAULT 0.00 COMMENT '已退款金额',
  `comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_user_account
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_user_account`;
CREATE TABLE `t_mall_user_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `integral` bigint(20) NOT NULL DEFAULT 0 COMMENT '积分',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `account_status` tinyint(2) NULL DEFAULT 1 COMMENT '账户状态 \'0\'禁用，\'1\' 启用',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE,
  INDEX `INDEX_USER_ID`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户账户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mall_user_balance_record
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_user_balance_record`;
CREATE TABLE `t_mall_user_balance_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `type` tinyint(2) NOT NULL COMMENT '类型：1=收入，2=支出',
  `amount` decimal(10, 2) NOT NULL COMMENT '变动金额',
  `comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_TENANT_ID`(`tenant_id`) USING BTREE,
  INDEX `INDEX_USER_ID`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
-- Table structure for t_sys_code_generator_config
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_config`;
CREATE TABLE `t_sys_code_generator_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源',
  `module_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `module_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块代码',
  `service_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `table_alias` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表别名',
  `table_prefix` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表前缀',
  `parent_package` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级包名',
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
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成配置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sys_code_generator_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_code_generator_dict`;
CREATE TABLE `t_sys_code_generator_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '字典上级',
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
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 770 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段属性配置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '多表查询时的联合表配置' ROW_FORMAT = DYNAMIC;

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
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段校验规则配置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据权限配置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据权限多部门' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sys_dfs
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dfs`;
CREATE TABLE `t_sys_dfs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `dfs_type` bigint(20) NULL DEFAULT NULL COMMENT '分布式存储分类',
  `dfs_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分布式存储编号',
  `access_url_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件访问地址前缀',
  `upload_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分布式存储上传地址',
  `bucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '空间名称',
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用ID',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'accessKey',
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'secretKey',
  `dfs_default` tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否默认存储 0否，1是',
  `dfs_status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态 0禁用，1 启用',
  `access_control` tinyint(2) NOT NULL DEFAULT 0 COMMENT '访问控制 0私有，1公开',
  `comments` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `del_flag` tinyint(2) NULL DEFAULT 0 COMMENT '1:删除 0:不删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分布式存储配置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分布式存储文件记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict`;
CREATE TABLE `t_sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '字典上级',
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
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sys_dict_business
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dict_business`;
CREATE TABLE `t_sys_dict_business`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '字典上级',
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
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sys_district
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_district`;
CREATE TABLE `t_sys_district`  (
  `id` int(5) NULL DEFAULT NULL,
  `name` varchar(270) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` int(5) NULL DEFAULT NULL,
  `initial` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `initials` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pinyin` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `suffix` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order` int(2) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_organization`;
CREATE TABLE `t_sys_organization`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父组织id',
  `ancestors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所有上级组织id的集合，便于机构查找',
  `organization_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织类型：1：事业部  2：机构  3：盐城',
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
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
  `resource_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源路径',
  `resource_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资料链接',
  `resource_level` int(11) NULL DEFAULT NULL COMMENT '资源级别',
  `resource_show` tinyint(1) NULL DEFAULT NULL COMMENT '是否显示',
  `resource_cache` tinyint(1) NULL DEFAULT NULL COMMENT '是否缓存',
  `resource_page_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源页面名称',
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
) ENGINE = InnoDB AUTO_INCREMENT = 245 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 92 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和数据权限关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 332 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和权限关联表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信渠道表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信配置表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户信息表' ROW_FORMAT = DYNAMIC;

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
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '2' COMMENT '1 : 男，0 : 女  -1：未知',
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
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

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

SET FOREIGN_KEY_CHECKS = 1;
