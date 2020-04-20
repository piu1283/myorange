package com.ood.myorange.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Chen on 3/17/20.
 */
@Data
@ToString
@Table(name = "t_admin")
public class Admin {
    @Id
    @Column(name = "id")
    private Integer id;
    private String name;
    private String password;
    private Timestamp createTime;
}
