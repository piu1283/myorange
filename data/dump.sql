-- MySQL dump 10.13  Distrib 8.0.18, for macos10.14 (x86_64)
--
-- Host: localhost    Database: db_test
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `db_test`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `db_test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `db_test`;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `dept_no` char(32) NOT NULL COMMENT '主键',
  `dept_name` varchar(40) NOT NULL COMMENT '部门名称',
  PRIMARY KEY (`dept_no`),
  UNIQUE KEY `dept_name` (`dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES ('d003','人事部'),('d001','市场部'),('d004','生产部'),('d005','研发部'),('d002','财务部'),('d006','质量部'),('d008','销售服务部'),('d007','销售部');
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dept_emp`
--

DROP TABLE IF EXISTS `dept_emp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dept_emp` (
  `emp_no` int(11) NOT NULL COMMENT '主键',
  `dept_no` char(4) NOT NULL COMMENT '主键',
  `from_date` date NOT NULL COMMENT '开始时间',
  `to_date` date NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`emp_no`,`dept_no`),
  KEY `dept_no` (`dept_no`),
  CONSTRAINT `dept_emp_ibfk_1` FOREIGN KEY (`emp_no`) REFERENCES `employees` (`emp_no`) ON DELETE CASCADE,
  CONSTRAINT `dept_emp_ibfk_2` FOREIGN KEY (`dept_no`) REFERENCES `departments` (`dept_no`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dept_emp`
--

