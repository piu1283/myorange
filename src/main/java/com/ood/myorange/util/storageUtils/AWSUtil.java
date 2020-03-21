package com.ood.myorange.util.storageUtils;

import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

public class AWSUtil implements StorageUtil {

    @Autowired
    StorageConfiguration storageConfiguration;

    @Override
    public String getDownloadUrl(int key) {
        String keyId = storageConfiguration.getKeyId();
        String accessKey = storageConfiguration.getAccessKey();
        String region = storageConfiguration.getRegion();
        String bucket = storageConfiguration.getBucketName();
        // return s3Client.getPresignedUrl(keyId, accessKey, region, bucket);
        return null;
    }

    @Override
    public String getUploadUrl(int key) {
        return null;
    }
}
