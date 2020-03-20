package com.ood.myorange.service;

import com.ood.myorange.dto.AdminInfo;
import com.ood.myorange.pojo.Admin;

/**
 * Created by Chen on 3/17/20.
 */
public interface AdminService {
    Admin getAdminByName(String name);
}
