package com.ood.myorange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.StorageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Guancheng Lai on 3/19/2020
 */

@RestController
@Slf4j
@RequestMapping("/admin")
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

    @PostMapping("/config/storage")
    public void addStorageConfig(
            @RequestBody StorageConfigDto storageConfigDto
    ) throws JsonProcessingException {
        validateConfig( storageConfigDto );
        storageConfigService.addConfiguration( storageConfigDto );
    }

    @PutMapping("/config/storage")
    public void editStorageConfig(
            @RequestBody StorageConfigDto storageConfigDto
    ) throws JsonProcessingException {
        validateConfig( storageConfigDto );
        storageConfigService.updateConfiguration(storageConfigDto );
    }

    @DeleteMapping("/config/storage/{configId}")
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
                        storageConfigDto.getAwsConfiguration().getAwsAccessKeyId() == null
                                || storageConfigDto.getAwsConfiguration().getAwsSecretAccessKey() == null
                                || storageConfigDto.getAwsConfiguration().getAwsRegion() == null
                                || storageConfigDto.getAwsConfiguration().getAwsBucketName() == null
                ) {
                    throw new InvalidRequestException( "Incomplete AWS S3 configuration"
                            + ", key_id : " + storageConfigDto.getAwsConfiguration().getAwsAccessKeyId()
                            + ", access_key : " + storageConfigDto.getAwsConfiguration().getAwsSecretAccessKey()
                            + ", region : " + storageConfigDto.getAwsConfiguration().getAwsRegion()
                            + ", bucket : " + storageConfigDto.getAwsConfiguration().getAwsBucketName()
                    );
                }
                break;
            case "AZURE" :
                if (
                        storageConfigDto.getAzureConfiguration().getAzureToken() == null
                ) {
                    throw new InvalidRequestException( "Incomplete Azure BlobStorage configuration" );
                }
                break;
            case "LOCAL" :
                if (
                        storageConfigDto.getLocalConfiguration() == null
                ){
                    throw new InvalidRequestException( "Incomplete Local configuration" );
                }
                break;
            default:
                throw new InvalidRequestException( "Invalid storage type" );
        }
    }

}