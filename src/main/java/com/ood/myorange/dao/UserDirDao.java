package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.dao.sqlprovider.FileDirSqlProvider;
import com.ood.myorange.pojo.UserDir;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Chen on 3/29/20.
 *
 * DROP TABLE IF EXISTS `user_dir`;
 * CREATE TABLE `user_dir` (
 *     `dir_id` INT(11) auto_increment PRIMARY KEY  COMMENT 'id',
 *     `user_id` INT(11) NOT NULL COMMENT 'user_id',
 *     `parent_id` INT(11)  NOT NULL COMMENT 'dir path',
 *     `dir_name` VARCHAR(50) NOT NULL COMMENT 'dir name',
 *     `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create_time',
 *     `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'modify_time',
 *     `delete_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'delete time',
 * 	`deleted` boolean NOT NULL DEFAULT false COMMENT 'is_delete',
 * 	`default_dir` boolean NOT NULL DEFAULT false COMMENT 'default, every user will have an default dir called root'
 * ) COMMENT 'user dir table';
 */
public interface UserDirDao extends BaseDao<UserDir> {

    @Select("SELECT dir_id, dir_name FROM `user_dir` " +
            "WHERE `dir_name` like #{keyword} " +
            "AND deleted=false " +
            "LIMIT 10")
    List<UserDir> searchUserDirByKeyWord(String keyword);

    @Select("SELECT * FROM `user_dir` " +
            "WHERE `parent_id`=#{parentId} AND `deleted`=false ")
    List<UserDir> getUserDirUnderTarget(int parentId);

    @Select("SELECT * FROM `user_dir` " +
            "WHERE `user_id`=#{userId} AND default_dir=true")
    UserDir getRootDir(int userId);

    @SelectProvider(type = FileDirSqlProvider.class, method = "checkDirsByIdAndUserId")
    @ResultType(Integer.class)
    List<Integer> checkDirsByIdAndUserId(List<Integer> dirIds, int userId);

    @Update("UPDATE `user_dir` " +
            "SET `parent_id`=#{targetId} " +
            "WHERE `dir_id`=#{dirId}")
    void updateParentIdOfDir(Integer dirId, int targetId);

    /**
     * recursively "delete" dir and all of its children.
     * not real deletion, only set deleted to true
     * @param dirId target dir id
     */
    @UpdateProvider(type = FileDirSqlProvider.class, method = "deleteDirAndItsChildByUpdate")
    void deleteDirAndItsChildByUpdate(int dirId);

    /**
     * check whether A_dir is a child of B_dir
     * @param BId
     * @param AId
     * @return if A_dir is under B_dir, return userDir of A_dir
     */
    @SelectProvider(type = FileDirSqlProvider.class, method = "checkDirIsUnderTarget")
    UserDir checkDirAIsUnderB(int BId, int AId);

}
