package com.ood.myorange.dto;

import com.ood.myorange.config.storage.StorageConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * Created by Guancheng Lai on 3/19/20.
 */
@Data
public class StorageConfigDto {
    private int id;
    private String name;
    private String type;
    private String config;
    private Timestamp createdTime;
    private Timestamp modifyTime;
    private boolean currentUse;
}
