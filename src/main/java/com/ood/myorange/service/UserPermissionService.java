package com.ood.myorange.service;

import com.ood.myorange.pojo.Permission;

import java.util.List;
import java.util.Map;

/**
 * Created by Chen on 3/18/20.
 */
public interface UserPermissionService {
    List<Permission> getPermissionListByUserId(int userId);

    /**
     * get all permission
     * @return key is the user id, value is the permission
     */
    Map<Integer, List<String>> getAllPermission();

    /**
     * change user permission
     * @param userId
     * @param obtainList
     * @param remove
     */
    void changePermission(int userId, List<String> obtainList, List<String> remove);
}
