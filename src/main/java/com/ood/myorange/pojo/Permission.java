package com.ood.myorange.pojo;

import com.sun.mail.imap.Rights;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import java.util.List;

/**
 * Created by Chen on 3/18/20.
 */
@Data
@ToString
public class Permission {
    private int permissionId;
    private String permissionName;
}
