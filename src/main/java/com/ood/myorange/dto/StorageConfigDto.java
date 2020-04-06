package com.ood.myorange.dto;

import com.ood.myorange.config.storage.AzureConfiguration;
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

    // AWS
    private String awsAccessKeyId;
    private String awsSecretAccessKey;
    private String awsRegion;
    private String awsBucketName;

    // Azure
    private String azureToken;

    private Timestamp createdTime;
    private Timestamp modifyTime;
    private boolean currentUse;

    public void setAWS(S3Configuration inConfig) {
        this.awsAccessKeyId = inConfig.getAws_access_key_id();
        this.awsSecretAccessKey = inConfig.getAws_secret_access_key();
        this.awsRegion = inConfig.getRegion();
        this.awsBucketName = inConfig.getBucketName();
    }

    public S3Configuration getAwsConfiguration() {
        return new S3Configuration(
                this.awsAccessKeyId,
                this.awsSecretAccessKey,
                this.awsRegion,
                this.awsBucketName
        );
    }

    public void setAzure(AzureConfiguration inConfig) {
        this.azureToken = inConfig.getAzureToken();
    }

    public AzureConfiguration getAzureConfiguration() {
        return new AzureConfiguration( this.azureToken );
    }
}
