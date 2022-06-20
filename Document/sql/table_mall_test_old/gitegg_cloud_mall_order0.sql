/*
 Navicat Premium Data Transfer

 Source Server         : 测试
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : 172.16.20.188:3306
 Source Schema         : gitegg_cloud_mall_order0

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : 65001

 Date: 25/04/2021 17:31:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mall_order0
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_order0`;
CREATE TABLE `t_mall_order0`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 593126633746444289 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mall_order1
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_order1`;
CREATE TABLE `t_mall_order1`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 593115830582489090 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mall_order_sku0
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_order_sku0`;
CREATE TABLE `t_mall_order_sku0`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 593127675888381953 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_mall_order_sku1
-- ----------------------------
DROP TABLE IF EXISTS `t_mall_order_sku1`;
CREATE TABLE `t_mall_order_sku1`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 593115831450710018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
