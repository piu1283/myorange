package com.ood.myorange.service.impl;

import com.ood.myorange.dao.PermissionDao;
import com.ood.myorange.pojo.Permission;
import com.ood.myorange.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chen on 3/18/20.
 */
@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Autowired
    PermissionDao permissionDao;

    @Override
    public List<Permission> getPermissionListByUserId(int userId) {
        return permissionDao.getPermissionByUserId(userId);
    }
}
