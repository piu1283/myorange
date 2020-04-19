package com.ood.myorange.pojo;


import com.ood.myorange.constant.enumeration.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Chen on 2/21/20.
 *
 * DROP TABLE IF EXISTS `t_user`;
 * CREATE TABLE `t_user` (
 *   `id` INT(11) auto_increment PRIMARY KEY COMMENT 'id',
 *   `birthday` date NOT NULL COMMENT 'birthday',
 *   `first_name` varchar(20) NOT NULL COMMENT 'first_name',
 *   `last_name` varchar(20) NOT NULL COMMENT 'last_name',
 *   `email` varchar(50) NOT NULL COMMENT 'email should be unique',
 *   `gender` enum('M','F') NOT NULL COMMENT 'gender using enum',
 *   `password` varchar(100) NOT NULL COMMENT 'password, max for 20 char',
 *   `memory_size` BIGINT(13) UNSIGNED DEFAULT 1073741824 COMMENT 'total space default 1G',
 *   `used_size` BIGINT(13) UNSIGNED DEFAULT 0 COMMENT 'used space',
 *   `source_id` INT(11) NOT NULL DEFAULT 1 COMMENT 'upload source id',
 *   `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
 *   `blocked` boolean NOT NULL DEFAULT false COMMENT 'is blocked or not, if yes, user cannot login',
 *   `blocked` boolean NOT NULL DEFAULT false
 * ) COMMENT 'user information table';
 */
@Data
@ToString
@Table(name = "t_user")
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator="JDBC")
    private Integer id;
    private Date birthday;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String password;
    private Long memorySize;
    private Long usedSize;
    private Integer sourceId;
    private Timestamp createdTime;
    private Boolean blocked;
    private Boolean deleted;

    public User(int userId) {
        this.id = userId;
    }

    public boolean isBlockedOrNot(){
        return blocked == null ? false : blocked;
    }

    public boolean isDeletedOrNot(){
        return deleted == null ? false : deleted;
    }
}



