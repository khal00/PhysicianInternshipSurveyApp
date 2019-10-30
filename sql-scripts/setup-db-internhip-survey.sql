DROP DATABASE  IF EXISTS `internship_survey`;

CREATE DATABASE  IF NOT EXISTS `internship_survey`;
USE `internship_survey`;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `admins_personal_data`
--

DROP TABLE IF EXISTS `admins_personal_data`;
CREATE TABLE `admins_personal_data` (
  `username` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone_number` int(15) NOT NULL,
  UNIQUE KEY `admins_idx_1` (`username`),
  CONSTRAINT `admins_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Default passwords here are: fun123
--


INSERT INTO `users` 
VALUES 
('john','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1),
('mary','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1),
('susan','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1);



--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authorities`
--

INSERT INTO `authorities` 
VALUES 
('john','ROLE_USER'),
('mary','ROLE_OILADMIN'),
('susan','ROLE_ADMIN');

--
-- Table structure for table `main_section`
--

DROP TABLE IF EXISTS `main_section`;
CREATE TABLE `main_section` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `status` varchar(15) DEFAULT 'draft',
  `medical_chamber` varchar(50),
  `unit_name` varchar(50) NOT NULL,
                                                                                                                                                                                             
  
  PRIMARY KEY `username_idx_1` (`id`),
  CONSTRAINT `username_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO main_section
(username, medical_chamber, unit_name) 
values ('john', 'OIL w Szczecinie', 'SPWSZ'),
	   ('mary', 'OIL w Szczecinie', 'SPSK1');

