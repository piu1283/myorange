package com.ood.myorange.service;

import com.ood.myorange.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chen on 2/24/20.
 */
public interface UserService {
    List<UserDto> getAllUser();

    UserDto getUserProfile(UserDto user);
}
