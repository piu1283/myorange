package com.ood.myorange.dao.sqlprovider;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Chen on 4/17/20.
 */
public class PermissionSqlProvider {
    public String addUserPermission(int userId, List<String> permission) {
        List<String> collect = permission.stream().map(s -> "'" + s + "'").collect(Collectors.toList());
        return  "INSERT IGNORE INTO user_permission (user_id, permission_id) " +
                "SELECT " + userId + ", id from t_permission " +
                "WHERE t_permission.name in (" + StringUtils.join(collect, ",") + ")";
    }

    public String removeUserPermission(int userId, List<String> permission) {
        List<String> collect = permission.stream().map(s -> "'" + s + "'").collect(Collectors.toList());
        SQL sql = new SQL();
        sql.DELETE_FROM("user_permission")
                .WHERE("user_id=#{userId}")
                .AND()
                .WHERE("permission_id in (SELECT id FROM t_permission WHERE name in (" + StringUtils.join(collect, ",") + "))");
        return sql.toString();
    }
}
