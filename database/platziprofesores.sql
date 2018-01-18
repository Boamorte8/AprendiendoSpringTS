CREATE DATABASE  IF NOT EXISTS `platziprofesores` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `platziprofesores`;
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: platziprofesores
-- ------------------------------------------------------
-- Server version	5.7.20

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
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course` (
  `id_course` int(11) NOT NULL AUTO_INCREMENT,
  `id_teacher` int(11) DEFAULT NULL,
  `name` varchar(250) NOT NULL,
  `themes` text,
  `project` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id_course`),
  KEY `id_teacher_idx` (`id_teacher`),
  CONSTRAINT `id_teacher_course` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id_teacher`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,NULL,'PruebaUpdate1','Pruebas Update1','Pruebas Update1'),(2,NULL,'JavaEE Avanzado','Hibernate, Spring Boot, STS, Api Rest, mySql','Api Rest'),(3,NULL,'PruebaDelete','Pruebadelete','Pruebadelete'),(4,NULL,'AngularJS','Directives, Controllers, Filters, Modules, HttpRequests','Single One Page');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `social_media`
--

DROP TABLE IF EXISTS `social_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `social_media` (
  `id_social_media` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `icon` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id_social_media`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `social_media`
--

LOCK TABLES `social_media` WRITE;
/*!40000 ALTER TABLE `social_media` DISABLE KEYS */;
INSERT INTO `social_media` VALUES (4,'ParaBorrar','avatar99'),(5,'Facebook','images/social_medias/5-pictureSocialMedia-2018-01-17-09-05-33.png'),(6,'Twitter','avatar99'),(7,'Instagram','avatar99');
/*!40000 ALTER TABLE `social_media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `id_teacher` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `avatar` varchar(250) NOT NULL,
  PRIMARY KEY (`id_teacher`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (2,'Juan Carlos Marin','images/teachers/2-pictureTeacher-2018-01-17-09-00-53.jpeg'),(3,'Diego Jose Luis Botia','avatar0'),(4,'For delete','avatar0'),(5,'For update','avatar0'),(6,'Jhonatan Diosa','avatar0'),(7,'Roberto Florez','avatar0');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_social_media`
--

DROP TABLE IF EXISTS `teacher_social_media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher_social_media` (
  `id_teacher_social_media` int(11) NOT NULL AUTO_INCREMENT,
  `id_teacher` int(11) NOT NULL,
  `id_social_media` int(11) NOT NULL,
  `nickname` varchar(250) NOT NULL,
  PRIMARY KEY (`id_teacher_social_media`),
  KEY `id_teacher` (`id_teacher`,`id_social_media`),
  KEY `id_social_media_idx` (`id_social_media`),
  CONSTRAINT `id_social_media_sm` FOREIGN KEY (`id_social_media`) REFERENCES `social_media` (`id_social_media`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `id_teacher_sm` FOREIGN KEY (`id_teacher`) REFERENCES `teacher` (`id_teacher`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_social_media`
--

LOCK TABLES `teacher_social_media` WRITE;
/*!40000 ALTER TABLE `teacher_social_media` DISABLE KEYS */;
INSERT INTO `teacher_social_media` VALUES (2,6,5,'jdiosa');
/*!40000 ALTER TABLE `teacher_social_media` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-18 10:02:22
