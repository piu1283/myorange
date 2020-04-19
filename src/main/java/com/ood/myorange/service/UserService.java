package com.ood.myorange.service;

import com.ood.myorange.dto.AdminUserDto;
import com.ood.myorange.dto.UserProfile;
import com.ood.myorange.pojo.User;

import java.util.List;

/**
 * Created by Chen on 2/24/20.
 */
public interface UserService {
    /**
     * get all user dto in admin aspect format
     * @return
     */
    List<AdminUserDto> getAllAdminUser();

    /**
     * get user pojo by email
     * @param emailAddress
     * @return
     */
    User getUserByEmail(String emailAddress);

    /**
     * get user dto in admin aspect format by id
     * @param userId
     * @return
     */
    AdminUserDto getAdminUserByUserId(int userId);

    /**
     * change user permission and total size
     * @param adminUserDto
     */
    void modifyUserPermissionAndMemory(AdminUserDto adminUserDto);

    /**
     * add a user
     * @param adminUserDto
     * @return
     */
    void addUser(AdminUserDto adminUserDto);

    /**
     * delete user by Id
     * @param userId
     */
    void deleteUser(int userId);

    /**
     * get user profile by id
     * @return
     */
    UserProfile getUserProfile();

    /**
     * modify User Profile
     * @return user profile after modification
     */
    UserProfile modifyUserProfile(UserProfile modifyProfile);

}
