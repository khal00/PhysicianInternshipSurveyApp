-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: localhost    Database: internship_survey
-- ------------------------------------------------------
-- Server version	5.7.29-0ubuntu0.18.04.1

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

--
-- Current Database: `internship_survey`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `internship_survey` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `internship_survey`;

--
-- Table structure for table `acl_class`
--

DROP TABLE IF EXISTS `acl_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_class` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_acl_class` (`class`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_class`
--

LOCK TABLES `acl_class` WRITE;
/*!40000 ALTER TABLE `acl_class` DISABLE KEYS */;
INSERT INTO `acl_class` VALUES (2,'com.khal.intern_survey.entity.InternshipUnit'),(1,'com.khal.intern_survey.entity.Questionnaire');
/*!40000 ALTER TABLE `acl_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_entry`
--

DROP TABLE IF EXISTS `acl_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_entry` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `acl_object_identity` bigint(20) unsigned NOT NULL,
  `ace_order` int(11) NOT NULL,
  `sid` bigint(20) unsigned NOT NULL,
  `mask` int(10) unsigned NOT NULL,
  `granting` tinyint(1) NOT NULL,
  `audit_success` tinyint(1) NOT NULL,
  `audit_failure` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_acl_entry` (`acl_object_identity`,`ace_order`),
  KEY `fk_acl_entry_acl` (`sid`),
  CONSTRAINT `fk_acl_entry_acl` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`),
  CONSTRAINT `fk_acl_entry_object` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_entry`
--

LOCK TABLES `acl_entry` WRITE;
/*!40000 ALTER TABLE `acl_entry` DISABLE KEYS */;
INSERT INTO `acl_entry` VALUES (1,1,0,1,1,1,0,0),(2,1,1,1,2,1,0,0),(3,1,2,1,8,1,0,0),(4,2,0,1,1,1,0,0),(5,2,1,1,2,1,0,0),(6,2,2,1,8,1,0,0),(7,1,3,2,1,1,0,0),(8,1,4,2,2,1,0,0),(9,1,5,2,8,1,0,0),(10,2,3,2,1,1,0,0),(11,2,4,2,2,1,0,0),(12,2,5,2,8,1,0,0),(16,3,0,3,1,1,0,0),(17,3,1,1,1,1,0,0),(18,3,2,1,8,1,0,0),(19,3,3,2,1,1,0,0),(20,3,4,2,8,1,0,0),(21,4,0,3,1,1,0,0),(22,4,1,3,2,1,0,0),(23,4,2,3,8,1,0,0),(24,5,0,4,1,1,0,0),(25,5,1,4,2,1,0,0),(26,5,2,4,8,1,0,0),(27,6,0,3,1,1,0,0),(28,6,1,3,2,1,0,0),(29,6,2,3,8,1,0,0),(30,7,0,3,1,1,0,0),(31,7,1,3,2,1,0,0),(32,7,2,3,8,1,0,0),(33,8,0,3,1,1,0,0),(34,8,1,3,2,1,0,0),(35,8,2,3,8,1,0,0);
/*!40000 ALTER TABLE `acl_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_object_identity`
--

DROP TABLE IF EXISTS `acl_object_identity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_object_identity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `object_id_class` bigint(20) unsigned NOT NULL,
  `object_id_identity` bigint(20) NOT NULL,
  `parent_object` bigint(20) unsigned DEFAULT NULL,
  `owner_sid` bigint(20) unsigned DEFAULT NULL,
  `entries_inheriting` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_acl_object_identity` (`object_id_class`,`object_id_identity`),
  KEY `fk_acl_object_identity_parent` (`parent_object`),
  KEY `fk_acl_object_identity_owner` (`owner_sid`),
  CONSTRAINT `fk_acl_object_identity_class` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
  CONSTRAINT `fk_acl_object_identity_owner` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`),
  CONSTRAINT `fk_acl_object_identity_parent` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_object_identity`
--

LOCK TABLES `acl_object_identity` WRITE;
/*!40000 ALTER TABLE `acl_object_identity` DISABLE KEYS */;
INSERT INTO `acl_object_identity` VALUES (1,2,1,NULL,1,0),(2,2,2,NULL,1,0),(3,1,1,NULL,3,1),(4,1,2,NULL,3,1),(5,1,3,NULL,4,1),(6,1,4,NULL,3,1),(7,1,5,NULL,3,1),(8,1,6,NULL,3,1);
/*!40000 ALTER TABLE `acl_object_identity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_sid`
--

DROP TABLE IF EXISTS `acl_sid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_sid` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `principal` tinyint(1) NOT NULL,
  `sid` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_acl_sid` (`sid`,`principal`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_sid`
--

LOCK TABLES `acl_sid` WRITE;
/*!40000 ALTER TABLE `acl_sid` DISABLE KEYS */;
INSERT INTO `acl_sid` VALUES (2,1,'ho@g.com'),(4,1,'krzhalewski@gmail.com'),(1,1,'ping@g.com'),(3,1,'xi@g.com');
/*!40000 ALTER TABLE `acl_sid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_personal_data`
--

DROP TABLE IF EXISTS `admin_personal_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_personal_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `phone_number` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `medical_chamber` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_personal_data`
--

LOCK TABLES `admin_personal_data` WRITE;
/*!40000 ALTER TABLE `admin_personal_data` DISABLE KEYS */;
INSERT INTO `admin_personal_data` VALUES (1,'Ping','Po','123456789','OIL w Szczecinie'),(2,'Ho','Li','123456789','OIL w Szczecinie'),(3,'Chi','Fu','123456789','OIL w Warszawie');
/*!40000 ALTER TABLE `admin_personal_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tutor_name` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_name` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tutor` tinyint(4) DEFAULT '0',
  `unit` tinyint(4) DEFAULT '0',
  `theoretical_knowledge` tinyint(4) DEFAULT '0',
  `practical_knowledge` tinyint(4) DEFAULT '0',
  `questionnaire_id` bigint(20) unsigned NOT NULL,
  `disabled` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `course_quest_id_ibfk` (`questionnaire_id`),
  CONSTRAINT `course_quest_id_ibfk` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaires` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'Clinical Transfusiology','opiekun trans','miejsce trans',4,4,4,4,1,0),(2,'Prevention of HIV infection',NULL,NULL,0,0,0,0,1,1),(3,'Emergency Medicine','op ratun',NULL,4,4,4,4,1,0),(4,'Medical Certification',NULL,NULL,0,0,0,0,1,1),(5,'Bioethics',NULL,NULL,0,0,0,0,1,1),(6,'Medical Law',NULL,NULL,0,0,0,0,1,1),(7,'Clinical Transfusiology',NULL,NULL,0,0,0,0,2,0),(8,'Prevention of HIV infection',NULL,NULL,0,0,0,0,2,0),(9,'Emergency Medicine',NULL,NULL,0,0,0,0,2,0),(10,'Medical Certification',NULL,NULL,0,0,0,0,2,0),(11,'Bioethics',NULL,NULL,0,0,0,0,2,0),(12,'Medical Law',NULL,NULL,0,0,0,0,2,0),(13,'Clinical Transfusiology',NULL,NULL,0,0,0,0,3,0),(14,'Prevention of HIV infection',NULL,NULL,0,0,0,0,3,0),(15,'Emergency Medicine',NULL,NULL,0,0,0,0,3,0),(16,'Medical Certification',NULL,NULL,0,0,0,0,3,0),(17,'Bioethics',NULL,NULL,0,0,0,0,3,0),(18,'Medical Law',NULL,NULL,0,0,0,0,3,0),(19,'Clinical Transfusiology',NULL,NULL,0,0,0,0,4,0),(20,'Prevention of HIV infection',NULL,NULL,0,0,0,0,4,0),(21,'Emergency Medicine',NULL,NULL,0,0,0,0,4,0),(22,'Medical Certification',NULL,NULL,0,0,0,0,4,0),(23,'Bioethics',NULL,NULL,0,0,0,0,4,0),(24,'Medical Law',NULL,NULL,0,0,0,0,4,0),(25,'Clinical Transfusiology',NULL,NULL,0,0,0,0,5,0),(26,'Prevention of HIV infection',NULL,NULL,0,0,0,0,5,0),(27,'Emergency Medicine',NULL,NULL,0,0,0,0,5,0),(28,'Medical Certification',NULL,NULL,0,0,0,0,5,0),(29,'Bioethics',NULL,NULL,0,0,0,0,5,0),(30,'Medical Law',NULL,NULL,0,0,0,0,5,0),(31,'Clinical Transfusiology',NULL,NULL,0,0,0,0,6,0),(32,'Prevention of HIV infection',NULL,NULL,0,0,0,0,6,0),(33,'Emergency Medicine',NULL,NULL,0,0,0,0,6,0),(34,'Medical Certification',NULL,NULL,0,0,0,0,6,0),(35,'Bioethics',NULL,NULL,0,0,0,0,6,0),(36,'Medical Law',NULL,NULL,0,0,0,0,6,0);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_update_token`
--

DROP TABLE IF EXISTS `email_update_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_update_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `expiry_date` date NOT NULL,
  `token` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `new_email` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `email_update_token_ibfk` (`user_id`),
  CONSTRAINT `email_update_token_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_update_token`
--

LOCK TABLES `email_update_token` WRITE;
/*!40000 ALTER TABLE `email_update_token` DISABLE KEYS */;
INSERT INTO `email_update_token` VALUES (1,1,'2020-04-03','1ddc6c67-4089-495b-966b-b0ef13c301cc','xitest@g.com'),(2,1,'2020-04-03','a1f4a163-86fd-4367-8c87-a7e485ebc920','xitest2@g.com'),(3,1,'2020-04-03','b60e2adb-3fde-471d-af4a-d1b954b4ae0d','xitest3@g.com'),(4,1,'2020-04-03','a7ce5f3e-42c2-4732-bcf1-d27d7111ef0f','xitest@g.com'),(5,1,'2020-04-03','97b348c0-b588-43d0-8df1-89c0dcbe6a74','xihh@g.com'),(6,1,'2020-04-03','1d4b0a95-a660-4d9e-8473-01958a8a850b','xifds@g.com');
/*!40000 ALTER TABLE `email_update_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `internship_section`
--

DROP TABLE IF EXISTS `internship_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `internship_section` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tutor_name` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_name` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `tutor` tinyint(4) DEFAULT '0',
  `unit` tinyint(4) DEFAULT '0',
  `number_of_procedures` tinyint(4) DEFAULT '0',
  `procedures_autonomy` tinyint(4) DEFAULT '0',
  `theoretical_knowledge` tinyint(4) DEFAULT '0',
  `practical_knowledge` tinyint(4) DEFAULT '0',
  `medical_duty` tinyint(4) DEFAULT '0',
  `ward` tinyint(4) DEFAULT '0',
  `clinic` tinyint(4) DEFAULT '0',
  `questionnaire_id` bigint(20) unsigned NOT NULL,
  `disabled` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `section_quest_id_ibfk` (`questionnaire_id`),
  CONSTRAINT `section_quest_id_ibfk` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaires` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `internship_section`
--

LOCK TABLES `internship_section` WRITE;
/*!40000 ALTER TABLE `internship_section` DISABLE KEYS */;
INSERT INTO `internship_section` VALUES (1,'Internal Medicine','opiek ch.w','oddział ch wew',3,3,3,3,3,3,3,3,3,1,0),(2,'Pediatrics',NULL,NULL,0,0,0,0,0,0,0,0,0,1,1),(3,'General Surgery',NULL,NULL,0,0,0,0,0,0,0,0,0,1,1),(4,'Obsterics and Gynecology',NULL,NULL,0,0,0,0,0,0,0,0,0,1,1),(5,'Anesthesiology and Intensive Care','op anest','miej anes',3,3,3,3,3,3,3,0,0,1,0),(6,'Psychiatry',NULL,NULL,0,0,0,0,0,0,0,0,0,1,1),(7,'Family Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,1,1),(8,'Internal Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,2,0),(9,'Pediatrics',NULL,NULL,0,0,0,0,0,0,0,0,0,2,0),(10,'General Surgery',NULL,NULL,0,0,0,0,0,0,0,0,0,2,0),(11,'Obsterics and Gynecology',NULL,NULL,0,0,0,0,0,0,0,0,0,2,0),(12,'Anesthesiology and Intensive Care',NULL,NULL,0,0,0,0,0,0,0,0,0,2,0),(13,'Psychiatry',NULL,NULL,0,0,0,0,0,0,0,0,0,2,0),(14,'Family Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,2,0),(15,'Internal Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,3,0),(16,'Pediatrics',NULL,NULL,0,0,0,0,0,0,0,0,0,3,0),(17,'General Surgery',NULL,NULL,0,0,0,0,0,0,0,0,0,3,0),(18,'Obsterics and Gynecology',NULL,NULL,0,0,0,0,0,0,0,0,0,3,0),(19,'Anesthesiology and Intensive Care',NULL,NULL,0,0,0,0,0,0,0,0,0,3,0),(20,'Psychiatry',NULL,NULL,0,0,0,0,0,0,0,0,0,3,0),(21,'Family Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,3,0),(22,'Internal Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,4,0),(23,'Pediatrics',NULL,NULL,0,0,0,0,0,0,0,0,0,4,0),(24,'General Surgery',NULL,NULL,0,0,0,0,0,0,0,0,0,4,0),(25,'Obsterics and Gynecology',NULL,NULL,0,0,0,0,0,0,0,0,0,4,0),(26,'Anesthesiology and Intensive Care',NULL,NULL,0,0,0,0,0,0,0,0,0,4,0),(27,'Psychiatry',NULL,NULL,0,0,0,0,0,0,0,0,0,4,0),(28,'Family Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,4,0),(29,'Internal Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,5,0),(30,'Pediatrics',NULL,NULL,0,0,0,0,0,0,0,0,0,5,0),(31,'General Surgery',NULL,NULL,0,0,0,0,0,0,0,0,0,5,0),(32,'Obsterics and Gynecology',NULL,NULL,0,0,0,0,0,0,0,0,0,5,0),(33,'Anesthesiology and Intensive Care',NULL,NULL,0,0,0,0,0,0,0,0,0,5,0),(34,'Psychiatry',NULL,NULL,0,0,0,0,0,0,0,0,0,5,0),(35,'Family Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,5,0),(36,'Internal Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,6,0),(37,'Pediatrics',NULL,NULL,0,0,0,0,0,0,0,0,0,6,0),(38,'General Surgery',NULL,NULL,0,0,0,0,0,0,0,0,0,6,0),(39,'Obsterics and Gynecology',NULL,NULL,0,0,0,0,0,0,0,0,0,6,0),(40,'Anesthesiology and Intensive Care',NULL,NULL,0,0,0,0,0,0,0,0,0,6,0),(41,'Psychiatry',NULL,NULL,0,0,0,0,0,0,0,0,0,6,0),(42,'Family Medicine',NULL,NULL,0,0,0,0,0,0,0,0,0,6,0);
/*!40000 ALTER TABLE `internship_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `internship_unit`
--

DROP TABLE IF EXISTS `internship_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `internship_unit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `medical_chamber` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `internship_unit`
--

LOCK TABLES `internship_unit` WRITE;
/*!40000 ALTER TABLE `internship_unit` DISABLE KEYS */;
INSERT INTO `internship_unit` VALUES (1,'Samodzielny Publiczny Szpital Kliniczny Nr 1 w Szczecinie','OIL w Szczecinie'),(2,'SPSK Nr 2 w Szczecinie','OIL w Szczecinie');
/*!40000 ALTER TABLE `internship_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_reset_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `expiry_date` date NOT NULL,
  `token` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `reset_token_ibfk` (`user_id`),
  CONSTRAINT `reset_token_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaires`
--

DROP TABLE IF EXISTS `questionnaires`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questionnaires` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status` tinyint(4) DEFAULT '0',
  `medical_chamber` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_id` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `send_date` date DEFAULT NULL,
  `verification_id` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `coordinator_name` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `coordinator` tinyint(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `create_time` (`create_time`),
  KEY `unit_id` (`unit_id`),
  KEY `user_id` (`user_id`),
  KEY `verification_id` (`verification_id`),
  CONSTRAINT `unit_id_ibfk` FOREIGN KEY (`unit_id`) REFERENCES `internship_unit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaires`
--

LOCK TABLES `questionnaires` WRITE;
/*!40000 ALTER TABLE `questionnaires` DISABLE KEYS */;
INSERT INTO `questionnaires` VALUES (1,2,'OIL w Szczecinie',1,1,'2020-03-11 13:19:32','2020-03-11','d39b9b99-2273-4912-9864-044ba5577187','Jan Nowak',6),(2,0,NULL,NULL,1,'2020-03-12 11:02:55',NULL,NULL,NULL,0),(3,0,NULL,NULL,6,'2020-03-28 21:58:07',NULL,NULL,NULL,0),(4,0,NULL,NULL,1,'2020-04-02 10:19:23',NULL,NULL,NULL,0),(5,0,NULL,NULL,1,'2020-04-05 09:51:34',NULL,NULL,NULL,0),(6,0,NULL,NULL,1,'2020-04-05 09:51:40',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `questionnaires` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_USER'),(2,'ROLE_MEDICALCHAMBERADMIN'),(3,'ROLE_ADMIN');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_admin_data`
--

DROP TABLE IF EXISTS `user_admin_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_admin_data` (
  `user_id` int(11) NOT NULL,
  `admin_data_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`admin_data_id`),
  KEY `admin_data_ibfk` (`admin_data_id`),
  CONSTRAINT `admin_data_ibfk` FOREIGN KEY (`admin_data_id`) REFERENCES `admin_personal_data` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_admin_data`
--

LOCK TABLES `user_admin_data` WRITE;
/*!40000 ALTER TABLE `user_admin_data` DISABLE KEYS */;
INSERT INTO `user_admin_data` VALUES (3,1),(4,2),(5,3);
/*!40000 ALTER TABLE `user_admin_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `password` char(68) COLLATE utf8_unicode_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'xi@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',1),(2,'jin@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',1),(3,'ping@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',1),(4,'ho@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',1),(5,'chi@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',1),(6,'krzhalewski@gmail.com','{bcrypt}$2a$10$JehsCuLQSEvtqN1flPvL/eQ9HCS9Dt51Y1TP7dFe6zuJCwl2eU2Yu',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `users_roles_role_ibfk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `users_roles_user_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES (1,1),(2,1),(6,1),(3,2),(4,2),(5,2);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_token`
--

DROP TABLE IF EXISTS `verification_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verification_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `expiry_date` date NOT NULL,
  `token` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `verification_token_ibfk` (`user_id`),
  CONSTRAINT `verification_token_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_token`
--

LOCK TABLES `verification_token` WRITE;
/*!40000 ALTER TABLE `verification_token` DISABLE KEYS */;
INSERT INTO `verification_token` VALUES (1,6,'2020-03-31','8268dcab-263e-4527-86e8-8aa5cecd3e54');
/*!40000 ALTER TABLE `verification_token` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-09 20:23:33