LOCK TABLES `dept_emp` WRITE;
/*!40000 ALTER TABLE `dept_emp` DISABLE KEYS */;
INSERT INTO `dept_emp` VALUES (10001,'d005','1986-06-26','9999-01-01'),(10002,'d007','1996-08-03','9999-01-01'),(10003,'d004','1995-12-03','9999-01-01'),(10004,'d004','1986-12-01','9999-01-01'),(10005,'d003','1989-09-12','9999-01-01'),(10006,'d005','1990-08-05','9999-01-01'),(10007,'d008','1989-02-10','9999-01-01'),(10008,'d005','1998-03-11','2000-07-31'),(10009,'d006','1985-02-18','9999-01-01'),(10010,'d004','1996-11-24','2000-06-26'),(10010,'d006','2000-06-26','9999-01-01'),(10011,'d007','1990-01-22','1996-11-09'),(10012,'d005','1992-12-18','9999-01-01'),(10013,'d003','1985-10-20','9999-01-01'),(10014,'d005','1993-12-29','9999-01-01'),(10015,'d008','1992-09-19','1993-08-22'),(10016,'d007','1998-02-11','9999-01-01'),(10017,'d001','1993-08-03','9999-01-01'),(10018,'d004','1992-07-29','9999-01-01'),(10018,'d005','1987-04-03','1992-07-29'),(10019,'d008','1999-04-30','9999-01-01'),(10020,'d002','1997-12-30','9999-01-01'),(10021,'d005','1988-02-10','2002-07-15'),(10022,'d005','1999-09-03','9999-01-01'),(10023,'d005','1999-09-27','9999-01-01'),(10024,'d004','1998-06-14','9999-01-01'),(10025,'d005','1987-08-17','1997-10-15'),(10026,'d004','1995-03-20','9999-01-01'),(10027,'d005','1995-04-02','9999-01-01'),(10028,'d005','1991-10-22','1998-04-06'),(10029,'d004','1991-09-18','1999-07-08'),(10029,'d006','1999-07-08','9999-01-01'),(10030,'d004','1994-02-17','9999-01-01');
/*!40000 ALTER TABLE `dept_emp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dept_manager`
--

DROP TABLE IF EXISTS `dept_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dept_manager` (
  `emp_no` int(11) NOT NULL COMMENT '主键',
  `dept_no` char(4) NOT NULL COMMENT '主键',
  `from_date` date NOT NULL COMMENT '开始时间',
  `to_date` date NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`emp_no`,`dept_no`),
  KEY `dept_no` (`dept_no`),
  CONSTRAINT `dept_manager_ibfk_1` FOREIGN KEY (`emp_no`) REFERENCES `employees` (`emp_no`) ON DELETE CASCADE,
  CONSTRAINT `dept_manager_ibfk_2` FOREIGN KEY (`dept_no`) REFERENCES `departments` (`dept_no`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dept_manager`
--

LOCK TABLES `dept_manager` WRITE;
/*!40000 ALTER TABLE `dept_manager` DISABLE KEYS */;
INSERT INTO `dept_manager` VALUES (10007,'d008','1988-09-09','1992-08-02'),(10013,'d003','1985-01-01','1989-12-17'),(10016,'d007','1985-01-01','1988-09-09'),(10017,'d001','1985-01-01','1991-10-01'),(10018,'d004','1989-12-17','9999-01-01'),(10018,'d005','1985-01-01','1992-03-21'),(10020,'d002','1991-10-01','9999-01-01'),(10029,'d006','1992-03-21','9999-01-01');
/*!40000 ALTER TABLE `dept_manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `emp_no` int(11) NOT NULL COMMENT '主键',
  `birth_date` date NOT NULL COMMENT '生日',
  `first_name` varchar(14) NOT NULL COMMENT '用户-姓',
  `last_name` varchar(16) NOT NULL COMMENT '用户-名',
  `gender` enum('M','F') NOT NULL COMMENT '性别',
  `hire_date` date NOT NULL COMMENT '入职时间',
  PRIMARY KEY (`emp_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (10001,'1953-09-02','Georgi','Facello','M','1986-06-26'),(10002,'1964-06-02','Bezalel','Simmel','F','1985-11-21'),(10003,'1959-12-03','Parto','Bamford','M','1986-08-28'),(10004,'1954-05-01','Chirstian','Koblick','M','1986-12-01'),(10005,'1955-01-21','Kyoichi','Maliniak','M','1989-09-12'),(10006,'1953-04-20','Anneke','Preusig','F','1989-06-02'),(10007,'1957-05-23','Tzvetan','Zielinski','F','1989-02-10'),(10008,'1958-02-19','Saniya','Kalloufi','M','1994-09-15'),(10009,'1952-04-19','Sumant','Peac','F','1985-02-18'),(10010,'1963-06-01','Duangkaew','Piveteau','F','1989-08-24'),(10011,'1953-11-07','Mary','Sluis','F','1990-01-22'),(10012,'1960-10-04','Patricio','Bridgland','M','1992-12-18'),(10013,'1963-06-07','Eberhardt','Terkki','M','1985-10-20'),(10014,'1956-02-12','Berni','Genin','M','1987-03-11'),(10015,'1959-08-19','Guoxiang','Nooteboom','M','1987-07-02'),(10016,'1961-05-02','Kazuhito','Cappelletti','M','1995-01-27'),(10017,'1958-07-06','Cristinel','Bouloucos','F','1993-08-03'),(10018,'1954-06-19','Kazuhide','Peha','F','1987-04-03'),(10019,'1953-01-23','Lillian','Haddadi','M','1999-04-30'),(10020,'1952-12-24','Mayuko','Warwick','M','1991-01-26'),(10021,'1960-02-20','Ramzi','Erde','M','1988-02-10'),(10022,'1952-07-08','Shahaf','Famili','M','1995-08-22'),(10023,'1953-09-29','Bojan','Montemayor','F','1989-12-17'),(10024,'1958-09-05','Suzette','Pettey','F','1997-05-19'),(10025,'1958-10-31','Prasadram','Heyers','M','1987-08-17'),(10026,'1953-04-03','Yongqiao','Berztiss','M','1995-03-20'),(10027,'1962-07-10','Divier','Reistad','F','1989-07-07'),(10028,'1963-11-26','Domenick','Tempesti','M','1991-10-22'),(10029,'1956-12-13','Otmar','Herbst','M','1985-11-20'),(10030,'1958-07-14','Elvis','Demeyer','M','1994-02-17');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `salaries`
--

DROP TABLE IF EXISTS `salaries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salaries` (
  `emp_no` int(11) NOT NULL COMMENT '主键',
  `salary` int(11) NOT NULL COMMENT '薪水',
  `from_date` date NOT NULL COMMENT '主键',
  `to_date` date NOT NULL,
  PRIMARY KEY (`emp_no`,`from_date`),
  CONSTRAINT `salaries_ibfk_1` FOREIGN KEY (`emp_no`) REFERENCES `employees` (`emp_no`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `salaries`
--

LOCK TABLES `salaries` WRITE;
/*!40000 ALTER TABLE `salaries` DISABLE KEYS */;
INSERT INTO `salaries` VALUES (10001,5000,'1986-06-26','9999-01-01'),(10002,5100,'1996-08-03','9999-01-01'),(10003,5200,'1995-12-03','9999-01-01'),(10004,5300,'1986-12-01','9999-01-01'),(10005,5400,'1989-09-12','9999-01-01'),(10006,5500,'1990-08-05','9999-01-01'),(10007,5600,'1989-02-10','9999-01-01'),(10008,5700,'1998-03-11','2000-07-31'),(10009,5800,'1985-02-18','9999-01-01'),(10010,5900,'1996-11-24','2000-06-26'),(10010,6000,'2000-06-26','9999-01-01'),(10011,6100,'1990-01-22','1996-11-09'),(10012,5000,'1992-12-18','9999-01-01'),(10013,5100,'1985-10-20','9999-01-01'),(10014,5200,'1993-12-29','9999-01-01'),(10015,5300,'1992-09-19','1993-08-22'),(10016,5400,'1998-02-11','9999-01-01'),(10017,5500,'1993-08-03','9999-01-01'),(10018,5700,'1987-04-03','1992-07-29'),(10018,5600,'1992-07-29','9999-01-01'),(10019,5800,'1999-04-30','9999-01-01'),(10020,5900,'1997-12-30','9999-01-01'),(10021,6000,'1988-02-10','2002-07-15'),(10022,6100,'1999-09-03','9999-01-01'),(10023,5000,'1999-09-27','9999-01-01'),(10024,5100,'1998-06-14','9999-01-01'),(10025,5200,'1987-08-17','1997-10-15'),(10026,5300,'1995-03-20','9999-01-01'),(10027,5400,'1995-04-02','9999-01-01'),(10028,5500,'1991-10-22','1998-04-06'),(10029,5600,'1991-09-18','1999-07-08'),(10029,5700,'1999-07-08','9999-01-01'),(10030,5800,'1994-02-17','9999-01-01');
/*!40000 ALTER TABLE `salaries` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-21 16:36:55
