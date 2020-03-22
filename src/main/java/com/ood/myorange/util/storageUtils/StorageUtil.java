package com.ood.myorange.util.storageUtils;

import org.springframework.stereotype.Component;

@Component
public interface StorageUtil {
    public String getDownloadUrl(int key);
    public String getUploadUrl(int key);
}
