DROP DATABASE IF EXISTS `internship_survey`;

CREATE DATABASE `internship_survey` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `internship_survey`;

CREATE TABLE `users` (
`id` int NOT NULL AUTO_INCREMENT,
`email` varchar(50) NOT NULL,
`password` char(68) NOT NULL,
`enabled` boolean NOT NULL DEFAULT FALSE,
PRIMARY KEY (`id`),
UNIQUE KEY (`email`)
) ENGINE=InnoDB;

CREATE TABLE `verification_token` (
`id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`expiry_date` date NOT NULL,
`token` varchar (50) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `verification_token_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `password_reset_token` (
`id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`expiry_date` date NOT NULL,
`token` varchar (50) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `reset_token_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `email_update_token` (
`id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`expiry_date` date NOT NULL,
`token` varchar (50) NOT NULL,
`new_email` varchar (30) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `email_update_token_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `admin_personal_data` (
`id` int NOT NULL AUTO_INCREMENT,
`first_name` varchar(50) NOT NULL,
`last_name` varchar(50) NOT NULL,
`phone_number` varchar(15) NOT NULL,
`medical_chamber` varchar(50),
PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `user_admin_data` (
`user_id` int NOT NULL,
`admin_data_id` int NOT NULL,
PRIMARY KEY (`user_id`, `admin_data_id`),
CONSTRAINT `user_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE,

CONSTRAINT `admin_data_ibfk` FOREIGN KEY (`admin_data_id`) REFERENCES `admin_personal_data` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- test password: x

INSERT INTO `users` 
VALUES 
(1,'xi@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true),
(2,'jin@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true),
(3,'ping@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true),
(4,'ho@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true),
(5,'chi@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true);

INSERT INTO `admin_personal_data`
VALUES
(1,'Ping','Po','123456789','OIL w Szczecinie'),
(2,'Ho','Li','123456789','OIL w Szczecinie'),
(3,'Chi','Fu','123456789','OIL w Warszawie');

INSERT INTO `user_admin_data`
VALUES
(3,1),
(4,2),
(5,3);

CREATE TABLE `role` (
`id` int NOT NULL AUTO_INCREMENT,
`name` varchar(50) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;


INSERT INTO `role` (name)
VALUES 
('ROLE_USER'),('ROLE_MEDICALCHAMBERADMIN'),('ROLE_ADMIN');


CREATE TABLE `users_roles` (
`user_id` int NOT NULL,
`role_id` int NOT NULL,
  
PRIMARY KEY (`user_id`,`role_id`),
KEY (`role_id`),
  
CONSTRAINT `users_roles_user_ibfk` FOREIGN KEY (`user_id`) 
REFERENCES `users` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE,
  
CONSTRAINT `users_roles_role_ibfk` FOREIGN KEY (`role_id`) 
REFERENCES `role` (`id`) 
ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO `users_roles` (user_id,role_id)
VALUES 
(1, 1),
(2, 1),
(3, 2),
(4, 2),
(5, 2);


CREATE TABLE `internship_unit` (
`id` int NOT NULL AUTO_INCREMENT,
`name` varchar(150) NOT NULL,
`medical_chamber` varchar(50) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `internship_unit` VALUES
(1,'Samodzielny Publiczny Szpital Kliniczny Nr 1 w Szczecinie','OIL w Szczecinie'),
(2,'SPSK Nr 2 w Szczecinie','OIL w Szczecinie');
/*
(3,'ZOZ MSWIA w Warszawie','OIL w Warszawie'),
(4,'111 Szpital Wojskowy w Poznaniu','WIL w Poznaniu');
*/


CREATE TABLE `questionnaires` (
`id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
`status` TINYINT DEFAULT 0,
`medical_chamber` varchar(50),
`unit_id` int,
`user_id` int NOT NULL,
`create_time` timestamp NOT NULL,
`send_date` date,
`verification_id` varchar(36),
`coordinator_name` varchar(40),
`coordinator` tinyint unsigned,
PRIMARY KEY (`id`),
KEY (`create_time`),
KEY (`unit_id`),
KEY (`user_id`),
KEY (`verification_id`),

CONSTRAINT `unit_id_ibfk` FOREIGN KEY (`unit_id`) REFERENCES `internship_unit`(`id`)
ON DELETE CASCADE ON UPDATE CASCADE,

CONSTRAINT `user_id_ibfk` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

/*
INSERT INTO `questionnaires`(id, status, user_id, create_time, coordinator) VALUES
(1, 0, 1, '2020-01-30 09:28:57', 6);
*/

CREATE TABLE `internship_section` (
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
name VARCHAR(45),
tutor_name VARCHAR(40) DEFAULT NULL,
unit_name VARCHAR(40) DEFAULT NULL,
tutor TINYINT DEFAULT 0,
unit TINYINT DEFAULT 0,
number_of_procedures TINYINT DEFAULT 0,
procedures_autonomy TINYINT DEFAULT 0,
theoretical_knowledge TINYINT DEFAULT 0,
practical_knowledge TINYINT DEFAULT 0,
medical_duty TINYINT DEFAULT 0,
ward TINYINT DEFAULT 0,
clinic TINYINT DEFAULT 0,
rating DECIMAL(5,4),
questionnaire_id BIGINT UNSIGNED NOT NULL,
disabled BOOLEAN DEFAULT FALSE,
PRIMARY KEY (`id`),
CONSTRAINT `section_quest_id_ibfk` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaires`(`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `course` (
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
name VARCHAR(30),
tutor_name VARCHAR(40) DEFAULT NULL,
unit_name VARCHAR(40) DEFAULT NULL,
tutor TINYINT DEFAULT 0,
unit TINYINT DEFAULT 0,
theoretical_knowledge TINYINT DEFAULT 0,
practical_knowledge TINYINT DEFAULT 0,
rating DECIMAL(5,4),
questionnaire_id BIGINT UNSIGNED NOT NULL,
disabled BOOLEAN DEFAULT FALSE,
PRIMARY KEY (`id`),
CONSTRAINT `course_quest_id_ibfk` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaires`(`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;


/*
ACL TABLES
*/

CREATE TABLE acl_sid (
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
principal BOOLEAN NOT NULL,
sid VARCHAR(100) NOT NULL,
UNIQUE KEY unique_acl_sid (sid, principal)
) ENGINE=InnoDB;

CREATE TABLE acl_class (
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
class VARCHAR(100) NOT NULL,
UNIQUE KEY uk_acl_class (class)
) ENGINE=InnoDB;

CREATE TABLE acl_object_identity (
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
object_id_class BIGINT UNSIGNED NOT NULL,
object_id_identity BIGINT NOT NULL,
parent_object BIGINT UNSIGNED,
owner_sid BIGINT UNSIGNED,
entries_inheriting BOOLEAN NOT NULL,
UNIQUE KEY uk_acl_object_identity (object_id_class, object_id_identity),
CONSTRAINT fk_acl_object_identity_parent FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id),
CONSTRAINT fk_acl_object_identity_class FOREIGN KEY (object_id_class) REFERENCES acl_class (id),
CONSTRAINT fk_acl_object_identity_owner FOREIGN KEY (owner_sid) REFERENCES acl_sid (id)
) ENGINE=InnoDB;

CREATE TABLE acl_entry (
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
acl_object_identity BIGINT UNSIGNED NOT NULL,
ace_order INTEGER NOT NULL,
sid BIGINT UNSIGNED NOT NULL,
mask INTEGER UNSIGNED NOT NULL,
granting BOOLEAN NOT NULL,
audit_success BOOLEAN NOT NULL,
audit_failure BOOLEAN NOT NULL,
UNIQUE KEY unique_acl_entry (acl_object_identity, ace_order),
CONSTRAINT fk_acl_entry_object FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity (id),
CONSTRAINT fk_acl_entry_acl FOREIGN KEY (sid) REFERENCES acl_sid (id)
) ENGINE=InnoDB;

INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'ping@g.com'),
(2, 1, 'ho@g.com');

INSERT INTO acl_class (id, class) VALUES
(1, 'com.khal.intern_survey.entity.Questionnaire'),
(2, 'com.khal.intern_survey.entity.InternshipUnit');
  
INSERT INTO acl_object_identity 
(id, object_id_class, object_id_identity, 
parent_object, owner_sid, entries_inheriting) 
VALUES
(1, 2, 1, NULL, 1, 0),
(2, 2, 2, NULL, 1, 0);

INSERT INTO acl_entry 
(id, acl_object_identity, ace_order, 
sid, mask, granting, audit_success, audit_failure) 
VALUES
(1, 1, 0, 1, 1, 1, 0, 0),
(2, 1, 1, 1, 2, 1, 0, 0),
(3, 1, 2, 1, 8, 1, 0, 0),
(4, 2, 0, 1, 1, 1, 0, 0),
(5, 2, 1, 1, 2, 1, 0, 0),
(6, 2, 2, 1, 8, 1, 0, 0),
(7, 1, 3, 2, 1, 1, 0, 0),
(8, 1, 4, 2, 2, 1, 0, 0),
(9, 1, 5, 2, 8, 1, 0, 0),
(10, 2, 3, 2, 1, 1, 0, 0),
(11, 2, 4, 2, 2, 1, 0, 0),
(12, 2, 5, 2, 8, 1, 0, 0);
  
