package com.ood.myorange.pojo;

import com.ood.myorange.config.storage.StorageConfiguration;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Id;

import java.sql.Timestamp;

/**
 * Created by Guancheng Lai on 3/19/2020
 */

@Data
@ToString
@Table(name = "t_source")
public class StorageConfig {
    @Id
    @Column(name = "id")
    private int id;
    private String name;
    private StorageConfiguration.StorageType type;
    private String config;
    private Timestamp createdTime;
    private Timestamp modifyTime;
    private boolean currentUse;
}