package com.ood.myorange.config.storage;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Created by Guancheng Lai
 */
@Component
@Data
@ToString
public class AzureConfiguration implements StorageConfiguration {
    private String azureToken;

    public AzureConfiguration() {
        azureToken = "";
    }

    public AzureConfiguration(String token) {
        azureToken = token;
    }

    public AzureConfiguration(AzureConfiguration azureConfig) {
        azureToken = azureConfig.getAzureToken();
    }
}
