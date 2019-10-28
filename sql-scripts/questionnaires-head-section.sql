DROP TABLE IF EXISTS `questionnaire_head_section`;
CREATE TABLE `questionnaire_head_section` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `status` varchar(15),
  `unit_name` varchar(50) NOT NULL,
                                                                                                                                                                                             
  
  PRIMARY KEY `username_idx_1` (`id`),
  CONSTRAINT `username_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO questionnaire_head_section
(username, status, unit_name) 
values ('john', 'draft', 'SPWSZ'),
	   ('mary', 'draft', 'SPSK1');

