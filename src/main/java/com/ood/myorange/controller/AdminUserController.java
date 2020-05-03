package com.ood.myorange.controller;

import com.ood.myorange.dto.AdminDto;
import com.ood.myorange.dto.AdminUserDto;
import com.ood.myorange.dto.response.UserListResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.AdminService;
import com.ood.myorange.service.UserService;
import com.ood.myorange.util.NamingUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Chen on 4/17/20.
 */
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @GetMapping("/users")
    public UserListResponse getAllUserData() {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setUsers(userService.getAllAdminUser());
        return userListResponse;
    }

    @GetMapping("/users/{id}")
    public AdminUserDto getUserData(@PathVariable("id") int userId) {
        if (userId <= 0) {
            throw new InvalidRequestException("Invalid userId: " + userId);
        }
        return userService.getAdminUserByUserId(userId);
    }

    @PostMapping("/users/{id}")
    public void changeUserPermissionAndMemory(@PathVariable("id") int userId, @RequestBody AdminUserDto adminUserDto) {
        if (adminUserDto.getUsedStorage() != null && adminUserDto.getTotalStorage() > 107_374_182_400L) {
            throw new InvalidRequestException("storage cannot exceed 100G");
        }
        adminUserDto.setId(userId);
        userService.modifyUserPermissionAndMemory(adminUserDto);
    }

    @PutMapping("/users")
    public void addUser(@RequestBody AdminUserDto adminUserDto) {
        if (adminUserDto.getUsedStorage() != null && adminUserDto.getTotalStorage() > 107_374_182_400L) {
            throw new InvalidRequestException("storage cannot exceed 100G");
        }
        if (!NamingUtil.validEmailAddress(adminUserDto.getEmail())) {
            throw new InvalidRequestException("invalid email format");
        }
        userService.addUser(adminUserDto);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/password")
    public void changeAdminPassword(@RequestBody AdminDto adminDto) {
        if (StringUtils.isBlank(adminDto.getPassword())) {
            throw new InvalidRequestException("password cannot be empty.");
        }
        if (adminDto.getPassword().length() < 6) {
            throw new InvalidRequestException("Length of password cannot less then 6.");
        }
        adminService.changePassword(adminDto.getPassword());
    }
}
