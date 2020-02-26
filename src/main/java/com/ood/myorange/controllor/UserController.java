package com.ood.myorange.controllor;

import com.ood.myorange.config.Person;
import com.ood.myorange.dto.UserDto;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.User;
import com.ood.myorange.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Chen on 2/14/20.
 */
@Slf4j
@RestController
public class UserController {

    @Autowired
    Person person;

    @Autowired
    UserService userService;

    @PostMapping(path = "/user/{id}")
    public UserDto getUserInfo(@PathVariable("id") Integer id, @RequestParam("toke_task") String tokenTask, @RequestBody UserDto userDto) {
        log.info("this is a test log");
        throw new ResourceNotFoundException("InvalidRequestException");
    }

    @GetMapping(path = "/users")
    public String getUsers(@RequestParam("toke_task") String tokenTask) {
        log.info("this is a test log");
        throw new InvalidRequestException("InvalidRequestException");
    }

    @GetMapping(path = "/all")
    public List<UserDto> getAll() {
        List<UserDto> users = userService.getAllUser();
        return users;
    }
}
