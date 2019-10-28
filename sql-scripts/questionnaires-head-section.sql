DROP TABLE IF EXISTS `questionnaire_head_section`;
CREATE TABLE `questionnaire_head_section` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `unit_name` varchar(50) NOT NULL,
  `coordinator_name` varchar(50) NOT NULL,  
  `coordinator_value` float DEFAULT NULL,                                                                                                                                                                                              
  
  PRIMARY KEY `username_idx_1` (`id`),
  CONSTRAINT `username_ifk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO questionnaire_head_section
(username, unit_name, coordinator_name, coordinator_value) 
values ('john', 'SPWSZ', 'Jan Kowalski', 4),
	   ('mary', 'SPSK1', 'Piort Nowak', 3);

