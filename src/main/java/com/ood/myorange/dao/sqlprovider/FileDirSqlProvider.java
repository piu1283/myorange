package com.ood.myorange.dao.sqlprovider;

import com.ood.myorange.constant.enumeration.FileType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
public class FileDirSqlProvider {
    private static final String TEMP_RESULT_TABLE_NAME = "tmp";

    public String searchByNameAndTypeSQL(String keyword, FileType fileType) {
        SQL sql = new SQL()
                .SELECT("file_id, file_name, file_type, suffixes")
                .FROM("user_file")
                .WHERE("deleted=false");
        if (!StringUtils.isBlank(keyword)) {
            sql.AND();
            sql.WHERE("file_name like #{keyword}");
        }
        if (fileType != null) {
            sql.AND();
            sql.WHERE("file_type=#{fileType}");
        }
        sql.LIMIT(10);
        return sql.toString();
    }

    public String checkDirsByIdAndUserId(List<Integer> dirIds, int userId) {
        SQL sql = new SQL();
        sql.SELECT("dir_id")
                .FROM("user_dir")
                .WHERE("deleted=true")
                .AND()
                .WHERE("user_id!=#{userId}")
                .AND()
                .WHERE("default_dir=true")
                .AND()
                .WHERE("dir_id in (" + StringUtils.join(dirIds, ",") + ")");
        return sql.toString();
    }

    public String checkFilesByIdAndUserId(List<Integer> dirIds, int userId) {
        SQL sql = new SQL();
        sql.SELECT("file_id")
                .FROM("user_file")
                .WHERE("deleted=true")
                .AND()
                .WHERE("user_id=#{userId}")
                .AND()
                .WHERE("file_id in (" + StringUtils.join(dirIds, ",") + ")");
        return sql.toString();
    }



    public String updateParentIdOfFiles(List<Integer> fileIds, int targetId) {
        SQL sql = new SQL();
        sql.UPDATE("user_file")
                .SET("dir_id=#{targetId}")
                .WHERE("file_id in (" + StringUtils.join(fileIds, ",") + ")");
        return sql.toString();
    }

    public String checkDirIsUnderTarget(int BId, int AId) {
        SQL sql = new SQL();
        sql.SELECT("dir_id, user_id, parent_id, dir_name")
                .FROM(TEMP_RESULT_TABLE_NAME)
                .WHERE("dir_id=#{AId}");
        return getAllChildDirIncludeItself(BId) + sql.toString();
    }

    public String deleteDirAndItsChildByUpdate(int dirId) {
        SQL sql = new SQL();
        sql.UPDATE("user_dir")
                .SET("deleted=true")
                .SET("delete_time='" + new Timestamp(System.currentTimeMillis()) + "'")
                .WHERE("user_dir.dir_id in (" + getAllChildDirIncludeItself(dirId) + " Select dir_id from " + TEMP_RESULT_TABLE_NAME + ")");
        return sql.toString();
    }

    public String deleteFilesUnderDirAndItsChildrenByUpdate(int dirId) {
        SQL sql = new SQL();
        sql.UPDATE("user_file")
                .SET("deleted=true")
                .SET("delete_time='" + new Timestamp(System.currentTimeMillis()) + "'")
                .WHERE("user_file.dir_id in (" + getAllChildDirIncludeItself(dirId) + " Select dir_id from " + TEMP_RESULT_TABLE_NAME + ")");
        return sql.toString();
    }

    public String deleteFile(int fileId) {
        SQL sql = new SQL();
        sql.UPDATE("user_file")
                .SET("deleted=true")
                .SET("delete_time='" + new Timestamp(System.currentTimeMillis()) + "'")
                .WHERE("file_id=#{fileId}");
        return sql.toString();
    }


    private String getAllChildDirIncludeItself(int dirId) {
        return "WITH RECURSIVE " + TEMP_RESULT_TABLE_NAME + " (`dir_id`, `user_id`, `parent_id`, `dir_name`) AS \n" +
                "(SELECT `dir_id`, `user_id`, `parent_id`, `dir_name` FROM `user_dir` WHERE `dir_id`=" + dirId + " \n" +
                "union all \n" +
                "SELECT A.`dir_id`, A.`user_id`, A.`parent_id`, A.`dir_name` FROM `user_dir` A, " + TEMP_RESULT_TABLE_NAME + " B WHERE A.`parent_id`=B.`dir_id`) \n";
    }
}
