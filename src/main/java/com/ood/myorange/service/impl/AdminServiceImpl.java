package com.ood.myorange.service.impl;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.dao.AdminDao;
import com.ood.myorange.pojo.Admin;
import com.ood.myorange.service.AdminService;
import com.ood.myorange.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chen on 3/17/20.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    ICurrentAccount currentAccount;

    @Override
    public Admin getAdminByName(String name) {
        return adminDao.getAdminByName(name);
    }

    @Override
    public void changePassword(String password) {
        String passAfterEncode = PasswordUtil.encodePassword(password);
        Admin admin = new Admin();
        admin.setPassword(passAfterEncode);
        int id = currentAccount.getAdminInfo().getId();
        admin.setId(id);
        adminDao.updateByPrimaryKeySelective(admin);
    }
}
