DROP DATABASE IF EXISTS `internship_survey`;

CREATE DATABASE IF NOT EXISTS `internship_survey`;
USE `internship_survey`;

CREATE TABLE `users` (
`id` int NOT NULL AUTO_INCREMENT,
`email` varchar(50) NOT NULL,
`password` char(68) NOT NULL,
`enabled` boolean NOT NULL DEFAULT FALSE,
PRIMARY KEY (`id`),
UNIQUE KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `verification_token` (
`id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`expiry_date` date NOT NULL,
`token` varchar (50) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `verification_token_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `password_reset_token` (
`id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`expiry_date` date NOT NULL,
`token` varchar (50) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `reset_token_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `email_update_token` (
`id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`expiry_date` date NOT NULL,
`token` varchar (50) NOT NULL,
`new_email` varchar (30) NOT NULL,
PRIMARY KEY (`id`),
CONSTRAINT `email_update_token_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `admin_personal_data` (
`id` int NOT NULL AUTO_INCREMENT,
`first_name` varchar(50) NOT NULL,
`last_name` varchar(50) NOT NULL,
`phone_number` varchar(15) NOT NULL,
`medical_chamber` varchar(50),
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `user_admin_data` (
`user_id` int NOT NULL,
`admin_data_id` int NOT NULL,
PRIMARY KEY (`user_id`, `admin_data_id`),
CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT `admin_data_fk` FOREIGN KEY (`admin_data_id`) REFERENCES `admin_personal_data` (`id`)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- test password: x

INSERT INTO `users` 
VALUES 
(1,'xi@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true),
(2,'jin@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true),
(3,'ping@g.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true),
(4,'krrsssfire@gmail.com','{bcrypt}$2a$10$BSEX1pxjulNZcFCbsxb5mufJUhW1bQ8Yw5Tulyp7gjR1LhnkpWu8S',true);

INSERT INTO `admin_personal_data`
VALUES
(1,'Xi','Ho','123456789','OIL w Szczecinie'),
(2,'Jin','Po','987654321','OIL w Szczecinie');

INSERT INTO `user_admin_data`
VALUES
(1,1),
(2,2);

CREATE TABLE `role` (
`id` int NOT NULL AUTO_INCREMENT,
`name` varchar(50) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


INSERT INTO `role` (name)
VALUES 
('ROLE_USER'),('ROLE_OILADMIN'),('ROLE_ADMIN');


CREATE TABLE `users_roles` (
`user_id` int NOT NULL,
`role_id` int NOT NULL,
  
PRIMARY KEY (`user_id`,`role_id`),
KEY (`role_id`),
  
CONSTRAINT `users_roles_user_fk` FOREIGN KEY (`user_id`) 
REFERENCES `users` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE,
  
CONSTRAINT `users_roles_role_fk` FOREIGN KEY (`role_id`) 
REFERENCES `role` (`id`) 
ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO `users_roles` (user_id,role_id)
VALUES 
(1, 1),
(2, 1),
(2, 2),
(3, 2),
(3, 3);


CREATE TABLE `questionnaires` (
`id` int NOT NULL AUTO_INCREMENT,
`status` varchar(15) DEFAULT 'draft',
`medical_chamber` varchar(50),
`unit_name` varchar(150),                                                                                                                                                                                               
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `users_questionnaires` (
`user_id` int NOT NULL,
`questionnaire_id` int NOT NULL,
PRIMARY KEY (`user_id`,`questionnaire_id`),
  
CONSTRAINT `users_quest_user_fk` FOREIGN KEY (`user_id`) 
REFERENCES `users` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE,
  
CONSTRAINT `users_quest_quest_fk` FOREIGN KEY (`questionnaire_id`) 
REFERENCES `questionnaires` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `internship_unit` (
`id` int NOT NULL AUTO_INCREMENT,
`name` varchar(150),
`medical_chamber` varchar(50),
PRIMARY KEY (`id`)
);

INSERT INTO `internship_unit` VALUES
(1,'Samodzielny Publiczny Szpital Kliniczny Nr 1 w Szczecinie','SZCZECIN'),
(2,'SPSK Nr 2 w Szczecinie','SZCZECIN'),
(3,'ZOZ MSWIA w Warszawie','WARSZAWA'),
(4,'111 Szpital Wojskowy w Poznaniu','POZNAN');

