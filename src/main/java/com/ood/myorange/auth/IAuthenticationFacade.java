package com.ood.myorange.auth;

import com.ood.myorange.dto.AdminInfo;
import com.ood.myorange.dto.UserInfo;

/**
 * Created by Chen on 3/18/20.
 */
public interface IAuthenticationFacade {
    UserInfo getUserInfo();
    AdminInfo getAdminInfo();
}
