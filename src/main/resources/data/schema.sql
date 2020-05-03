DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` INT(11) auto_increment PRIMARY KEY COMMENT 'id',
  `birthday` date COMMENT 'birthday',
  `first_name` varchar(20) NOT NULL COMMENT 'first_name',
  `last_name` varchar(20) NOT NULL COMMENT 'last_name',
  `email` varchar(50) NOT NULL COMMENT 'email should be unique',
  `gender` enum('M','F') NOT NULL DEFAULT 'M' COMMENT 'gender using enum',
  `password` varchar(100) NOT NULL COMMENT 'password, max for 20 char',
  `memory_size` BIGINT(13) UNSIGNED DEFAULT 1073741824 COMMENT 'total space default 1G',
  `used_size` BIGINT(13) UNSIGNED DEFAULT 0 COMMENT 'used space',
  `source_id` INT(11) NOT NULL DEFAULT 1 COMMENT 'upload source id',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `blocked` boolean NOT NULL DEFAULT false COMMENT 'is blocked or not, if yes, user cannot login',
  `deleted` boolean NOT NULL DEFAULT false COMMENT 'is deleted or not, if yes, all the user info and file will be deleted later'
) COMMENT 'user information table';

DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
  `name` varchar(20) NOT NULL UNIQUE COMMENT 'name',
  `desc` varchar(50) COMMENT 'description of the permission',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time'
) COMMENT 'permission information table (download, upload)';

DROP TABLE IF EXISTS `user_permission`;
CREATE TABLE `user_permission` (
  `user_id` INT(11) COMMENT 'id',
  `permission_id` INT(11) COMMENT 'name',
  PRIMARY KEY(`user_id`,`permission_id`)
) COMMENT 'user permission relation information table';

DROP TABLE IF EXISTS `t_source`;
CREATE TABLE `t_source` (
  `id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT 'name, like s3',
  `type` enum('LOCAL', 'AWS', 'AZURE') NOT NULL COMMENT 'storage type',
  `config` TEXT NOT NULL COMMENT 'json config string',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify time',
  `current_use` boolean NOT NULL DEFAULT TRUE COMMENT 'this storage source is using or not'
) COMMENT 'third-party config';

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `class_id` varchar(80) PRIMARY KEY COMMENT 'config class name',
  `config` TEXT NOT NULL COMMENT 'json config string',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify time'
) COMMENT 'system config';

DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin` (
  `id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
  `name` varchar(20) NOT NULL COMMENT 'name',
  `password` varchar(100) NOT NULL COMMENT 'password, max for 50 char',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time'
) COMMENT 'admin information table';

DROP TABLE IF EXISTS `origin_file`;
CREATE TABLE `origin_file` (
  `origin_id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
  `origin_file_id` varchar(70) NOT NULL COMMENT 'origin_file_id',
  `file_md5` CHAR(32) NOT NULL COMMENT 'md5',
  `file_size` BIGINT(10) UNSIGNED NOT NULL COMMENT 'file_size in byte',
  `file_count` SMALLINT UNSIGNED DEFAULT 1 COMMENT 'refer count',
  `source_id` INT(11) NOT NULL DEFAULT 1 COMMENT 'upload source id',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'time created',
  `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
  UNIQUE KEY `origin_file_unique` (`origin_file_id`,`source_id`)
) COMMENT 'original file information table';


DROP TABLE IF EXISTS `t_share`;
CREATE TABLE `t_share` (
    `id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
    `user_id` INT(11) UNSIGNED NOT NULL COMMENT 'owner id',
    `file_id` INT(11) UNSIGNED NOT NULL COMMENT 'shared file id',
    `share_type` enum('NONEPWD', 'PWD') NOT NULL DEFAULT 'NONEPWD' COMMENT 'share type. [none password][password]',
    `share_pass` VARCHAR(30) DEFAULT '' COMMENT 'password',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
	`download_count` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'download_count',
	`download_limitation` INT(11) NOT NULL DEFAULT '-1' COMMENT 'download count limitation, [-1] means infinit',
    `share_deadline` DATETIME DEFAULT "1970-01-01 00:00:00" COMMENT 'expired time, every share must have an expire time',
    `share_key` VARCHAR(255) NOT NULL COMMENT 'string that store in redis as the key',
    UNIQUE KEY `share_unique` (`user_id`,`file_id`),
    UNIQUE KEY `share_key_unique` (`share_key`)
) COMMENT 'share table';

DROP TABLE IF EXISTS `user_file`;
CREATE TABLE `user_file` (
    `file_id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
    `user_id` INT(11) NOT NULL COMMENT 'user_id',
    `dir_id` INT(11) NOT NULL COMMENT 'id of the dir it belongs to',
    `origin_id`INT(11) NOT NULL COMMENT 'origin_file_id',
    `file_size` BIGINT(10) UNSIGNED NOT NULL COMMENT 'file_size in byte',
    `file_name` VARCHAR(255) NOT NULL COMMENT 'file_name',
    `file_type` ENUM ('DOCUMENT','AUDIO','VIDEO','IMG') NOT NULL DEFAULT 'DOCUMENT' COMMENT 'file type',
    `suffixes` varchar(10) NOT NULL DEFAULT 'txt' COMMENT 'file suffixes when it first upload',
    `file_status` ENUM ('NORMAL','SHARED') NOT NULL DEFAULT 'NORMAL' COMMENT 'file_status [normal, shared]',
    `deleted` boolean NOT NULL DEFAULT false COMMENT 'is_delete',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
    `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
    `delete_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'delete time',
    UNIQUE KEY `file_unique` (`dir_id`,`file_name`,`suffixes`,`user_id`, `deleted`)
) COMMENT 'user file table';

DROP TABLE IF EXISTS `user_dir`;
CREATE TABLE `user_dir` (
    `dir_id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
    `user_id` INT(11) NOT NULL COMMENT 'user_id',
    `parent_id` INT(11)  NOT NULL COMMENT 'dir path',
    `dir_name` VARCHAR(50) NOT NULL COMMENT 'dir name',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
    `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
    `delete_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'delete time',
	`deleted` boolean NOT NULL DEFAULT false COMMENT 'is_delete',
	`default_dir` boolean NOT NULL DEFAULT false COMMENT 'default, every user will have an default dir called root',
	UNIQUE KEY `dir_unique` (`dir_name`,`parent_id`,`user_id`)
) COMMENT 'user dir table';