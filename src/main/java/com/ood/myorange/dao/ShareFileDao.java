package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.constant.enumeration.ShareType;
import com.ood.myorange.pojo.ShareFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.sql.Timestamp;
import java.util.List;


/**
 * Created by Linkun on 3/19/2020
 * <p>
 * CREATE TABLE `t_share` (
 * `id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
 * `user_id` INT(11) UNSIGNED NOT NULL COMMENT 'owner id',
 * `file_id` INT(11) UNSIGNED NOT NULL COMMENT 'shared file id',
 * `share_type` enum('NONEPWD', 'PWD') NOT NULL DEFAULT 'NONEPWD' COMMENT 'share type. [none password][password]',
 * `share_pass` VARCHAR(30) DEFAULT '' COMMENT 'password',
 * `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
 * `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
 * `download_count` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'download_count',
 * `download_limitation` INT(11) NOT NULL DEFAULT '-1' COMMENT 'download count limitation, [-1] means infinit',
 * `share_deadline` DATETIME DEFAULT "1970-01-01 00:00:00" COMMENT 'expired time, every share must have an expire time',
 * `share_key` VARCHAR(255) NOT NULL COMMENT 'string that store in redis as the key',
 * `share_url` VARCHAR(225) DEFAULT '' COMMENT 'share url of front end',
 * UNIQUE KEY `share_unique` (`user_id`,`file_id`),
 * UNIQUE KEY `share_key_unique` (`share_key`)
 * ) COMMENT 'share table';
 */
public interface ShareFileDao extends BaseDao<ShareFile> {

    @Select("SELECT * FROM `t_share` WHERE share_key=#{shareKey}")
    ShareFile SelectShareFileByShareKey(String shareKey);

    @Select("SELECT * FROM `t_share` WHERE file_id=#{fileId}")
    ShareFile SelectShareFileByFileId(Integer fileId);

    @Select("SELECT * FROM `t_share` WHERE id=#{Id}")
    ShareFile SelectShareFileByShareId(Integer Id);

    @Select("SELECT * FROM `t_share` WHERE id=#{share_id} AND CURRENT_TIMESTAMP < share_deadline")
    ShareFile SelectShareFileIfDeadlineNotExpired(Integer shareId);

    @Select("SELECT * FROM `t_share`")
    List<ShareFile> SelectAllShareFileInfo();

    @Delete("DELETE FROM `t_share` WHERE id = #{shareId}")
    void deleteShareFile(int shareId);

    @Insert("INSERT into `t_share` (user_id,file_id,share_type,share_pass,share_deadline,share_key,share_url) VALUES(#{userId},#{fileId},#{shareType},#{sharePass},#{deadline},#{shareKey},#{shareUrl})")
    void insertShareFile(Integer userId, Integer fileId, ShareType shareType, String sharePass, Timestamp deadline, String shareKey, String shareUrl);

    @Update("UPDATE `t_share` SET share_deadline = #{deadline}, download_limitation = #{limitDownloadTimes}, share_type=#{shareType} WHERE id = #{shareId}")
    void updateShareFileById(int shareId, Timestamp deadline, int limitDownloadTimes, ShareType shareType);
}