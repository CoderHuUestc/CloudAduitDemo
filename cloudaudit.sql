/*
Navicat MySQL Data Transfer

Source Server         : MyFirstWeb
Source Server Version : 50722
Source Host           : 39.108.10.155:3306
Source Database       : cloudaudit

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-02-28 14:36:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ca_file
-- ----------------------------
DROP TABLE IF EXISTS `ca_file`;
CREATE TABLE `ca_file` (
  `file_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  `file_type` varchar(255) NOT NULL,
  `file_md5` varchar(255) NOT NULL,
  `upload_time` datetime NOT NULL,
  `file_route` varchar(255) NOT NULL,
  `status` smallint(6) unsigned NOT NULL COMMENT '文件的状态，上传完成则0，未完成则1',
  `total_pieces` int(10) unsigned NOT NULL,
  PRIMARY KEY (`file_id`),
  UNIQUE KEY `INDEX_MD5_SELECT` (`file_md5`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ca_file
-- ----------------------------
INSERT INTO `ca_file` VALUES ('1', '(中文)零基础深度学习', 'pdf', 'Scci0BmqpMO1FOI+c+A4gw==', '2019-02-28 14:26:09', 'D:\\TEMP_FILE\\ProxySigner\\Scci0BmqpMO1FOI+c+A4gw==', '0', '1');
INSERT INTO `ca_file` VALUES ('2', '[程序员的思维修炼：开发认知潜能的九堂课（中文版）].(Andy.Hunt).崔康.扫描版', 'pdf', 'vX3rQsFpMQk8ya+k6R9aLA==', '2019-02-28 14:26:10', 'D:\\TEMP_FILE\\ProxySigner\\vX3rQsFpMQk8ya+k6R9aLA==', '0', '1');
INSERT INTO `ca_file` VALUES ('3', '[教程] 深度学习 (Bengio & Goodfellow)', 'pdf', 'm6msCmix3fnoNxVqGndXoA==', '2019-02-28 14:26:11', 'D:\\TEMP_FILE\\ProxySigner\\m6msCmix3fnoNxVqGndXoA==', '0', '1');
INSERT INTO `ca_file` VALUES ('4', '《Hadoop核心指南》第三版', 'pdf', 'fj6X0DHl4Q57oAFHwU742w==', '2019-02-28 14:26:13', 'D:\\TEMP_FILE\\ProxySigner\\fj6X0DHl4Q57oAFHwU742w==', '0', '2');
INSERT INTO `ca_file` VALUES ('5', '《Java核心技术 卷II 高级特性(原书第9版)》（完整中文版）', 'pdf', '0AkzqzhAQuck237QlIc8IQ==', '2019-02-28 14:26:16', 'D:\\TEMP_FILE\\ProxySigner\\0AkzqzhAQuck237QlIc8IQ==', '0', '5');
INSERT INTO `ca_file` VALUES ('6', '《Maven权威指南》(中文)', 'pdf', 'VQekdO5RmLa4wuvbDUmaGg==', '2019-02-28 14:26:17', 'D:\\TEMP_FILE\\ProxySigner\\VQekdO5RmLa4wuvbDUmaGg==', '0', '1');
INSERT INTO `ca_file` VALUES ('7', '《鸟哥的Linux私房菜》', 'pdf', 'WFp/1xt9Xxe8f8NveHmzZw==', '2019-02-28 14:26:18', 'D:\\TEMP_FILE\\ProxySigner\\WFp\\1xt9Xxe8f8NveHmzZw==', '0', '2');
INSERT INTO `ca_file` VALUES ('8', 'flipped', 'txt', 'wLjX4OjPfRMnK3D2QK6cFw==', '2019-02-28 14:26:19', 'D:\\TEMP_FILE\\ProxySigner\\wLjX4OjPfRMnK3D2QK6cFw==', '0', '1');
INSERT INTO `ca_file` VALUES ('9', '神经网络设计', 'pdf', 'iqBEEY4FQoIlF2/GJUqYTw==', '2019-02-28 14:26:19', 'D:\\TEMP_FILE\\ProxySigner\\iqBEEY4FQoIlF2\\GJUqYTw==', '0', '1');
INSERT INTO `ca_file` VALUES ('10', '实践论', 'pdf', 'wVdjMEHq9uSgbrc89h6iYw==', '2019-02-28 14:26:20', 'D:\\TEMP_FILE\\ProxySigner\\wVdjMEHq9uSgbrc89h6iYw==', '0', '1');
INSERT INTO `ca_file` VALUES ('11', '凸优化——影印版', 'pdf', 'tIFKvxLmBqRduXt+/Q+Icw==', '2019-02-28 14:26:21', 'D:\\TEMP_FILE\\ProxySigner\\tIFKvxLmBqRduXt+\\Q+Icw==', '0', '2');
INSERT INTO `ca_file` VALUES ('12', '图灵程序设计丛书 算法 第4版', 'pdf', 'lXWnpGxT2K1MIEoJ5tSNkA==', '2019-02-28 14:26:24', 'D:\\TEMP_FILE\\ProxySigner\\lXWnpGxT2K1MIEoJ5tSNkA==', '0', '3');
INSERT INTO `ca_file` VALUES ('13', '微信公众平台企业应用开发实战', 'pdf', 'un6y0aLrTKVUb0VdyhtROw==', '2019-02-28 14:26:25', 'D:\\TEMP_FILE\\ProxySigner\\un6y0aLrTKVUb0VdyhtROw==', '0', '2');
INSERT INTO `ca_file` VALUES ('14', '学习vi和vim编辑器_第7版', 'pdf', 'bcZcf+43ACWqgi2ebpGV8A==', '2019-02-28 14:26:26', 'D:\\TEMP_FILE\\ProxySigner\\bcZcf+43ACWqgi2ebpGV8A==', '0', '1');

-- ----------------------------
-- Table structure for ca_user
-- ----------------------------
DROP TABLE IF EXISTS `ca_user`;
CREATE TABLE `ca_user` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ca_user
-- ----------------------------
INSERT INTO `ca_user` VALUES ('1', 'admin', 'ICy5YqxZB1uWSwcVLSNLcA==');

-- ----------------------------
-- Table structure for ca_user_file
-- ----------------------------
DROP TABLE IF EXISTS `ca_user_file`;
CREATE TABLE `ca_user_file` (
  `user_id` int(10) unsigned NOT NULL,
  `file_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`file_id`),
  KEY `REF_UF_FILE` (`file_id`),
  CONSTRAINT `REF_UF_FILE` FOREIGN KEY (`file_id`) REFERENCES `ca_file` (`file_id`),
  CONSTRAINT `REF_UF_USER` FOREIGN KEY (`user_id`) REFERENCES `ca_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ca_user_file
-- ----------------------------
INSERT INTO `ca_user_file` VALUES ('1', '1');
INSERT INTO `ca_user_file` VALUES ('1', '2');
INSERT INTO `ca_user_file` VALUES ('1', '3');
INSERT INTO `ca_user_file` VALUES ('1', '4');
INSERT INTO `ca_user_file` VALUES ('1', '5');
INSERT INTO `ca_user_file` VALUES ('1', '6');
INSERT INTO `ca_user_file` VALUES ('1', '7');
INSERT INTO `ca_user_file` VALUES ('1', '8');
INSERT INTO `ca_user_file` VALUES ('1', '9');
INSERT INTO `ca_user_file` VALUES ('1', '10');
INSERT INTO `ca_user_file` VALUES ('1', '11');
INSERT INTO `ca_user_file` VALUES ('1', '12');
INSERT INTO `ca_user_file` VALUES ('1', '13');
INSERT INTO `ca_user_file` VALUES ('1', '14');

-- ----------------------------
-- Table structure for file_blocks
-- ----------------------------
DROP TABLE IF EXISTS `file_blocks`;
CREATE TABLE `file_blocks` (
  `file_id` int(10) unsigned NOT NULL,
  `file_block_index` int(10) unsigned NOT NULL,
  PRIMARY KEY (`file_id`,`file_block_index`),
  CONSTRAINT `REF_BLOCK_FILEID` FOREIGN KEY (`file_id`) REFERENCES `ca_file` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file_blocks
-- ----------------------------
INSERT INTO `file_blocks` VALUES ('1', '0');
INSERT INTO `file_blocks` VALUES ('2', '0');
INSERT INTO `file_blocks` VALUES ('3', '0');
INSERT INTO `file_blocks` VALUES ('4', '0');
INSERT INTO `file_blocks` VALUES ('4', '1');
INSERT INTO `file_blocks` VALUES ('5', '0');
INSERT INTO `file_blocks` VALUES ('5', '1');
INSERT INTO `file_blocks` VALUES ('5', '2');
INSERT INTO `file_blocks` VALUES ('5', '3');
INSERT INTO `file_blocks` VALUES ('5', '4');
INSERT INTO `file_blocks` VALUES ('6', '0');
INSERT INTO `file_blocks` VALUES ('7', '0');
INSERT INTO `file_blocks` VALUES ('7', '1');
INSERT INTO `file_blocks` VALUES ('8', '0');
INSERT INTO `file_blocks` VALUES ('9', '0');
INSERT INTO `file_blocks` VALUES ('10', '0');
INSERT INTO `file_blocks` VALUES ('11', '0');
INSERT INTO `file_blocks` VALUES ('11', '1');
INSERT INTO `file_blocks` VALUES ('12', '0');
INSERT INTO `file_blocks` VALUES ('12', '1');
INSERT INTO `file_blocks` VALUES ('12', '2');
INSERT INTO `file_blocks` VALUES ('13', '0');
INSERT INTO `file_blocks` VALUES ('13', '1');
INSERT INTO `file_blocks` VALUES ('14', '0');
