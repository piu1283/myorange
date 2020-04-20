package com.ood.myorange.service.impl;

import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.exception.ForbiddenException;
import com.ood.myorange.pojo.Permission;
import com.ood.myorange.pojo.User;
import com.ood.myorange.service.UserPermissionService;
import com.ood.myorange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service("userDetailService")
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    UserPermissionService userPermissionService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        User user = userService.getUserByEmail(s);
        UserInfo userInfo = new UserInfo();
        if (user == null || user.isDeletedOrNot()) {
            throw new UsernameNotFoundException("cannot find user name: " + s);
        }
        if (user.isBlockedOrNot()) {
            throw new ForbiddenException("You are blocked by admin, please contact them for more detail.");
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        List<Permission> permissions = userPermissionService.getPermissionListByUserId(user.getId());
        permissions.forEach(p->authorities.add(new SimpleGrantedAuthority(p.getPermissionName())));
        userInfo.setAuthorities(authorities);
        userInfo.setBlocked(user.isBlockedOrNot());
        userInfo.setEmail(user.getEmail());
        userInfo.setId(user.getId());
        userInfo.setPassword(user.getPassword());
        userInfo.setSourceId(user.getSourceId());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        return userInfo;
    }
}
