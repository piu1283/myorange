package com.ood.myorange.service.impl;

import com.ood.myorange.dao.UserDao;
import com.ood.myorange.dto.response.UserDto;
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
        // 1.拿到 User
        List<User> users = userDao.getUsers();
        List<UserDto> res = new ArrayList<>();
        // 2. User -> UserRequest(UserDto)
        for (User user : users) {
            // add other logic
            res.add(ModelMapperUtil.mapping(user, UserDto.class));
        }
        return res;
    }

    @Override
    public UserDto getUserProfile(UserDto queryDto) {
        User user = ModelMapperUtil.mapping(queryDto, User.class);
        User filledUser = userDao.selectOne(user);
        return ModelMapperUtil.mapping(filledUser, UserDto.class);
    }

    @Override
    public User getUserByEmail(String emailAddress) {
        return userDao.getUserByEmail(emailAddress);
    }
}
