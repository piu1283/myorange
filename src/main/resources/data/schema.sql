DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `birthday` date NOT NULL COMMENT 'birthday',
  `first_name` varchar(20) NOT NULL COMMENT 'first_name',
  `last_name` varchar(20) NOT NULL COMMENT 'last_name',
  `email` varchar(50) NOT NULL COMMENT 'email should be unique',
  `gender` enum('M','F') NOT NULL COMMENT 'gender using enum',
  `password` varchar(20) NOT NULL COMMENT 'password, max for 20 char',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
