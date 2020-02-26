package com.ood.myorange.service.impl;

import com.ood.myorange.dao.UserDao;
import com.ood.myorange.dto.UserDto;
import com.ood.myorange.pojo.User;
import com.ood.myorange.service.UserService;
import com.ood.myorange.util.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 2/24/20.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userDao.selectAll();
        List<UserDto> res = new ArrayList<>();
        for (User u : users) {
            UserDto userDto = ModelMapperUtil.mapping(u, UserDto.class);
            res.add(userDto);
        }
        return res;
    }

    @Override
    public UserDto getUserProfile(UserDto queryDto) {
        User user = ModelMapperUtil.mapping(queryDto, User.class);
        User filledUser = userDao.selectByPrimaryKey(user);
        return ModelMapperUtil.mapping(filledUser, UserDto.class);
    }
}
