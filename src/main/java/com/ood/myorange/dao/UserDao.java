package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.pojo.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Chen on 2/21/20.
 */

public interface UserDao extends BaseDao<User> {

    // the BaseDao has already integrate some CRUD function.
    // can put user defined SQL like this
    // the firstName is in the Employee Class
    @Select("SELECT * FROM `t_user` WHERE email=#{email}")
    User getUserByEmail(String email);

    @Select("SELECT * FROM `t_user` WHERE `deleted`=false")
    List<User> getUsers();

    /**
     * delete user by update deleted true.
     * latter will remove this record by script
     */
    @Update("UPDATE `t_user` SET `deleted`=true WHERE `id`=#{userId}")
    void deleteUser(int userId);
}
