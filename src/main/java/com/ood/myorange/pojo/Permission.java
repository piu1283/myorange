package com.ood.myorange.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Chen on 3/18/20.
 */
@Data
@ToString
public class Permission {
    private int userId;
    private int permissionId;
    private String permissionName;
}
