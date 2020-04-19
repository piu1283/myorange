package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.dao.sqlprovider.PermissionSqlProvider;
import com.ood.myorange.pojo.Permission;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Chen on 3/18/20.
 */
public interface PermissionDao extends BaseDao<Permission> {
    @Select("SELECT t.name AS permission_name, t.id AS permission_id " +
            "FROM `t_permission` t, `user_permission` ut " +
            "WHERE t.`id` = ut.`permission_id` AND ut.`user_id`=#{userId}")
    @Results({// column mapping
            @Result(column = "permission_name", property = "permissionName"),
            @Result(column = "permission_id", property = "permissionId"),

    })
    List<Permission> getPermissionByUserId(int userId);

    @Select("SELECT t.name AS permission_name, t.id AS permission_id, ut.user_id AS user_id " +
            "FROM `t_permission` t, `user_permission` ut " +
            "WHERE t.`id` = ut.`permission_id`")
    @Results({// column mapping
            @Result(column = "permission_name", property = "permissionName"),
            @Result(column = "permission_id", property = "permissionId"),
            @Result(column = "user_id", property = "userId")

    })
    List<Permission> getAllUsersPermission();

    @InsertProvider(type = PermissionSqlProvider.class, method = "addUserPermission")
    void addUserPermission(int userId, List<String> permission);

    @InsertProvider(type = PermissionSqlProvider.class, method = "removeUserPermission")
    void removeUserPermission(int userId, List<String> permission);

}
