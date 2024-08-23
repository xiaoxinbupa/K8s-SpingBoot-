-- MySQL dump 10.13  Distrib 5.7.20-19, for Linux (x86_64)
--
-- Host: localhost    Database: cs_mall_passport
-- ------------------------------------------------------
-- Server version	5.7.20-19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*!50717 SELECT COUNT(*) INTO @rocksdb_has_p_s_session_variables FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'performance_schema' AND TABLE_NAME = 'session_variables' */;
/*!50717 SET @rocksdb_get_is_supported = IF (@rocksdb_has_p_s_session_variables, 'SELECT COUNT(*) INTO @rocksdb_is_supported FROM performance_schema.session_variables WHERE VARIABLE_NAME=\'rocksdb_bulk_load\'', 'SELECT 0') */;
/*!50717 PREPARE s FROM @rocksdb_get_is_supported */;
/*!50717 EXECUTE s */;
/*!50717 DEALLOCATE PREPARE s */;
/*!50717 SET @rocksdb_enable_bulk_load = IF (@rocksdb_is_supported, 'SET SESSION rocksdb_bulk_load = 1', 'SET @rocksdb_dummy_bulk_load = 0') */;
/*!50717 PREPARE s FROM @rocksdb_enable_bulk_load */;
/*!50717 EXECUTE s */;
/*!50717 DEALLOCATE PREPARE s */;

--
-- Table structure for table `cmp_admin`
--

DROP TABLE IF EXISTS `cmp_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmp_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `nickname` varchar(16) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT '1',
  `enable` tinyint(1) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `avator` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cmp_admin`
--

