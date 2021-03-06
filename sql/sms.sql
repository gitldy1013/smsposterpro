/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.31 : Database - sms
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sms` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `sms`;

/*Table structure for table `sms_msg` */

DROP TABLE IF EXISTS `sms_msg`;

CREATE TABLE `sms_msg` (
  `id` int(12) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `send_phone_num` varchar(64) DEFAULT NULL COMMENT '发送号码',
  `rec_phone` varchar(20000) DEFAULT NULL COMMENT '接受号码',
  `context` varchar(255) DEFAULT NULL COMMENT '发送内容',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
