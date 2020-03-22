package com.ood.myorange.config.storage;

public interface StorageConfiguration {
    public enum StorageType {
        LOCAL, AWS_S3, Azure;
    }
    // Shared
    default String getKeyId() {return "";}

    // S3 Specific
    default String getAccessKey() {return "";}
    default String getRegion() {return "";}
    default String getBucketName() {return "";}
}
