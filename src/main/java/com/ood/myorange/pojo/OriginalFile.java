package com.ood.myorange.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Chen on 4/11/20.
 *
 * DROP TABLE IF EXISTS `origin_file`;
 * CREATE TABLE `origin_file` (
 *   `origin_file_id` varchar(70) PRIMARY KEY COMMENT 'origin_file_id',
 *   `file_md5` CHAR(32) NOT NULL COMMENT 'md5',
 *   `file_size` BIGINT(20) UNSIGNED NOT NULL COMMENT 'file_size in byte',
 *   `file_count` SMALLINT UNSIGNED DEFAULT 1 COMMENT 'refer count',
 *   `source_id` INT(11) NOT NULL DEFAULT 1 COMMENT 'upload source id',
 *   `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'time created',
 *   `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time'
 * ) COMMENT 'original file information table';
 *
 */
@Data
@ToString
@Table(name = "origin_file")
@NoArgsConstructor
public class OriginalFile {
    @Id
    @Column(name = "origin_id")
    private Integer originId;
    private String originFileId;
    private String fileMd5;
    private Long fileSize;
    private Integer fileCount;
    private Integer source_id;
    private Timestamp createTime;
    private Timestamp modifyTime;
}
