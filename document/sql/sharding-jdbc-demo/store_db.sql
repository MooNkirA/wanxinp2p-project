DROP DATABASE IF EXISTS `store_db`;
CREATE DATABASE `store_db` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
USE `store_db`;

DROP TABLE IF EXISTS `store_info`;
CREATE TABLE `store_info` (
    `id` BIGINT(20) NOT NULL COMMENT 'id',
    `store_name` VARCHAR(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
    `reputation` INT(11) NULL DEFAULT NULL COMMENT '信誉等级',
    `region_code` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺所在地',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

INSERT INTO `store_info` VALUES (1, '斩月铺子', 4, '110100');
INSERT INTO `store_info` VALUES (2, '斩月超市', 3, '410100');