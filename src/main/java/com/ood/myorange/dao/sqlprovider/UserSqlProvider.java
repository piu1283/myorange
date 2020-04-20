package com.ood.myorange.dao.sqlprovider;

import org.apache.ibatis.jdbc.SQL;

import java.sql.Timestamp;

/**
 * Created by Chen on 4/18/20.
 */
public class UserSqlProvider {
    public String deleteUser(int userId) {
        SQL sql = new SQL();
        sql.UPDATE("t_user")
                .SET("deleted=true")
                .SET("delete_time='" + new Timestamp(System.currentTimeMillis()) + "'")
                .WHERE("id=#{userId}");
        return sql.toString();
    }

}
