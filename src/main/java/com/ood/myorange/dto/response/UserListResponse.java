package com.ood.myorange.dto.response;

import com.ood.myorange.dto.AdminUserDto;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by Chen on 4/17/20.
 */
@Data
@ToString
public class UserListResponse {
    List<AdminUserDto> users;
}
