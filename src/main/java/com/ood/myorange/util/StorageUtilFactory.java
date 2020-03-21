package com.ood.myorange.util;

import com.ood.myorange.util.storageUtils.StorageUtil;
import com.ood.myorange.config.storage.StorageConfiguration;
import java.util.HashMap;

public class StorageUtilFactory {
    HashMap<StorageConfiguration.StorageType, StorageUtil> storageUtilHashMap;
    StorageUtil getInstance(StorageConfiguration.StorageType type) {
        return storageUtilHashMap.get( type );
    }
}
