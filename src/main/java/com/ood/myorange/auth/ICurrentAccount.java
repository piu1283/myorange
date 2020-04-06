package com.ood.myorange.auth;

import com.ood.myorange.dto.AdminInfo;
import com.ood.myorange.dto.UserInfo;

/**
 * Created by Chen on 3/18/20.
 */
public interface ICurrentAccount {
    UserInfo getUserInfo();
    AdminInfo getAdminInfo();
}
