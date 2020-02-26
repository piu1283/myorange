package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Date;

/**
 * Created by Chen on 2/24/20.
 */
@Data
public class UserDto {
    private Integer id;
    private Date birthday;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    @JsonIgnore
    private String password;
}
