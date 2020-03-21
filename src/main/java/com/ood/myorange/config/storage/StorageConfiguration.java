package com.ood.myorange.config.storage;

public interface StorageConfiguration {
    public enum StorageType {
        LOCAL, AWS_S3, Azure;
    }

    String getKeyId();
    String getAccessKey();
    String getRegion();
    String getBucketName();
}
