package com.ood.myorange.service.impl;

import com.ood.myorange.dao.AdminDao;
import com.ood.myorange.pojo.Admin;
import com.ood.myorange.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chen on 3/17/20.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDao adminDao;

    @Override
    public Admin getAdminByName(String name) {
        return adminDao.getAdminByName(name);
    }
}
