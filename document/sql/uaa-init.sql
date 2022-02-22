DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL，
  `resource_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL，
  `client_secret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL，
  `scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL，
  `authorized_grant_types` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL，
  `web_server_redirect_uri` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL，
  `authorities` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL，
  `access_token_validity` int(11) NULL DEFAULT NULL，
  `refresh_token_validity` int(11) NULL DEFAULT NULL，
  `additional_information` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL，
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP，
  `archived` tinyint(1) NULL DEFAULT 0，
  `trusted` tinyint(1) NULL DEFAULT 0，
  `autoapprove` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'false'，
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

INSERT INTO `oauth_client_details` VALUES ('wanxin-p2p-web-admin'， 'wanxin-resource'， '123456'， 'read'， 'client_credentials，password，authorization_code，implicit，refresh_token'， ''， 'ROLE_ADMIN，ROLE_CONSUMER，ROLE_API'， 7200， 259200， NULL， '2019-05-07 18:17:18'， 0， 0， 'false');
INSERT INTO `oauth_client_details` VALUES ('wanxin-p2p-web-app'， 'wanxin-resource'， '123456'， 'read'， 'client_credentials，password，authorization_code，implicit，refresh_token'， ''， 'ROLE_CONSUMER，ROLE_API'， 31536000， 2592000， NULL， '2019-05-07 18:17:37'， 0， 0， 'false');
INSERT INTO `oauth_client_details` VALUES ('wanxin-p2p-web-h5'， 'wanxin-resource'， 'itcasth5'， 'read'， 'client_credentials，password，authorization_code，implicit，refresh_token'， ''， 'ROLE_CONSUMER，ROLE_API'， 31536000， 2592000， NULL， '2019-05-07 19:21:23'， 0， 0， 'false');