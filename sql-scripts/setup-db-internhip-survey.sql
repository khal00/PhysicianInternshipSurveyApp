DROP DATABASE  IF EXISTS `internship_survey`;

CREATE DATABASE  IF NOT EXISTS `internship_survey`;
USE `internship_survey`;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
`id` int NOT NULL AUTO_INCREMENT,
`username` varchar(50) NOT NULL,
`password` char(68) NOT NULL,
`enabled` tinyint(1) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `admin_personal_data`
--

DROP TABLE IF EXISTS `admin_personal_data`;
CREATE TABLE `admin_personal_data` (
`id` int NOT NULL AUTO_INCREMENT,
`first_name` varchar(50) NOT NULL,
`last_name` varchar(50) NOT NULL,
`email` varchar(50) NOT NULL,
`phone_number` int(15) NOT NULL,
PRIMARY KEY `admins_idx_1` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table `user_admin_data`
--

DROP TABLE IF EXISTS `user_admin_data`;
CREATE TABLE `user_admin_data` (
`user_id` int NOT NULL,
`admin_data_id` int NOT NULL,
PRIMARY KEY (`user_id`, `admin_data_id`),
CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
ON DELETE NO ACTION ON UPDATE CASCADE,
CONSTRAINT `admin_data_fk` FOREIGN KEY (`admin_data_id`) REFERENCES `admin_personal_data` (`id`)
ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Insert some data for testing. Default passwords here are: fun123
--

INSERT INTO `user` 
VALUES 
(1,'john','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1),
(2,'mary','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1),
(3,'susan','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1);

INSERT INTO `admin_personal_data`
VALUES
(1,'Mary','Kennedy','mary@tmail.com',123456789),
(2,'Susan','Lincoln','susan@tmail.com',987654321);

INSERT INTO `user_admin_data`
VALUES
(2,1),
(3,2);

--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
`user_id` int NOT NULL,
`authority` varchar(50) NOT NULL,
UNIQUE KEY `authorities_idx_1` (`user_id`,`authority`),
CONSTRAINT `authorities_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authorities`
--

INSERT INTO `authorities` 
VALUES 
(1,'ROLE_USER'),
(2,'ROLE_OILADMIN'),
(3,'ROLE_ADMIN');

--
-- Table structure for table `main_section`
--

DROP TABLE IF EXISTS `main_section`;
CREATE TABLE `main_section` (
`quest_id` int NOT NULL AUTO_INCREMENT,
`status` varchar(15) DEFAULT 'draft',
`medical_chamber` varchar(50),
`unit_name` varchar(50) NOT NULL,
                                                                                                                                                                                               
PRIMARY KEY `quest_id_idx_1` (`quest_id`),
CONSTRAINT `quest_id_fk_1` FOREIGN KEY (`quest_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO main_section
(medical_chamber, unit_name) 
VALUES ('OIL w Szczecinie', 'SPWSZ');

