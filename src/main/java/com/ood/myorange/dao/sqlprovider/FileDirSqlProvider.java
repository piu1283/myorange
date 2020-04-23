package com.ood.myorange.dao.sqlprovider;

import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.pojo.OriginalFile;
import com.ood.myorange.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
        sql.UPDATE("user_file AS u, t_user AS t")
                .SET("u.deleted=true")
                .SET("u.delete_time='" + TimeUtil.getCurrentTimeStamp() + "'")
                .SET("t.used_size=IF((cast(t.used_size as signed)-cast(u.file_size as signed))>=0, t.used_size - u.file_size, 0)")
                .WHERE("u.user_id=t.id")
                .AND()
                .WHERE("u.dir_id in (" + getAllChildDirIncludeItself(dirId) + " Select dir_id from " + TEMP_RESULT_TABLE_NAME + ")");
        return sql.toString();
    }

    public String deleteFile(int fileId) {
        SQL sql = new SQL();
        sql.UPDATE("user_file AS u, t_user AS t")
                .SET("u.deleted=true")
                .SET("u.delete_time='" + TimeUtil.getCurrentTimeStamp() + "'")
                .SET("t.used_size=IF((cast(t.used_size as signed)-cast(u.file_size as signed))>=0, t.used_size - u.file_size, 0)")
                .WHERE("u.file_id=#{fileId}");
        return sql.toString();
    }

    public String getAllFileIdUnderDir(int dirId){
        SQL sql = new SQL();
        sql.SELECT("u.file_id")
                .FROM("user_file u INNER JOIN "+TEMP_RESULT_TABLE_NAME+" t ON u.dir_id=t.dir_id");
        System.out.println(getAllChildDirIncludeItself(dirId) + sql.toString());
        return getAllChildDirIncludeItself(dirId) + sql.toString();
    }

    public String deleteFilesByFileIds(Map<String, Object> parameters) {
        List<Integer> fileIds = (List<Integer>) parameters.get("fileIds");
        SQL sql = new SQL();
        sql.UPDATE("user_file")
                .SET("deleted=true")
                .SET("delete_time='" + TimeUtil.getCurrentTimeStamp() + "'")
                .WHERE("file_id in (" + StringUtils.join(fileIds, ",") + ")");
        return sql.toString();
    }

    public String updateUsedSizeDecreaseByFileIds(List<Integer> fileIds, int userId) {
        SQL sql = new SQL();
        sql.UPDATE("t_user t, (SELECT SUM(file_size) size FROM user_file WHERE file_id in (" + StringUtils.join(fileIds, ",") + ")) u")
                .SET("t.used_size = IF(cast(t.used_size as signed) - cast(u.size as signed)>=0, cast(t.used_size as signed) - cast(u.size as signed), 0)")
                .WHERE("t.id=#{userId}");
        return sql.toString();
    }


    private String getAllChildDirIncludeItself(int dirId) {
        return "WITH RECURSIVE " + TEMP_RESULT_TABLE_NAME + " (`dir_id`, `user_id`, `parent_id`, `dir_name`) AS \n" +
                "(SELECT `dir_id`, `user_id`, `parent_id`, `dir_name` FROM `user_dir` WHERE `dir_id`=" + dirId + " \n" +
                "union all \n" +
                "SELECT A.`dir_id`, A.`user_id`, A.`parent_id`, A.`dir_name` FROM `user_dir` A, " + TEMP_RESULT_TABLE_NAME + " B WHERE A.`parent_id`=B.`dir_id`) \n";
    }

    public String insertOrUpdateOriginFile(OriginalFile originalFile) {
        return "INSERT INTO `origin_file` (`origin_file_id`, `file_md5`, `file_size`,`file_count`,`source_id`)\n" +
                "VALUES(#{originFileId},#{fileMd5},#{fileSize},#{fileCount},#{sourceId}) \n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "`file_count`=`file_count`+1\n";
    }
}
