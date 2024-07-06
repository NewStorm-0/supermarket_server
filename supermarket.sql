/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : supermarket

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 30/08/2022 19:09:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for administrator
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`  (
  `account` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员账号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for charge
-- ----------------------------
DROP TABLE IF EXISTS `charge`;
CREATE TABLE `charge`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '充值单号',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '会员用户卡号',
  `amount` double UNSIGNED NOT NULL COMMENT '充值金额',
  `time` datetime NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `charge_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`account`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for commodity
-- ----------------------------
DROP TABLE IF EXISTS `commodity`;
CREATE TABLE `commodity`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `price` double NOT NULL COMMENT '初始价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `type` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '类型',
  `relief_amount` double UNSIGNED NOT NULL COMMENT '减免金额',
  `lowest_amount` double UNSIGNED NOT NULL COMMENT '最低金额',
  `cost` int NOT NULL COMMENT '兑换所需积分',
  PRIMARY KEY (`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for membership_level
-- ----------------------------
DROP TABLE IF EXISTS `membership_level`;
CREATE TABLE `membership_level`  (
  `type` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '等级种类',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会员等级名称',
  `required_amount` double UNSIGNED NOT NULL COMMENT '需要的初始金额',
  `discount` double NOT NULL COMMENT '折扣',
  PRIMARY KEY (`type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_commodity
-- ----------------------------
DROP TABLE IF EXISTS `order_commodity`;
CREATE TABLE `order_commodity`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` int UNSIGNED NOT NULL COMMENT '订单号',
  `commodity_id` int UNSIGNED NOT NULL COMMENT '商品号',
  `quantity` int NOT NULL COMMENT '数量',
  `original_price` double NOT NULL COMMENT '原价格（折扣前的价格）',
  `actual_price` double NOT NULL COMMENT '实际价格（折扣后价格）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  INDEX `commodity_id`(`commodity_id`) USING BTREE,
  CONSTRAINT `order_commodity_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `user_order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_commodity_ibfk_2` FOREIGN KEY (`commodity_id`) REFERENCES `commodity` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for redeem
-- ----------------------------
DROP TABLE IF EXISTS `redeem`;
CREATE TABLE `redeem`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '兑换单号',
  `value` int NOT NULL COMMENT '花费积分',
  `coupon_type` int UNSIGNED NULL DEFAULT NULL COMMENT '满减券种类',
  `number` int UNSIGNED NOT NULL COMMENT '兑换数量',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '会员账号',
  `time` datetime NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `coupon_type`(`coupon_type`) USING BTREE,
  INDEX `redeem_ibfk_2`(`user_id`) USING BTREE,
  CONSTRAINT `redeem_ibfk_1` FOREIGN KEY (`coupon_type`) REFERENCES `coupon` (`type`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `redeem_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user_coupon` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `account` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '会员卡号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `mobile_phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `identity_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `balance` double NOT NULL DEFAULT 0 COMMENT '余额',
  `reward_points` int NOT NULL DEFAULT 0 COMMENT '积分',
  `level` int UNSIGNED NULL DEFAULT NULL COMMENT 'VIP等级',
  PRIMARY KEY (`account`) USING BTREE,
  INDEX `level`(`level`) USING BTREE,
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`level`) REFERENCES `membership_level` (`type`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 10000012 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` int UNSIGNED NOT NULL COMMENT '会员用户卡号',
  `coupon_type` int UNSIGNED NOT NULL COMMENT '满减券种类',
  `quantity` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '满减券数量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `coupon_type`(`coupon_type`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `user_coupon_ibfk_1` FOREIGN KEY (`coupon_type`) REFERENCES `coupon` (`type`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_coupon_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单号',
  `user_id` int UNSIGNED NULL DEFAULT NULL COMMENT '会员卡号',
  `payment_amount` double NOT NULL COMMENT '支付金额',
  `reward_points` int NOT NULL COMMENT '赠送的积分',
  `time` datetime NOT NULL COMMENT '时间',
  `coupon_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '使用的满减券信息，若为null，则未使用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `coupon_type`(`coupon_info`) USING BTREE,
  CONSTRAINT `user_order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`account`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
