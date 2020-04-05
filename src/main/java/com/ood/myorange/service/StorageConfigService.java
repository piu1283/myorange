package com.ood.myorange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.StorageConfigDto;

import java.util.List;

/**
 * Created by Guancheng Lai on 3/19/20.
 */

public interface StorageConfigService {

    List<StorageConfigDto> getAllConfigurations() throws JsonProcessingException;
    StorageConfigDto getConfiguration(int configId) throws JsonProcessingException;
    void addConfiguration(StorageConfigDto queryDto) throws JsonProcessingException;
    void updateConfiguration(int configId, StorageConfigDto queryDto) throws JsonProcessingException;
    void deleteConfiguration(int configId);
    
}
