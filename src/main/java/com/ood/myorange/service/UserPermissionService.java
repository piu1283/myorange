package com.ood.myorange.service;

import com.ood.myorange.pojo.Permission;

import java.util.List;

/**
 * Created by Chen on 3/18/20.
 */
public interface UserPermissionService {
    List<Permission> getPermissionListByUserId(int userId);
}
