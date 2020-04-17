package com.ood.myorange.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Chen on 4/14/20.
 */
@Data
@Table(name = "sys_config")
@ToString
public class SysConfig {
    @Id
    @Column(name = "class_id")
    private String classId;
    private String config;
    private Timestamp createTime;
    private Timestamp modifyTime;
}
