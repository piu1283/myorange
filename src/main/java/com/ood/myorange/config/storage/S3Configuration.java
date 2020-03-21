package com.ood.myorange.config.storage;

import com.ood.myorange.config.storage.StorageConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class S3Configuration implements StorageConfiguration, java.io.Serializable {
    private String aws_access_key_id;
    private String aws_access_access_key;
    private String region;
    private String bucketName;

    @Override
    public String getKeyId() {
        return null;
    }

    @Override
    public String getAccessKey() {
        return null;
    }

    @Override
    public String getRegion() {
        return null;
    }

    @Override
    public String getBucketName() {
        return null;
    }
}
