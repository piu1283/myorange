package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

/**
 * Created by Chen on 4/18/20.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private Integer id;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private Long storage;
    private Long usedStorage;
    private boolean uploadAccess;
    private boolean downloadAccess;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
