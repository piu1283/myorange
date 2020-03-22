package com.ood.myorange.config.storage;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
public class AzureConfiguration implements StorageConfiguration {
    String key;
    public AzureConfiguration() { key = ""; }
    public AzureConfiguration(AzureConfiguration azureConfig) {
        key = azureConfig.getKey();
    }

    @Override
    public String getAccessKey() {
        return this.key;
    }
}
