package com.ood.myorange.config.storage;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * Created by Guancheng Lai
 */
@Component
@Data
@ToString
@Getter
@Setter
@AllArgsConstructor
public class AzureConfiguration implements StorageConfiguration {
    private String azureToken;

    public AzureConfiguration() {
        azureToken = "Default";
    }

    public AzureConfiguration(AzureConfiguration azureConfig) {
        azureToken = azureConfig.getAzureToken();
    }
}
