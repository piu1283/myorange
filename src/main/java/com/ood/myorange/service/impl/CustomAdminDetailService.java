package com.ood.myorange.service.impl;

import com.ood.myorange.dto.AdminInfo;
import com.ood.myorange.pojo.Admin;
import com.ood.myorange.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 3/17/20.
 */
@Service("adminDetailService")
public class CustomAdminDetailService implements UserDetailsService {

    @Autowired
    AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Admin admin = adminService.getAdminByName(s);
        AdminInfo adminInfo = new AdminInfo();
        if (admin == null) {
            throw new UsernameNotFoundException("cannot find admin name: " + s);
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        adminInfo.setId(admin.getId());
        adminInfo.setName(admin.getName());
        adminInfo.setPassword(admin.getPassword());
        adminInfo.setAuthorities(authorities);
        return adminInfo;
    }
}
