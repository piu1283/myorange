package com.ood.myorange.pojo;

import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.constant.enumeration.ShareType;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Linkun on 4/13/20.
 *
 * DROP TABLE IF EXISTS `t_share`;
 * CREATE TABLE `t_share` (
 *     `id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
 *     `user_id` INT(11) UNSIGNED NOT NULL COMMENT 'owner id',
 *     `file_id` INT(11) UNSIGNED NOT NULL COMMENT 'shared file id',
 *     `share_type` enum('NONEPWD', 'PWD') NOT NULL DEFAULT 'NONEPWD' COMMENT 'share type. [none password][password]',
 *     `share_pass` VARCHAR(30) DEFAULT '' COMMENT 'password',
 *     `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
 * 	`modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
 * 	`download_count` INT(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'download_count',
 * 	`download_limitation` INT(11) NOT NULL DEFAULT '-1' COMMENT 'download count limitation, [-1] means infinit',
 *     `share_deadline` DATETIME DEFAULT "1970-01-01 00:00:00" COMMENT 'expired time, every share must have an expire time',
 *     `share_key` VARCHAR(255) NOT NULL COMMENT 'string that store in redis as the key',
 *     `share_url` VARCHAR(225) DEFAULT '' COMMENT 'share url of front end',
 *     UNIQUE KEY `share_unique` (`user_id`,`file_id`),
 *     UNIQUE KEY `share_key_unique` (`share_key`)
 * ) COMMENT 'share table';
 */
@Data
@ToString
@Table(name = "t_share")
public class ShareFile {
    @Id
    @Column(name = "id")
    private int id;
    private int userId;
    private int fileID;
    private ShareType shareType;
    private String sharePass;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private int downloadCount;
    private int downloadLimitation;
    private Timestamp shareDeadline;
    private String shareKey;
    private String shareUrl;

}
