-- user table
INSERT INTO
`t_user` (`birthday`,`first_name`,`last_name`,`email`,`gender`,`password`,`used_size` )
VALUES
-- password is 123123
('2019-09-30', 'John', 'Wick', 'johnwick123@gmail.com', 1, '$2a$10$BnXQyK65iP5wdnCz.B00aunLi30shp.HohGy4i2JNUEnq9EVemQUm', 107080),
('2019-02-21', 'Bastin', 'Jiber', 'basji33@gmail.com', 2, '$2a$10$BnXQyK65iP5wdnCz.B00aunLi30shp.HohGy4i2JNUEnq9EVemQUm', 83421);

-- admin table
INSERT INTO
`t_admin` (`name`, `password`)
VALUES
('admin', '$2a$10$BnXQyK65iP5wdnCz.B00aunLi30shp.HohGy4i2JNUEnq9EVemQUm');

-- permission
INSERT INTO
`t_permission` (`name`, `desc`)
VALUES
('DOWNLOAD', 'download permission'), ('UPLOAD', 'upload permission');

-- user_permission
INSERT INTO
`user_permission` (`user_id`, `permission_id`)
VALUES
(1,1),(1,2),(2,1),(2,2);

-- user_file

INSERT INTO
`user_file`(`user_id`,`dir_id`,`origin_id`,`file_size`,`file_name`,`file_type`,`suffixes`)
VALUES
(1, 1, 1, 3456324, "file_", 'DOCUMENT' , "txt"),
(1, 2, 2, 83421, "file_1a_a", 'VIDEO' , "avi"),
(1, 3, 2, 83421, "file_1b_a", 'VIDEO' , "avi"),
(1, 4, 3, 23425, "file_2a_a", 'AUDIO', 'mp3'),
(1, 4, 4, 234, "file_2a_b", 'AUDIO', 'mp3');

-- user_dir

INSERT INTO `user_dir`(`user_id`,`parent_id`,`dir_name`,`default_dir`)
VALUES
-- id start with 1
-- 1:{2:{4:{6},5},3}
(1, 0, "/", true),(1, 1, "level_1a", false),(1, 1, "level_1b", false), (1, 2, "level_2a", false),(1, 2, "level_2b", false),(1, 4, "level_4a", false);

INSERT INTO `user_dir`(`user_id`,`parent_id`,`dir_name`,`default_dir`)
VALUES
-- id start with 7
(2, 0, "/", true),(2, 7, "level_1a", false),(2, 7, "level_1b", false), (2, 7, "level_2a", false);

-- source
INSERT INTO
`t_source` (`name`, `type`, `config`)
VALUES
(
    'myS3',
    'AWS',
    '{"aws_access_key_id": "keyid1","aws_secret_access_key" : "accesskey1","aws_region" : "us-east-2","aws_bucket_name" : "my-bucket-glai-01"}'
),
(
    'myAzure',
    'AZURE',
    '{"azure_token" : "azureazure123456"}'
);

-- original file
INSERT INTO `origin_file`(`origin_file_id`,`file_md5`,`file_size`,`file_count`,`source_id`)
VALUES("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA_3456324", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 3456324, 1, 1),
("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB_83421", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB", 83421, 2, 1),
("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC_23425", "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", 23425, 1, 1),
("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD_234", "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC", 234, 1, 1);


-- sysConfig for mail
INSERT INTO `sys_config`(`class_id`,`config`)
VALUES ('com.ood.myorange.config.sys.MailConfig', '{\"mail_address\":\"myorange098@gmail.com\",\"host\":\"smtp.gmail.com\",\"port\":587,\"password\":\"Myorange123\",\"sender\":\"mo\"}');

