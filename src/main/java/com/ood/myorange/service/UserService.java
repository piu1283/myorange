package com.ood.myorange.service;

import com.ood.myorange.dto.response.UserDto;
import com.ood.myorange.pojo.User;

import java.util.List;

/**
 * Created by Chen on 2/24/20.
 */
public interface UserService {
    List<UserDto> getAllUser();
    UserDto getUserProfile(UserDto user);
    User getUserByEmail(String emailAddress);

}
