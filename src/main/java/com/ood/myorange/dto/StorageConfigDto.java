package com.ood.myorange.dto;

import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
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

    // AWS
    private String aws_access_key_id;
    private String aws_secret_access_key;
    private String aws_region;
    private String aws_bucketName;

    // Azure
    private String azure_token;

    private Timestamp createdTime;
    private Timestamp modifyTime;
    private boolean currentUse;

    public void setAWS(S3Configuration inConfig) {
        this.aws_access_key_id = inConfig.getAws_access_key_id();
        this.aws_secret_access_key = inConfig.getAws_secret_access_key();
        this.aws_region = inConfig.getRegion();
        this.aws_bucketName = inConfig.getBucketName();
    }

    public S3Configuration getAWSConfiguration() {
        return new S3Configuration(
                this.aws_access_key_id,
                this.aws_secret_access_key,
                this.aws_region,
                this.aws_bucketName
        );
    }

    public void setAzure(AzureConfiguration inConfig) {
        this.azure_token = inConfig.getAzureToken();
    }

    public AzureConfiguration getAzureConfiguration() {
        return new AzureConfiguration( this.azure_token );
    }
}
