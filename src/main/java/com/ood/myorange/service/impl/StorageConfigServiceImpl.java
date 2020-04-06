package com.ood.myorange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageConfiguration;
import com.ood.myorange.dao.StorageConfigDao;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.StorageConfig;
import com.ood.myorange.service.StorageConfigService;
import com.ood.myorange.util.ModelMapperUtil;
import com.ood.myorange.util.StorageConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guancheng Lai on 03/19/2020
 */
@Service
@Repository
public class StorageConfigServiceImpl implements StorageConfigService {
    @Autowired
    StorageConfigDao storageConfigDao;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public List<StorageConfigDto> getAllConfigurations() throws JsonProcessingException {

        List<StorageConfig> queryConfigList = storageConfigDao.selectAll();

        List<StorageConfigDto> result = new ArrayList<>();

        for (StorageConfig q : queryConfigList) {
            result.add( extract_from_pojo_to_dto( q ));
        }

        return result;
    }

    @Override
    public StorageConfigDto getConfiguration(int configId) throws JsonProcessingException {
        StorageConfig queryResult = storageConfigDao.selectByPrimaryKey( configId );
        if (queryResult == null) {
            throw new ResourceNotFoundException( "Storage configuration not found by the given configId" );
        }

        StorageConfigDto result = extract_from_pojo_to_dto( queryResult );
        return result;
    }

    @Override
    public void addConfiguration(StorageConfigDto configDto) throws JsonProcessingException {
        InsertOrUpdateConfiguration( configDto );
    }

    @Override
    public void updateConfiguration(StorageConfigDto configDto) throws JsonProcessingException {
        StorageConfiguration.StorageType type;
        switch (configDto.getType()) {
            case "AWS":
                type = StorageConfiguration.StorageType.AWS;
                break;
            case "Azure":
                type = StorageConfiguration.StorageType.Azure;
                break;
            case "LOCAL":
                type = StorageConfiguration.StorageType.LOCAL;
                break;
            default:
                throw new ResourceNotFoundException( "Invalid storage type" );
        }

//        StorageConfig queryExistConfig = storageConfigDao.SelectSourceByType( type );
//        if (queryExistConfig == null) {
//            throw new ResourceNotFoundException( "Storage name:" + configDto.getName()
//                    + ", type:" + configDto.getType()
//                    + " not found, failed to edit config" );
//        }

        InsertOrUpdateConfiguration( configDto );

//        StorageConfig updatedConfig = extract_from_dto_to_pojo( configDto );
//        storageConfigDao.updateSourceById( configId, updatedConfig.getName(),updatedConfig.getConfig(),false );
    }

    @Override
    public void deleteConfiguration(int configId) {
        StorageConfig queryResult = storageConfigDao.selectByPrimaryKey( configId );
        if (queryResult == null) {
            throw new ResourceNotFoundException( "Storage configuration not found by the given configId, can not proceed deletion" );
        }

        storageConfigDao.deleteByPrimaryKey( configId );
        StorageConfigUtil.eraseStorageConfiguration( queryResult.getType() );
    }

    /* --------------------------Helper Functions Starts----------------------------------------------------------------------- */

    private void InsertOrUpdateConfiguration(StorageConfigDto configDto) throws JsonProcessingException {
        StorageConfig configToUpdate = ModelMapperUtil.mapping( configDto,StorageConfig.class );
        switch (configDto.getType()) {
            case "AWS":
                S3Configuration s3Configuration = configDto.getAwsConfiguration();
                if (StorageConfigUtil.validateS3( s3Configuration )) {

                    // Setting up Pojo
                    configToUpdate.setType( StorageConfiguration.StorageType.AWS );
                    configToUpdate.setConfig( objectMapper.writeValueAsString( s3Configuration ) );
                    configToUpdate.setCurrentUse( configDto.isCurrentUse() );

                    // Insert into DB
                    if (storageConfigDao.countSourceByType( StorageConfiguration.StorageType.AWS ) == 0) {
                        storageConfigDao.insertSource(configToUpdate.getName(), configToUpdate.getType(), configToUpdate.getConfig());
                    }
                    else {
                        storageConfigDao.updateSourceByType( StorageConfiguration.StorageType.AWS, configToUpdate.getName(), configToUpdate.getConfig(), configToUpdate.isCurrentUse() );
                    }

                    // Insert into in memory HashMap
                    StorageConfigUtil.putStorageConfiguration( StorageConfiguration.StorageType.AWS,s3Configuration );

                }
                else {
                    throw new InvalidRequestException( "Could not validate the AWS S3 configuration" + configDto.getName() );
                }

                break;
            case "Azure":
                AzureConfiguration azureConfiguration = configDto.getAzureConfiguration();
                if (StorageConfigUtil.validateAzure( azureConfiguration )) {

                    // Setting up Pojo
                    configToUpdate.setType( StorageConfiguration.StorageType.Azure );
                    configToUpdate.setConfig( objectMapper.writeValueAsString( azureConfiguration ) );

                    // Insert into DB
                    if (storageConfigDao.countSourceByType( StorageConfiguration.StorageType.Azure ) == 0) {
                        storageConfigDao.insertSource(configToUpdate.getName(), configToUpdate.getType(), configToUpdate.getConfig());
                    }
                    else {
                        storageConfigDao.updateSourceByType( StorageConfiguration.StorageType.Azure, configToUpdate.getName(), configToUpdate.getConfig(), configToUpdate.isCurrentUse() );
                    }

                    // Insert into in memory HashMap
                    StorageConfigUtil.putStorageConfiguration( StorageConfiguration.StorageType.Azure,azureConfiguration );

                }
                else {
                    throw new InvalidRequestException( "Could not validate the Azure configuration:" + configDto.getName());
                }

                break;
            case "LOCAL":
                break;
            default:
                throw new InvalidRequestException( "Storage ServiceImpl: Invalid Storage type:" + configDto.getType());
        }
    }


    private StorageConfig extract_from_dto_to_pojo(StorageConfigDto configDto) throws JsonProcessingException {
        StorageConfig updatedConfig = ModelMapperUtil.mapping( configDto,StorageConfig.class );
        switch (configDto.getType()) {
            case "AWS":
                updatedConfig.setType( StorageConfiguration.StorageType.AWS );
                updatedConfig.setConfig( objectMapper.writeValueAsString( configDto.getAwsConfiguration() ) );
                break;
            case "Azure":
                updatedConfig.setType( StorageConfiguration.StorageType.Azure );
                updatedConfig.setConfig( objectMapper.writeValueAsString( configDto.getAzureConfiguration() ) );
                break;
            case "LOCAL":
                break;
        }
        return updatedConfig;
    }

    private StorageConfigDto extract_from_pojo_to_dto(StorageConfig storageConfig) throws JsonProcessingException {
        StorageConfigDto result = ModelMapperUtil.mapping(storageConfig, StorageConfigDto.class);

        switch (storageConfig.getType()) {
            case AWS:
                result.setAWS( objectMapper.readValue( storageConfig.getConfig(),S3Configuration.class ) );
                break;
            case Azure:
                result.setAzure( objectMapper.readValue( storageConfig.getConfig(),AzureConfiguration.class ) );
                break;
            case LOCAL:
                break;
            default:
                throw new InternalError( "Failed to convert pojo to dto due to error storage type" );
        }

        return result;
    }



    /* --------------------------Helper Functions Ends----------------------------------------------------------------------- */
}
