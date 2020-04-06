package com.ood.myorange.auth;

import com.ood.myorange.dto.AdminInfo;
import com.ood.myorange.dto.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public UserInfo getUserInfo() {
        return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public AdminInfo getAdminInfo() {
        return (AdminInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}


