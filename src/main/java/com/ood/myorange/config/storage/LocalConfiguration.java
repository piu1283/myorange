package com.ood.myorange.config.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

/**
 * Created by Guancheng Lai
 */
@Component
@Data
@ToString
@AllArgsConstructor
public class LocalConfiguration implements StorageConfiguration {
    private String localPath;

    public LocalConfiguration() {
        localPath = "/";
    }
}
