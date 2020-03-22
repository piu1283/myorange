package com.ood.myorange.util;

import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.pojo.StorageConfig;
import com.ood.myorange.util.storageUtils.AWSUtil;
import com.ood.myorange.util.storageUtils.StorageUtil;
import com.ood.myorange.config.storage.StorageConfiguration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StorageUtilFactory {
    private static HashMap<StorageConfiguration.StorageType, StorageUtil> storageUtilHashMap =
            new HashMap<StorageConfiguration.StorageType, StorageUtil>() {
            {
                put(StorageConfiguration.StorageType.AWS_S3, new AWSUtil( new S3Configuration() ) );
            }
    };

    public StorageUtil getInstance(StorageConfiguration.StorageType type) {
        return storageUtilHashMap.get( type );
    }
}
