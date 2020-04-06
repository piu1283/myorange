package com.ood.myorange.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Chen on 3/29/20.
 * <p>
 * CREATE TABLE `user_dir` (
 * `dir_id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
 * `user_id` INT(11) NOT NULL COMMENT 'user_id',
 * `parent_id` INT(11)  NOT NULL COMMENT 'dir path',
 * `name` VARCHAR(50) NOT NULL COMMENT 'dir name',
 * `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
 * `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
 * `delete_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'delete time',
 * `deleted` boolean NOT NULL DEFAULT false COMMENT 'is_delete',
 * `default_dir` boolean NOT NULL DEFAULT false COMMENT 'default, every user will have an default dir called root'
 * ) COMMENT 'user dir table';
 */
@Data
@ToString
@Table(name = "user_dir")
@NoArgsConstructor
public class UserDir {
    @Id
    @Column(name = "dir_id")
    private Integer dirId;
    private Integer userId;
    private Integer parentId;
    private String dirName;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Timestamp deleteTime;
    private Boolean deleted;
    private Boolean defaultDir;

    public UserDir(int _dirId) {
        this.dirId = _dirId;
    }

}
