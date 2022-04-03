DROP DATABASE IF EXISTS `product_db_2`;
CREATE DATABASE `product_db_2` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
USE `product_db_2`;

DROP TABLE IF EXISTS `product_descript_1`;
CREATE TABLE `product_descript_1` (
	`id` BIGINT(20) NOT NULL COMMENT 'id',
	`product_info_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属商品id',
	`descript` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述',
	`store_info_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属店铺id',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `FK_Reference_2`(`product_info_id`) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `product_descript_2`;
CREATE TABLE `product_descript_2` (
	`id` BIGINT(20) NOT NULL COMMENT 'id',
	`product_info_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属商品id',
	`descript` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述',
	`store_info_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属店铺id',
	INDEX `FK_Reference_2`(`product_info_id`) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `product_info_1`;
CREATE TABLE `product_info_1` (
	`product_info_id` BIGINT(20) NOT NULL COMMENT 'id',
	`store_info_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属店铺id',
	`product_name` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
	`spec` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
	`region_code` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产地',
	`price` DECIMAL(10, 0) NULL DEFAULT NULL COMMENT '商品价格',
	`image_url` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
	PRIMARY KEY (`product_info_id`) USING BTREE,
	INDEX `FK_Reference_1`(`store_info_id`) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `product_info_2`;
CREATE TABLE `product_info_2` (
	`product_info_id` BIGINT(20) NOT NULL COMMENT 'id',
	`store_info_id` BIGINT(20) NULL DEFAULT NULL COMMENT '所属店铺id',
	`product_name` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
	`spec` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
	`region_code` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产地',
	`price` DECIMAL(10, 0) NULL DEFAULT NULL COMMENT '商品价格',
	`image_url` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
	PRIMARY KEY (`product_info_id`) USING BTREE,
	INDEX `FK_Reference_1`(`store_info_id`) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;