LOCK TABLES `cmp_admin` WRITE;
/*!40000 ALTER TABLE `cmp_admin` DISABLE KEYS */;
INSERT INTO `cmp_admin` VALUES (1,'liyuan2','$e21b0ba4dcc.3a944f9.b6aa5097a4bdbfe65962e00c5b7dfb2509f783893','nickname2','16639644272','1826265849@qq.com',1,1,'2022-10-21 19:38:00','2022-11-24 20:59:33',NULL),(12,'liyuan','$e21y0babdac6319c4f90b.af5d95a4b9b8e25b64ec075975f42.08fe8e803','liyuan','16639644277','1826265847@qq.com',1,1,'2022-12-10 14:45:01','2022-12-10 14:45:01',NULL),(13,'xiao','$220a2fceb3946d2eaecd509e00755.ba95694ab80e77105e27d.273b4ab57','asldf','1231231','sdafdsf',1,0,'2022-12-28 20:28:07','2022-12-28 20:28:07','asfsdf'),(14,'er','$220y2ecdbd9d6220a1c152940d7.54b29e6d4bb90c7a1.592ddd273642bc7','asldf','12','123',1,0,'2022-12-28 20:33:02','2022-12-28 20:33:02','asfsdf'),(15,'rqwe','$220y2cc1b6956229a7c.5493087c54bd9.664bb5037a105125da213848b67','asldf','13212','12sdfs3',1,0,'2022-12-28 20:41:56','2022-12-28 20:41:56','asfsdf'),(16,'xiao1','$220b2dc6b49e6426aac45b980d7754b.9d6549bd03751c5e25d02c3d47b87','12312','12321312','12312',1,1,'2022-12-29 15:15:33','2022-12-29 15:15:33','12312'),(17,'','$d24b17d581c2df9b8afa0a0fbb25044.e291880d0f93958.e1ccf68841217','','','',1,0,'2023-01-03 19:09:40','2023-01-03 19:09:40',''),(18,'test001','$e21y09afd9cf389c4597b7a85a9.a4bdb8e85962e900527bff2d08fa8f8b3','test001','12345678901','test001@tedu.cn',1,0,'2023-01-03 19:11:49','2023-01-03 19:11:49',''),(19,'test002','$e21y03a.ddcd399c4898b4a5519ca4b0beec5b6ee702547ffc2009fb8.863','test002','12345678902','test002@tedu.cn',1,1,'2023-01-03 19:13:49','2023-01-03 19:13:49',''),(20,'test003','$e21b09a7ddc3399.4198baaa599fa4beb0e15862e600537bfd2a0.f58a8f3','test003-name','15201408096','1',1,1,'2023-01-16 13:57:14','2023-01-16 13:57:14',''),(21,'test004','$e21b09a7ddc3399.4198baaa599fa4beb0e15862e600537bfd2a0.f58a8f3','演示帐号1','123123','dsf32',1,1,'2023-01-16 14:19:09','2023-01-16 14:19:09','123'),(22,'test005','$e21y06a2d6cc3.9b479ebaa65992a.babfe95b6de20b5b73fe2f01fc8d833','123','15201408095','12312311',1,1,'2023-01-16 16:24:00','2023-01-16 16:24:00',''),(23,'test008','$e21b0da0d0c734984e96bdab559ba4b4b.ef5961e60a5a73ff2803fe8.843','test008','15201408097','15201408097@qq.cc',1,1,'2023-01-16 17:30:06','2023-01-16 17:30:06',NULL),(24,'test01','$e21y00a7d4c835954691b9a35f9ea4bbb6e05b67ea0.587cf.2408f488863','cedar','15510593332','zhangxs@tedu.cn',1,1,'2023-01-17 11:22:38','2023-01-17 11:22:38',NULL),(25,'test006','$e21b09a7d7c13e9d4.91bca9589ca4b7bdeb5a6eee025874fc2f0.f683863','test','123','12323',1,1,'2023-01-17 18:44:16','2023-01-17 18:44:16','123'),(26,'admin','$e21y07a9dbc03a9d4595b7ac579ca4b2b8ec5.6.e3055f77ff2307fa8d8f3','admin','12345678900','1234567890@qq.com',1,1,'2023-01-29 14:24:23','2023-01-29 14:24:23',NULL),(27,'111111','$926yee7c90271286906b52e9b1712.ce9c2eaa55489dddd752a939390.1b1','111111','15274910702','111111@qq.com',1,1,'2023-01-30 14:37:56','2023-01-30 14:37:56',NULL),(28,'dkf','$e2db5fe3861b2c585e8daa5ab578740e353289df2f2b7.95f4517db6b19c2','董开放','15939689138','11121566654@qq.com',1,1,'2023-01-30 14:41:17','2023-01-30 14:41:17',NULL),(29,'sean','$628yd0d263d8a6957be6d7f53b67a408ba79489160e5a66ce8d3ed2.12ec8','sean','13664833271','1725230896@qq.com',1,1,'2023-01-30 14:44:41','2023-01-30 14:44:41',NULL),(30,'a123','$e21b0fa5d4cf319e4a90bda95392a4b1bde55.60e40d527.fc2b00f18a893','a123','15764647878','123@qq.com',1,1,'2023-01-30 14:46:25','2023-01-30 14:46:25',NULL),(31,'abc123','$e29y92a51.87ca4b2983c2bf3286d45cfc2b680e865a33677e899.2d28e70','abc','13456789012','3536737@qq.com',1,1,'2023-01-30 14:50:11','2023-01-30 14:50:11',NULL),(32,'11','$e21b0.abd5c83.9e4398b4aa5e94a4b2bfe75b64e30b5f74f02407fa80823','xiaofei','15737222781','3122323@qq.com',1,1,'2023-01-30 15:31:56','2023-01-30 15:31:56',NULL),(33,'3333','$e21y0.a3d.c8369a409bbea2509aa4bcb5e85a68e60b5d79f72a04fa8e853','3333','15737222782','31223232@qq.com',1,1,'2023-01-30 15:32:42','2023-01-30 15:32:42',NULL),(34,'3133','$e21b0.afd4c53a9.429bb6a8559da4bbbfe25b6ae8065b70fc2c01f48e833','3333','15737122282','312231232@qq.com',1,1,'2023-01-30 15:33:20','2023-01-30 15:33:20',NULL),(35,'31233','$e21b0badd8cf3.9e4e91bca.519aa4b2bae45a6aef0c5d7df02803f08f823','OR 1 =1','15737122222','3122231232@qq.com',1,1,'2023-01-30 15:33:56','2023-01-30 15:33:56',NULL),(36,'OR 1 =1','$e21b00a9dbcc3b95439ab9a55a9aa4beb0ee5a6de.0d5875fe2507f5858a3','OR 1 =1','15737121122','31222311232@qq.com',1,1,'2023-01-30 15:34:37','2023-01-30 15:34:37',NULL),(37,'666','$f2abe301b82071c64d5f1ece7.2284846f7ba85f6873e685ca19b6b44be55','666','15169437254','643825946@163.com',1,1,'2023-01-30 16:22:13','2023-01-30 16:22:13',NULL);
/*!40000 ALTER TABLE `cmp_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cmp_login_log`
--

DROP TABLE IF EXISTS `cmp_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmp_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(20) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `user_agent` varchar(50) DEFAULT NULL,
  `gmt_login` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cmp_login_log`
--

LOCK TABLES `cmp_login_log` WRITE;
/*!40000 ALTER TABLE `cmp_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `cmp_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cmp_login_log_detail`
--

DROP TABLE IF EXISTS `cmp_login_log_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmp_login_log_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_id` bigint(20) DEFAULT NULL,
  `state` tinyint(1) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cmp_login_log_detail`
--

LOCK TABLES `cmp_login_log_detail` WRITE;
/*!40000 ALTER TABLE `cmp_login_log_detail` DISABLE KEYS */;
INSERT INTO `cmp_login_log_detail` VALUES (64,92,1,'用户登录',0),(65,93,1,'用户登录',1),(66,94,1,'用户登录',0),(67,95,1,'用户登录',0),(68,96,1,'用户登录',1),(69,97,1,'用户登录',0),(70,98,1,'用户登录',0),(71,99,1,'用户登录',0),(72,100,1,'用户登录',0),(73,101,1,'用户登录',0),(74,102,1,'用户登录',0),(75,103,1,'用户登录',0),(76,104,1,'用户登录',0),(77,105,1,'用户登录',0),(78,106,1,'用户登录',0),(79,107,1,'用户登录',0),(80,108,1,'用户登录',0),(81,109,1,'用户登录',0),(82,110,1,'用户登录',0),(83,111,1,'用户登录',0),(84,112,1,'用户登录',0),(85,113,1,'用户登录',0),(86,114,1,'用户登录',0),(87,115,1,'用户登录',0),(88,116,1,'用户登录',0),(89,117,1,'用户登录',0),(90,118,1,'用户登录',0),(91,119,1,'用户登录',0),(92,120,1,'用户登录',0),(93,121,1,'用户登录',0),(94,122,1,'用户登录',0),(95,123,1,'用户登录',0);
/*!40000 ALTER TABLE `cmp_login_log_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cmp_operate_log_detail`
--

DROP TABLE IF EXISTS `cmp_operate_log_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cmp_operate_log_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin_id` bigint(20) DEFAULT NULL,
  `state` tinyint(1) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `gmt_operate` datetime DEFAULT NULL,
  `request_parameter` varchar(255) DEFAULT NULL,
  `operate_method` varchar(255) DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=268 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cmp_operate_log_detail`
--

LOCK TABLES `cmp_operate_log_detail` WRITE;
/*!40000 ALTER TABLE `cmp_operate_log_detail` DISABLE KEYS */;
INSERT INTO `cmp_operate_log_detail` VALUES (255,25,1,'','2023-01-17 18:44:16','{\"password\":\"{protected}\",\"phone\":\"123\",\"enable\":0,\"nickname\":\"test\",\"actPassword\":\"123456\",\"avator\":\"123\",\"id\":0,\"email\":\"12323\",\"username\":\"test006\"}','addNewUser',0),(256,26,1,'','2023-01-29 14:24:23','{\"password\":\"{protected}\",\"phone\":\"12345678900\",\"nickname\":\"admin\",\"actPassword\":\"123456\",\"email\":\"1234567890@qq.com\",\"username\":\"admin\"}','addNewUser',0),(257,27,1,'','2023-01-30 14:37:56','{\"password\":\"{protected}\",\"phone\":\"15274910702\",\"nickname\":\"111111\",\"actPassword\":\"111111\",\"email\":\"111111@qq.com\",\"username\":\"111111\"}','addNewUser',0),(258,28,1,'','2023-01-30 14:41:17','{\"password\":\"{protected}\",\"phone\":\"15939689138\",\"nickname\":\"董开放\",\"actPassword\":\"qq321321\",\"email\":\"11121566654@qq.com\",\"username\":\"dkf\"}','addNewUser',0),(259,29,1,'','2023-01-30 14:44:41','{\"password\":\"{protected}\",\"phone\":\"13664833271\",\"nickname\":\"sean\",\"actPassword\":\"WSS4120180\",\"email\":\"1725230896@qq.com\",\"username\":\"sean\"}','addNewUser',0),(260,30,1,'','2023-01-30 14:46:25','{\"password\":\"{protected}\",\"phone\":\"15764647878\",\"nickname\":\"a123\",\"actPassword\":\"123456\",\"email\":\"123@qq.com\",\"username\":\"a123\"}','addNewUser',0),(261,31,1,'','2023-01-30 14:50:11','{\"password\":\"{protected}\",\"phone\":\"13456789012\",\"nickname\":\"abc\",\"actPassword\":\"abc123\",\"email\":\"3536737@qq.com\",\"username\":\"abc123\"}','addNewUser',0),(262,32,1,'','2023-01-30 15:31:56','{\"password\":\"{protected}\",\"phone\":\"15737222781\",\"nickname\":\"xiaofei\",\"actPassword\":\"123456\",\"email\":\"3122323@qq.com\",\"username\":\"11\"}','addNewUser',0),(263,33,1,'','2023-01-30 15:32:42','{\"password\":\"{protected}\",\"phone\":\"15737222782\",\"nickname\":\"3333\",\"actPassword\":\"123456\",\"email\":\"31223232@qq.com\",\"username\":\"3333\"}','addNewUser',0),(264,34,1,'','2023-01-30 15:33:20','{\"password\":\"{protected}\",\"phone\":\"15737122282\",\"nickname\":\"3333\",\"actPassword\":\"123456\",\"email\":\"312231232@qq.com\",\"username\":\"3133\"}','addNewUser',0),(265,35,1,'','2023-01-30 15:33:56','{\"password\":\"{protected}\",\"phone\":\"15737122222\",\"nickname\":\"OR 1 =1\",\"actPassword\":\"123456\",\"email\":\"3122231232@qq.com\",\"username\":\"31233\"}','addNewUser',0),(266,36,1,'','2023-01-30 15:34:37','{\"password\":\"{protected}\",\"phone\":\"15737121122\",\"nickname\":\"OR 1 =1\",\"actPassword\":\"123456\",\"email\":\"31222311232@qq.com\",\"username\":\"OR 1 =1\"}','addNewUser',0),(267,37,1,'','2023-01-30 16:22:13','{\"password\":\"{protected}\",\"phone\":\"15169437254\",\"nickname\":\"666\",\"actPassword\":\"666\",\"email\":\"643825946@163.com\",\"username\":\"666\"}','addNewUser',0);
/*!40000 ALTER TABLE `cmp_operate_log_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'cs_mall_passport'
--

--
-- Dumping routines for database 'cs_mall_passport'
--
/*!50112 SET @disable_bulk_load = IF (@is_rocksdb_supported, 'SET SESSION rocksdb_bulk_load = @old_rocksdb_bulk_load', 'SET @dummy_rocksdb_bulk_load = 0') */;
/*!50112 PREPARE s FROM @disable_bulk_load */;
/*!50112 EXECUTE s */;
/*!50112 DEALLOCATE PREPARE s */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-14 15:40:28
