DROP TABLE IF EXISTS `internal_medicine`;
CREATE TABLE `internal_medicine` (
  `id` int NOT NULL,
  `coordinator_name` varchar(50) NOT NULL,  
  `coordinator_value` float DEFAULT NULL, 
  `tutor_name` varchar(50) DEFAULT NULL,
  `tutor_value` float DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `ward` float DEFAULT NULL,
  `clinic` float DEFAULT NULL,
  `number_of_conducted_procedures` float DEFAULT NULL,
  `self_reliance_of_conducted_procedures` float DEFAULT NULL,
  `acquired_teoretical_knowledge` float DEFAULT NULL,
  `acquired_practical_knowledge` float DEFAULT NULL,
  `surgery` float DEFAULT NULL,
  
  PRIMARY KEY `username_idx_1` (`id`),
  CONSTRAINT `username_ibfk_2` FOREIGN KEY (`id`) REFERENCES `questionnaire_head_section` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;