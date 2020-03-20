package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.pojo.User;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Chen on 2/21/20.
 */

public interface UserDao extends BaseDao<User> {

    // the BaseDao has already integrate some CRUD function.
    // can put user defined SQL like this
    // the firstName is in the Employee Class
    @Select("SELECT * FROM `t_user` WHERE email=#{email}")
    User getUserByEmail(String email);
}
