package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * Created by Chen on 3/17/20.
 */
@Data
public class UserInfo implements UserDetails {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private int sourceId;
    @JsonIgnore
    private boolean blocked;
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !blocked;
    }
}
