package com.ood.myorange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.StorageConfigDto;

import java.util.List;

/**
 * Created by Guancheng Lai on 3/19/2020
 */

public interface StorageConfigService {

    List<StorageConfigDto> getAllConfigurations() throws JsonProcessingException;
    StorageConfigDto getConfiguration(int configId) throws JsonProcessingException;
    void addConfiguration(StorageConfigDto queryDto) throws JsonProcessingException;
    void updateConfiguration(StorageConfigDto configDto) throws JsonProcessingException;
    void deleteConfiguration(int configId);

    /**
     * get id, name, type of all storage config, but not the detail
     * @return list of config
     */
    List<StorageConfigDto> getAllConfigurationsWithoutDetail();

    /**
     * get id, name, type of storage config, but not the detail
     * @return list of config
     */
    StorageConfigDto getConfigurationsWithoutDetail(int sourceId);
    
}
