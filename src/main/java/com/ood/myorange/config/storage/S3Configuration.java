package com.ood.myorange.config.storage;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
public class S3Configuration implements StorageConfiguration, java.io.Serializable {
    private String aws_access_key_id;
    private String aws_access_access_key;
    private String region;
    private String bucketName;

    public S3Configuration() {
        aws_access_key_id = "Default";
        aws_access_access_key = "Default";
        region = "Default";
        bucketName = "Default";
    }

    public S3Configuration(S3Configuration s3Config) {
        aws_access_key_id = s3Config.getAws_access_key_id();
        aws_access_access_key = s3Config.getAws_access_access_key();
        region = s3Config.getRegion();
        bucketName = s3Config.getBucketName();
    }

    @Override
    public String getKeyId() { return aws_access_key_id; }

    @Override
    public String getAccessKey() { return aws_access_access_key; }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }
}
