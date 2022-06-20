/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : spring-boot-demo

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 12/12/2018 18:52:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sec_permission
-- ----------------------------
DROP TABLE IF EXISTS `sec_permission`;
CREATE TABLE `sec_permission`
(
  `id`         bigint(64)  NOT NULL COMMENT 'Primary key',
  `name`       varchar(50) NOT NULL COMMENT 'Permission name',
  `url`        varchar(1000) DEFAULT NULL COMMENT 'When the type is the page, it represents the front -end routing address. When the type is the button, it represents the rear port interface address',
  `type`       int(2)      NOT NULL COMMENT 'Permanent type, page-1, button-2',
  `permission` varchar(50)   DEFAULT NULL COMMENT 'Permissions expression',
  `method`     varchar(50)   DEFAULT NULL COMMENT 'Back -end interface access method',
  `sort`       int(11)     NOT NULL COMMENT 'Sort',
  `parent_id`  bigint(64)  NOT NULL COMMENT 'Parent ID',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='Permissions table';

-- ----------------------------
-- Records of sec_permission
-- ----------------------------
BEGIN;
INSERT INTO `sec_permission`
VALUES (1072806379288399872, 'Test page', '/test', 1, 'page:test', NULL, 1, 0);
INSERT INTO `sec_permission`
VALUES (1072806379313565696, 'Test page-query', '/**/test', 2, 'btn:test:query', 'GET', 1, 1072806379288399872);
INSERT INTO `sec_permission`
VALUES (1072806379330342912, 'Test page-Add', '/**/test', 2, 'btn:test:insert', 'POST', 2, 1072806379288399872);
INSERT INTO `sec_permission`
VALUES (1072806379342925824, 'Surveillance online user page', '/monitor', 1, 'page:monitor:online', NULL, 2, 0);
INSERT INTO `sec_permission`
VALUES (1072806379363897344, 'Online User Page-Query', '/**/api/monitor/online/user', 2, 'btn:monitor:online:query', 'GET', 1,
        1072806379342925824);
INSERT INTO `sec_permission`
VALUES (1072806379384868864, 'Online user page-kick out', '/**/api/monitor/online/user/kickout', 2, 'btn:monitor:online:kickout',
        'DELETE', 2, 1072806379342925824);
COMMIT;

-- ----------------------------
-- Table structure for sec_role
-- ----------------------------
DROP TABLE IF EXISTS `sec_role`;
CREATE TABLE `sec_role`
(
  `id`          bigint(64)  NOT NULL COMMENT 'Primary key',
  `name`        varchar(50) NOT NULL COMMENT 'character name',
  `description` varchar(100) DEFAULT NULL COMMENT 'describe',
  `create_time` bigint(13)  NOT NULL COMMENT 'Creation time',
  `update_time` bigint(13)  NOT NULL COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='角色表';

-- ----------------------------
-- Records of sec_role
-- ----------------------------
BEGIN;
INSERT INTO `sec_role`
VALUES (1072806379208708096, 'Administrator ',' Super Administrator', 1544611947239, 1544611947239);
INSERT INTO `sec_role`
VALUES (1072806379238068224, 'Ordinary user ',' ordinary users', 1544611947246, 1544611947246);
COMMIT;

-- ----------------------------
-- Table structure for sec_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sec_role_permission`;
CREATE TABLE `sec_role_permission`
(
  `role_id`       bigint(64) NOT NULL COMMENT 'Character key',
  `permission_id` bigint(64) NOT NULL COMMENT 'Permanent key',
  PRIMARY KEY (`role_id`, `permission_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='Role permission relationship table';

-- ----------------------------
-- Records of sec_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `sec_role_permission`
VALUES (1072806379208708096, 1072806379288399872);
INSERT INTO `sec_role_permission`
VALUES (1072806379208708096, 1072806379313565696);
INSERT INTO `sec_role_permission`
VALUES (1072806379208708096, 1072806379330342912);
INSERT INTO `sec_role_permission`
VALUES (1072806379208708096, 1072806379342925824);
INSERT INTO `sec_role_permission`
VALUES (1072806379208708096, 1072806379363897344);
INSERT INTO `sec_role_permission`
VALUES (1072806379208708096, 1072806379384868864);
INSERT INTO `sec_role_permission`
VALUES (1072806379238068224, 1072806379288399872);
INSERT INTO `sec_role_permission`
VALUES (1072806379238068224, 1072806379313565696);
COMMIT;

-- ----------------------------
-- Table structure for sec_user
-- ----------------------------
DROP TABLE IF EXISTS `sec_user`;
CREATE TABLE `sec_user`
(
`ID` Bigint (64) Not Null Comment 'Primary Key', 
   `username` varchar (50) not null comments 'username', 
   `Password` varchar (60) Not null comments 'password', 
   `nickName` varchar (255) default null comments 'nickname', 
   `Phone` varchar (11) default null comments 'mobile', 
   `email` varchar (50) default null comments 'mailbox', 
   `BIRTHDAY` BIGINT (13) Default NULL Comment 'Birthday', 
   `sex` int (2) default null comments, male -1, female -2 ', 
   `Status` int (2) Not null default '1' comments 'state, enable -1, disable -0', 
   `Create_time` Bigint (13) NOT NULL Comment 'Create time', 
   `Update_time` Bigint (13) NOT NULL Comment 'Update Time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户表';

-- ----------------------------
-- Records of sec_user
-- ----------------------------
BEGIN;
INSERT INTO `sec_user`
VALUES (1072806377661009920, 'admin', '$2a$10$64iuSLkKNhpTN19jGHs7xePvFsub7ZCcCmBqEYw8fbACGTE3XetYq', '管理员',
        '17300000000', 'admin@xkcoding.com', 785433600000, 1, 1, 1544611947032, 1544611947032);
INSERT INTO `sec_user`
VALUES (1072806378780889088, 'user', '$2a$10$OUDl4thpcHfs7WZ1kMUOb.ZO5eD4QANW5E.cexBLiKDIzDNt87QbO', '普通用户',
        '17300001111', 'user@xkcoding.com', 785433600000, 1, 1, 1544611947234, 1544611947234);
COMMIT;

-- ----------------------------
-- Table structure for sec_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sec_user_role`;
CREATE TABLE `sec_user_role`
(
  `user_id` bigint(64) NOT NULL COMMENT '用户主键',
  `role_id` bigint(64) NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户角色关系表';

-- ----------------------------
-- Records of sec_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sec_user_role`
VALUES (1072806377661009920, 1072806379208708096);
INSERT INTO `sec_user_role`
VALUES (1072806378780889088, 1072806379238068224);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
