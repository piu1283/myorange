package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.dao.sqlprovider.FileDirSqlProvider;
import com.ood.myorange.pojo.UserFile;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Created by Chen on 3/29/20.
 *
 * DROP TABLE IF EXISTS `user_file`;
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
 *     `delete_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'delete time'
 * ) COMMENT 'user file table';
 */

public interface UserFileDao extends BaseDao<UserFile> {

    @SelectProvider(type = FileDirSqlProvider.class, method = "searchByNameAndTypeSQL")
    List<UserFile> getFileByKeyWordAndType(String keyword, FileType fileType);

    @Select("SELECT * FROM `user_file` " +
            "WHERE dir_id=#{dirId} AND deleted=false")
    List<UserFile> getFilesByTargetDir(int dirId);

    @SelectProvider(type = FileDirSqlProvider.class, method = "checkFilesByIdAndUserId")
    @ResultType(Integer.class)
    List<Integer> checkFilesByIdAndUserId(List<Integer> dirIds, int userId);

    /**
     * recursively "delete" files of one dir and all its children.
     * not real deletion, only set deleted to true
     * @param dirId target dir id
     */
    @UpdateProvider(type = FileDirSqlProvider.class, method = "deleteFilesUnderDirAndItsChildrenByUpdate")
    void deleteFilesUnderDirAndItsChildrenByUpdate(int dirId);

    @SelectProvider(type = FileDirSqlProvider.class, method = "updateParentIdOfFiles")
    void updateParentIdOfFiles(List<Integer> fileIds, int targetId);

    /**
     * delete a file
     * not real deletion, only set deleted to true
     *
     * @param fileId target dir id
     */
    @UpdateProvider(type = FileDirSqlProvider.class, method = "deleteFile")
    void deleteFile(int fileId);
}
