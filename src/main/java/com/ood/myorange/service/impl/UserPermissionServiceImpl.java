package com.ood.myorange.service.impl;

import com.ood.myorange.dao.PermissionDao;
import com.ood.myorange.pojo.Permission;
import com.ood.myorange.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<Integer, List<String>> getAllPermission() {
        List<Permission> allUsersPermission = permissionDao.getAllUsersPermission();
        Map<Integer, List<String>> res = new HashMap<>();
        for (Permission permission : allUsersPermission) {
            if (res.containsKey(permission.getUserId())) {
                res.get(permission.getUserId()).add(permission.getPermissionName());
            } else {
                List<String> permissionList = new ArrayList<>();
                permissionList.add(permission.getPermissionName());
                res.put(permission.getUserId(), permissionList);
            }
        }
        return res;
    }

    @Override
    @Transactional
    public void changePermission(int userId, List<String> obtainList, List<String> remove) {
        if (!CollectionUtils.isEmpty(obtainList)) {
            permissionDao.addUserPermission(userId, obtainList);
        }
        if (!CollectionUtils.isEmpty(remove)) {
            permissionDao.removeUserPermission(userId, remove);
        }
    }
}
