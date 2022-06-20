/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : gitegg_cloud_mall

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 20/06/2022 16:57:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
-- Records of t_mall_goods_brand
-- ----------------------------

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
-- Records of t_mall_goods_brand_category
-- ----------------------------

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
-- Records of t_mall_goods_category
-- ----------------------------

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
-- Records of t_mall_goods_sku
-- ----------------------------
INSERT INTO `t_mall_goods_sku` VALUES (1, 0, 1, '123123', '123123', 8640, 10.00, '', '', 1, NULL, NULL, NULL, NULL, 0);
INSERT INTO `t_mall_goods_sku` VALUES (2, 0, 2, '234234', '23423', 8368, 23.00, '', '', 1, NULL, NULL, NULL, NULL, 0);

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
-- Records of t_mall_goods_spec_group
-- ----------------------------

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
-- Records of t_mall_goods_spec_param
-- ----------------------------

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
-- Records of t_mall_goods_spu
-- ----------------------------

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
-- Records of t_mall_goods_spu_detail
-- ----------------------------

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
-- Records of t_mall_order
-- ----------------------------
INSERT INTO `t_mall_order` VALUES (1, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-24 21:29:48', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (2, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-24 21:31:35', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (3, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-24 21:33:19', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (4, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-29 11:12:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (5, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-29 11:14:22', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (6, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-29 11:25:19', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (7, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-29 11:46:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (8, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-29 18:47:22', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (9, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 18:32:52', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (10, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 18:41:44', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (11, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 18:50:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (12, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 19:27:31', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (13, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 19:27:34', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (14, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 19:27:36', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (15, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 19:27:37', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (16, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 19:27:38', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (17, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-30 19:31:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (18, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-03-31 11:45:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (22, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 17:08:16', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (23, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 17:08:52', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (24, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 17:28:04', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (25, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 17:34:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (26, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 17:57:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (27, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 17:59:49', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (28, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 18:20:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (29, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 18:33:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (30, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 18:34:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (31, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 18:57:46', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (32, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 18:58:27', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (33, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 18:59:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (34, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 18:59:08', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (35, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:00:57', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (36, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:00:59', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (37, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:01:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (38, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:01:12', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (39, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:01:13', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (40, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:05:45', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (41, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:12:08', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (42, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-02 19:15:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (43, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-20 15:05:31', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (44, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-20 15:11:47', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (45, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-20 15:52:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order` VALUES (46, 0, 1, 0, '', 188.00, 188.00, 188.00, NULL, NULL, NULL, 0.00, NULL, NULL, NULL, NULL, NULL, '', '', '', '', NULL, '', '', 0, 1, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', '', '', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00', 0, 0, '', 0, 0, NULL, 0, '0000-00-00 00:00:00', 0, 0, 1, NULL, NULL, NULL, '2021-04-20 17:12:33', 1, NULL, NULL, 0);

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
-- Records of t_mall_order_sku
-- ----------------------------
INSERT INTO `t_mall_order_sku` VALUES (1, 0, 3, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-24 21:33:19', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (2, 0, 3, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-24 21:33:19', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (3, 0, 4, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:12:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (4, 0, 4, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:12:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (5, 0, 5, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:14:22', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (6, 0, 5, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:14:22', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (7, 0, 6, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:25:19', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (8, 0, 6, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:25:19', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (9, 0, 7, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:46:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (10, 0, 7, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 11:46:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (11, 0, 8, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 18:47:22', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (12, 0, 8, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-29 18:47:22', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (13, 0, 9, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 18:32:52', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (14, 0, 9, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 18:32:52', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (15, 0, 10, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 18:41:44', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (16, 0, 10, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 18:41:44', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (17, 0, 11, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 18:50:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (18, 0, 11, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 18:50:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (19, 0, 12, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:31', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (20, 0, 12, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:31', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (21, 0, 13, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:34', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (22, 0, 13, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:34', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (23, 0, 14, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:36', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (24, 0, 14, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:36', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (25, 0, 15, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:37', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (26, 0, 15, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:37', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (27, 0, 16, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:38', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (28, 0, 16, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:27:38', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (29, 0, 17, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:31:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (30, 0, 17, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-30 19:31:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (31, 0, 18, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-31 11:45:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (32, 0, 18, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-03-31 11:45:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (39, 0, 22, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:08:16', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (40, 0, 22, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:08:16', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (41, 0, 23, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:08:52', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (42, 0, 23, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:08:52', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (43, 0, 24, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:28:04', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (44, 0, 24, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:28:04', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (45, 0, 25, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:34:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (46, 0, 25, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:34:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (47, 0, 26, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:57:06', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (48, 0, 26, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:57:06', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (49, 0, 27, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:59:49', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (50, 0, 27, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 17:59:49', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (51, 0, 28, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:20:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (52, 0, 28, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:20:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (53, 0, 29, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:33:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (54, 0, 29, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:33:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (55, 0, 30, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:34:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (56, 0, 30, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:34:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (57, 0, 31, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:57:46', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (58, 0, 31, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:57:46', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (59, 0, 32, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:58:27', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (60, 0, 32, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:58:27', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (61, 0, 33, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:59:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (62, 0, 33, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:59:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (63, 0, 34, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:59:08', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (64, 0, 34, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 18:59:08', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (65, 0, 35, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:00:57', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (66, 0, 35, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:00:57', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (67, 0, 36, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:00:59', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (68, 0, 36, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:00:59', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (69, 0, 37, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:01:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (70, 0, 37, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:01:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (71, 0, 38, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:01:12', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (72, 0, 38, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:01:12', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (73, 0, 39, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:01:13', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (74, 0, 39, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:01:13', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (75, 0, 40, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:05:45', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (76, 0, 40, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:05:45', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (77, 0, 41, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:12:08', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (78, 0, 41, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:12:08', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (79, 0, 42, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:15:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (80, 0, 42, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-02 19:15:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (81, 0, 43, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 15:05:33', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (82, 0, 43, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 15:05:33', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (83, 0, 44, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 15:11:48', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (84, 0, 44, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 15:11:48', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (85, 0, 45, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 15:52:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (86, 0, 45, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 15:52:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (87, 0, 46, 1, 5, 10.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 17:12:33', 1, NULL, NULL, 0);
INSERT INTO `t_mall_order_sku` VALUES (88, 0, 46, 2, 6, 23.00, NULL, NULL, NULL, 0.00, NULL, 0, 0, '2021-04-20 17:12:33', 1, NULL, NULL, 0);

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
-- Records of t_mall_pay_record
-- ----------------------------
INSERT INTO `t_mall_pay_record` VALUES (1, 0, 1, '0', 100.00, 1, 5, '', 0.00, '', '2021-03-19 16:36:11', 1, NULL, NULL, 0);
INSERT INTO `t_mall_pay_record` VALUES (2, 0, 1, '0', 100.00, 1, 5, '', 0.00, '', '2021-04-02 19:11:12', 1, NULL, NULL, 0);
INSERT INTO `t_mall_pay_record` VALUES (3, 0, 1, '0', 188.00, 1, 5, '', 0.00, '', '2021-04-20 16:00:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_pay_record` VALUES (4, 0, 1, '0', 188.00, 1, 5, '', 0.00, '', '2021-04-20 16:57:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_pay_record` VALUES (5, 0, 1, '0', 188.00, 1, 5, '', 0.00, '', '2021-04-20 17:08:01', 1, NULL, NULL, 0);
INSERT INTO `t_mall_pay_record` VALUES (6, 0, 1, '0', 188.00, 1, 5, '', 0.00, '', '2021-04-20 17:12:33', 1, NULL, NULL, 0);

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
-- Records of t_mall_user_account
-- ----------------------------
INSERT INTO `t_mall_user_account` VALUES (1, 0, 1, 1000, 1019.80, 1, NULL, '2021-03-19 14:26:48', 1, '2021-04-20 17:12:33', 1, 0);

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
-- Records of t_mall_user_balance_record
-- ----------------------------
INSERT INTO `t_mall_user_balance_record` VALUES (1, 0, 1, 2, 100.00, '', '2021-03-19 16:32:59', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (2, 0, 1, 2, 100.00, '', '2021-03-19 16:33:19', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (3, 0, 1, 2, 100.00, '', '2021-03-19 16:33:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (4, 0, 1, 2, 100.00, '', '2021-03-19 16:33:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (5, 0, 1, 2, 100.00, '', '2021-03-19 16:33:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (6, 0, 1, 2, 100.00, '', '2021-03-19 16:33:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (7, 0, 1, 2, 100.00, '', '2021-03-19 16:33:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (8, 0, 1, 2, 100.00, '', '2021-03-19 16:34:51', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (9, 0, 1, 2, 100.00, '', '2021-03-19 16:36:09', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (10, 0, 1, 2, 23.01, '', '2021-03-29 11:51:30', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (11, 0, 1, 2, 23.01, '', '2021-03-29 11:51:32', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (12, 0, 1, 2, 23.01, '', '2021-03-29 11:51:32', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (13, 0, 1, 2, 23.01, '', '2021-03-29 11:51:33', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (14, 0, 1, 2, 23.01, '', '2021-03-29 11:51:34', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (15, 0, 1, 2, 23.01, '', '2021-03-29 11:51:34', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (16, 0, 1, 2, 23.01, '', '2021-03-29 11:51:35', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (17, 0, 1, 2, 23.01, '', '2021-03-29 11:51:35', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (18, 0, 1, 2, 23.01, '', '2021-03-29 11:51:35', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (19, 0, 1, 2, 23.01, '', '2021-03-29 11:51:36', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (20, 0, 1, 2, 23.01, '', '2021-03-29 11:51:36', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (21, 0, 1, 2, 23.01, '', '2021-03-29 11:51:36', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (22, 0, 1, 2, 23.01, '', '2021-03-29 11:52:00', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (23, 0, 1, 2, 23.01, '', '2021-03-29 11:52:01', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (24, 0, 1, 2, 23.01, '', '2021-03-29 11:52:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (25, 0, 1, 2, 23.01, '', '2021-03-29 11:52:07', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (26, 0, 1, 2, 23.01, '', '2021-03-29 11:56:57', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (27, 0, 1, 2, 23.01, '', '2021-03-29 11:57:34', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (28, 0, 1, 2, 188.00, '', '2021-03-31 11:45:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (29, 0, 1, 2, 188.00, '', '2021-04-01 09:43:14', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (30, 0, 1, 2, 188.00, '', '2021-04-01 09:44:51', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (31, 0, 1, 2, 188.00, '', '2021-04-01 09:47:47', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (32, 0, 1, 2, 188.00, '', '2021-04-01 09:56:12', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (33, 0, 1, 2, 188.00, '', '2021-04-01 11:07:43', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (34, 0, 1, 2, 188.00, '', '2021-04-01 11:09:50', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (35, 0, 1, 2, 188.00, '', '2021-04-01 11:16:36', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (36, 0, 1, 2, 188.00, '', '2021-04-01 15:42:24', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (37, 0, 1, 2, 188.00, '', '2021-04-01 18:14:54', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (38, 0, 1, 2, 188.00, '', '2021-04-01 18:25:10', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (39, 0, 1, 2, 188.00, '', '2021-04-01 18:31:49', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (40, 0, 1, 2, 188.00, '', '2021-04-01 18:43:54', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (41, 0, 1, 2, 188.00, '', '2021-04-01 18:55:48', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (42, 0, 1, 2, 188.00, '', '2021-04-02 11:12:52', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (43, 0, 1, 2, 188.00, '', '2021-04-02 11:59:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (44, 0, 1, 2, 188.00, '', '2021-04-02 15:36:21', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (45, 0, 1, 2, 188.00, '', '2021-04-02 16:54:32', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (46, 0, 1, 2, 188.00, '', '2021-04-02 17:08:11', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (47, 0, 1, 2, 188.00, '', '2021-04-02 17:08:13', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (48, 0, 1, 2, 188.00, '', '2021-04-02 17:08:51', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (49, 0, 1, 2, 188.00, '', '2021-04-02 17:08:51', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (50, 0, 1, 2, 188.00, '', '2021-04-02 17:28:03', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (51, 0, 1, 2, 188.00, '', '2021-04-02 17:28:03', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (52, 0, 1, 2, 188.00, '', '2021-04-02 17:34:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (53, 0, 1, 2, 188.00, '', '2021-04-02 17:34:17', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (54, 0, 1, 2, 188.00, '', '2021-04-02 17:57:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (55, 0, 1, 2, 188.00, '', '2021-04-02 17:57:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (56, 0, 1, 2, 188.00, '', '2021-04-02 17:59:48', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (57, 0, 1, 2, 188.00, '', '2021-04-02 17:59:48', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (58, 0, 1, 2, 188.00, '', '2021-04-02 18:20:22', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (59, 0, 1, 2, 188.00, '', '2021-04-02 18:20:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (60, 0, 1, 2, 188.00, '', '2021-04-02 18:33:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (61, 0, 1, 2, 188.00, '', '2021-04-02 18:33:15', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (62, 0, 1, 2, 188.00, '', '2021-04-02 18:34:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (63, 0, 1, 2, 188.00, '', '2021-04-02 18:34:23', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (64, 0, 1, 2, 100.00, '', '2021-04-02 19:10:56', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (65, 0, 1, 2, 188.00, '', '2021-04-20 16:00:02', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (66, 0, 1, 2, 188.00, '', '2021-04-20 16:57:05', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (67, 0, 1, 2, 188.00, '', '2021-04-20 17:07:56', 1, NULL, NULL, 0);
INSERT INTO `t_mall_user_balance_record` VALUES (68, 0, 1, 2, 188.00, '', '2021-04-20 17:12:33', 1, NULL, NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
