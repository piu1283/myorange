package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.pojo.OriginalFile;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Chen on 4/11/20.
 *
 * CREATE TABLE `origin_file` (
 * ``
 *   `origin_file_id` varchar(70) PRIMARY KEY COMMENT 'origin_file_id',
 *   `file_md5` CHAR(32) NOT NULL COMMENT 'md5',
 *   `file_size` BIGINT(20) UNSIGNED NOT NULL COMMENT 'file_size in byte',
 *   `file_count` SMALLINT UNSIGNED DEFAULT 1 COMMENT 'refer count',
 *   `source_id` INT(11) NOT NULL DEFAULT 1 COMMENT 'upload source id',
 *   `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'time created',
 *   `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time'
 * ) COMMENT 'original file information table';
 */
public interface OriginalFileDao extends BaseDao<OriginalFile> {

    @Select("SELECT * FROM " +
            "user_file u LEFT JOIN origin_file o ON u.origin_id=o.origin_id " +
            "WHERE u.file_id=#{fileId}")
    OriginalFile getByFileId(int fileId);

}
