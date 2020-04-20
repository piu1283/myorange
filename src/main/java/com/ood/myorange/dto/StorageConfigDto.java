package com.ood.myorange.dto;

import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.LocalConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by Guancheng Lai on 3/19/2020
 */
@Data
public class StorageConfigDto {
    private int id;
    private String name;
    private String type;

    private S3Configuration awsConfiguration;
    private AzureConfiguration azureConfiguration;
    private LocalConfiguration localConfiguration;

    private Timestamp createdTime;
    private Timestamp modifyTime;
    private boolean currentUse;
}
