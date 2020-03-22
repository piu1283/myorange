INSERT INTO
`t_user` (`birthday`,`first_name`,`last_name`,`email`,`gender`,`password` )
VALUES
-- password is 123123
('2019-09-30', 'John', 'Wick', 'johnwick123@gmail.com', 1, '$2a$10$BnXQyK65iP5wdnCz.B00aunLi30shp.HohGy4i2JNUEnq9EVemQUm'),
('2019-02-21', 'Bastin', 'Jiber', 'basji33@gmail.com', 2, '$2a$10$BnXQyK65iP5wdnCz.B00aunLi30shp.HohGy4i2JNUEnq9EVemQUm');

INSERT INTO
`t_admin` (`name`, `password`)
VALUES
('admin', '$2a$10$BnXQyK65iP5wdnCz.B00aunLi30shp.HohGy4i2JNUEnq9EVemQUm');

INSERT INTO
`t_permission` (`name`, `desc`)
VALUES
('DOWNLOAD', 'download permission'), ('UPLOAD', 'upload permission');

INSERT INTO
`user_permission` (`user_id`, `permission_id`)
VALUES
(1,1),(1,2),(2,1),(2,2);

INSERT INTO
`t_source` (`name`, `type`, `config`)
VALUES
('myS3_1', 'AWS_S3', 's3_token1'),
('myS3_2', 'AWS_S3', 's3_token1'),
('myAzure_1', 'Azure', 'Azure_token1');