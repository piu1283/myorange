package com.ood.myorange.service;

import com.ood.myorange.pojo.Admin;

/**
 * Created by Chen on 3/17/20.
 */
public interface AdminService {
    /**
     * get admin by name
     * @param name
     * @return
     */
    Admin getAdminByName(String name);

    /**
     * change admin password
     * @param password
     */
    void changePassword(String password);
}
