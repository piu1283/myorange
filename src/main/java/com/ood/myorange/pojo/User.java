package com.ood.myorange.pojo;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Chen on 2/21/20.
 */
@Data
@ToString
@Table(name = "t_user")
public class User {
    @Id
    @Column(name = "id")
    private Integer id;
    private Date birthday;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private String password;

}


