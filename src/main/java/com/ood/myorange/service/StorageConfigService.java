package com.ood.myorange.service;

import com.ood.myorange.dto.StorageConfigDto;

import java.util.List;

/**
 * Created by Guancheng Lai on 3/19/20.
 */

public interface StorageConfigService {
    List<StorageConfigDto> getAllConfigurations(int adminId);
    StorageConfigDto getConfiguration(int configId);
    void addConfiguration(StorageConfigDto queryDto);
    void updateConfiguration(StorageConfigDto queryDto);
    void deleteConfiguration(int configId);
}
