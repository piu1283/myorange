package com.ood.myorange.controller;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.dto.UserProfile;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Chen on 2/14/20.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    ICurrentAccount currentAccount; // authenticationFacade can be used to obtain the current login user

    @Autowired
    UserService userService;

    @PostMapping(path = "/profile")
    public UserProfile changeUserProfile(@RequestBody UserProfile modifyProfile) {
        if (modifyProfile.getBirthday().getTime() > new Date().getTime()) {
            throw new InvalidRequestException("We don't recommend time travel.");
        }
        return userService.modifyUserProfile(modifyProfile);
    }

    @GetMapping(path = "/profile")
    public UserProfile getUserProfile() {
        return userService.getUserProfile();
    }

}
