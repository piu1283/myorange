package com.ood.myorange.config.storage;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Created by Guancheng Lai
 */
@Component
@Data
@ToString
@Getter
@Setter
@AllArgsConstructor
public class S3Configuration implements StorageConfiguration, java.io.Serializable {
    private String aws_access_key_id;
    private String aws_secret_access_key;
    private String region;
    private String bucketName;

    public S3Configuration() {
        aws_access_key_id = "Default";
        aws_secret_access_key = "Default";
        region = "Default";
        bucketName = "Default";
    }

    public S3Configuration(S3Configuration s3Config) {
        aws_access_key_id = s3Config.getAws_access_key_id();
        aws_secret_access_key = s3Config.getAws_secret_access_key();
        region = s3Config.getRegion();
        bucketName = s3Config.getBucketName();
    }
}
