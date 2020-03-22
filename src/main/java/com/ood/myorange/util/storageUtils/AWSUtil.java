package com.ood.myorange.util.storageUtils;

import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AWSUtil implements StorageUtil {

    @Autowired
    S3Configuration storageConfiguration;

    public AWSUtil(S3Configuration s3Configuration) {
        this.storageConfiguration = s3Configuration;
    }

    @Override
    public String getDownloadUrl(int key) {
        String keyId = storageConfiguration.getKeyId();
        String accessKey = storageConfiguration.getAccessKey();
        String region = storageConfiguration.getRegion();
        String bucket = storageConfiguration.getBucketName();
//        return s3Client.getPresignedUrl(keyId, accessKey, region, bucket);
        return Integer.toString(key);
    }

    @Override
    public String getUploadUrl(int key) {
        return null;
    }
}
