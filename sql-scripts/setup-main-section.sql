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

