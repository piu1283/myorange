package com.ood.myorange.dao;

import com.ood.myorange.config.BaseDao;
import com.ood.myorange.pojo.Admin;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Chen on 3/17/20.
 */
public interface AdminDao extends BaseDao<Admin> {
    @Select("SELECT * FROM `t_admin` WHERE name=#{name}")
    Admin getAdminByName(String name);
}
