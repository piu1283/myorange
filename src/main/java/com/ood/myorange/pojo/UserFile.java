package com.ood.myorange.pojo;

import com.ood.myorange.constant.enumeration.FileStatus;
import com.ood.myorange.constant.enumeration.FileType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Chen on 3/29/20.
 * CREATE TABLE `user_file` (
 *     `file_id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
 *     `user_id` INT(11) NOT NULL COMMENT 'user_id',
 *     `dir_id` INT(11) NOT NULL COMMENT 'id of the dir it belongs to',
 *     `origin_id`INT(11) NOT NULL COMMENT 'origin_file_id',
 *     `file_name` VARCHAR(255) NOT NULL COMMENT 'file_name',
 *     `file_type` ENUM ('DOCUMENT','AUDIO','VIDEO','IMG') NOT NULL DEFAULT 'DOCUMENT' COMMENT 'file type',
 *     `suffixes` varchar(10) NOT NULL DEFAULT 'txt' COMMENT 'file suffixes when it first upload',
 *     `file_status` ENUM ('NORMAL','DELETED','SHARED') NOT NULL DEFAULT 'NORMAL' COMMENT 'file_status [normal,delete, shared]',
 *     `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
 *     `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
 *     `deleted` boolean DEFAULT false COMMENT 'file is delete or not',
 *     `delete_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'delete time'
 * ) COMMENT 'user file table';
 */
@Data
@ToString
@Table(name = "user_file")
@NoArgsConstructor
public class UserFile {
    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator="JDBC")
    private Integer fileId;
    private Integer userId;
    private Integer dirId;
    private Integer originId;
    private String fileName;
    private FileType fileType;
    private Long fileSize;
    private String suffixes;
    private FileStatus fileStatus;
    private Boolean deleted;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private Timestamp deleteTime;

    public UserFile(int _fileId) {
        this.fileId = _fileId;
    }
}
