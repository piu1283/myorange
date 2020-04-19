package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;

/**
 * Created by Chen on 2/24/20.
 */
@Data
public class AdminUserDto {
    private Integer id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private Long totalStorage;
    private Long usedStorage;
    private Boolean blockedStatus;
    private Boolean uploadAccess;
    private Boolean downloadAccess;
    private Integer sourceId;
    private String sourceName;
    private String sourceType;
}
