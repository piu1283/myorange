package com.ood.myorange.controllor;

import com.ood.myorange.auth.IAuthenticationFacade;
import com.ood.myorange.dto.response.UserDto;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created by Chen on 2/14/20.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    IAuthenticationFacade authenticationFacade; // authenticationFacade can be used to obtain the current login user

    @Autowired
    UserService userService;

    @PostMapping(path = "/users/{id}")
    public UserDto getUserInfo(@PathVariable("id") Integer id, @RequestParam("toke_task") String tokenTask, @RequestBody UserDto userDto) {
        log.info("this is a test log");
        // this line can get the user detail in the context
        System.out.println(authenticationFacade.getUserInfo());
        throw new ResourceNotFoundException("file you want is not there.");
    }

    @GetMapping(path = "/users")
    public List<UserDto> getAllUser() {
        return userService.getAllUser();
    }
}
