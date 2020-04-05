package com.ood.myorange.controllor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.StorageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Guancheng Lai on 3/19/20.
 */

@RestController
@Slf4j
@RequestMapping("/Admin")
public class StorageConfigController {

    @Autowired
    StorageConfigService storageConfigService;

    @GetMapping("/config/storage")
    public List<StorageConfigDto> viewAllStorageConfig() throws JsonProcessingException {
        return storageConfigService.getAllConfigurations();
    }

    @GetMapping("/config/storage/{configId}")
    public StorageConfigDto viewOneStorageConfig(
            @PathVariable("configId") int configId
    ) throws JsonProcessingException {
        validateConfigId( configId );
        return storageConfigService.getConfiguration( configId );
    }

    @PostMapping("/config/storage/add")
    public void addStorageConfig(
            @RequestBody StorageConfigDto storageConfigDto
    ) throws JsonProcessingException {
        validateConfig( storageConfigDto );
        storageConfigService.addConfiguration( storageConfigDto );
    }

    @PutMapping("/config/storage/edit/{configId}")
    public void editStorageConfig(
            @PathVariable("configId") int configId,
            @RequestBody StorageConfigDto storageConfigDto
    ) throws JsonProcessingException {
        validateConfigId( configId );
        validateConfig( storageConfigDto );
        storageConfigService.updateConfiguration( configId, storageConfigDto );
    }

    @PostMapping("/config/storage/delete/{configId}")
    public void deleteStorageConfig(
            @PathVariable int configId
    ){
        validateConfigId( configId );
        storageConfigService.deleteConfiguration( configId );
    }

    void validateConfigId(int configId) {
        if (configId <= 0) {
            throw new InvalidRequestException("ConfigId should be greater than zero");
        }
    }

    void validateConfig(StorageConfigDto storageConfigDto) {
        switch (storageConfigDto.getType()) {
            case "AWS" :
                if (
                        storageConfigDto.getAws_access_key_id() == null
                                || storageConfigDto.getAws_secret_access_key() == null
                                || storageConfigDto.getAws_region() == null
                                || storageConfigDto.getAws_bucketName() == null
                ) {
                    throw new InvalidRequestException( "Incomplete AWS S3 configuration" );
                }
                break;
            case "Azure" :
                if (
                        storageConfigDto.getAzure_token() == null
                ) {
                    throw new InvalidRequestException( "Incomplete Azure BlobStorage configuration" );
                }
                break;
            case "LOCAL" :
                break;
            default:
                throw new InvalidRequestException( "Invalid storage type" );
        }
    }
}