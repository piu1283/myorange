package com.ood.myorange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageType;
import com.ood.myorange.dao.StorageConfigDao;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.StorageConfig;
import com.ood.myorange.service.StorageConfigService;
import com.ood.myorange.util.ModelMapperUtil;
import com.ood.myorange.util.StorageConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ood.myorange.config.storage.StorageType.AWS;
import static com.ood.myorange.config.storage.StorageType.AZURE;

/**
 * Created by Guancheng Lai on 03/19/2020
 */
@Service
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
        StorageType type;
        switch (configDto.getType()) {
            case "AWS":
                type = AWS;
                break;
            case "AZURE":
                type = AZURE;
                break;
            case "LOCAL":
                type = StorageType.LOCAL;
                break;
            default:
                throw new ResourceNotFoundException( "Invalid storage type" );
        }

        InsertOrUpdateConfiguration( configDto );
    }

    @Override
    public void deleteConfiguration(int configId) {
        StorageConfig queryResult = storageConfigDao.selectByPrimaryKey( configId );
        if (queryResult == null) {
            throw new ResourceNotFoundException( "Storage configuration not found by the given configId, can not proceed deletion" );
        }

        storageConfigDao.deleteByPrimaryKey( configId );
        StorageConfigUtil.eraseStorageConfiguration( configId );
    }

    /* --------------------------Helper Functions Starts----------------------------------------------------------------------- */

    private void InsertOrUpdateConfiguration(StorageConfigDto configDto) throws JsonProcessingException {
        StorageConfig configToUpdate = ModelMapperUtil.mapping( configDto,StorageConfig.class );
        switch (configDto.getType()) {
            case "AWS":
                S3Configuration s3Configuration = configDto.getAwsConfiguration();
//                if (StorageConfigUtil.validateS3( s3Configuration )) {
                if (true) {
                    // Setting up Pojo
                    configToUpdate.setType( AWS );
                    configToUpdate.setConfig( objectMapper.writeValueAsString( s3Configuration ) );
                    configToUpdate.setCurrentUse( configDto.isCurrentUse() );

                    // Insert into DB
                    if (storageConfigDao.countSourceByType( AWS ) == 0) {
                        storageConfigDao.insertSource(configToUpdate.getName(), configToUpdate.getType(), configToUpdate.getConfig());
                    }
                    else {
                        storageConfigDao.updateSourceByType( AWS, configToUpdate.getName(), configToUpdate.getConfig(), configToUpdate.isCurrentUse() );
                    }

                    // Insert into the in-memory HashMap
                    int sourceId = storageConfigDao.SelectSourceByType( AWS ).getId();
                    StorageConfigUtil.insertStorageConfig( sourceId,"AWS",s3Configuration );

                }
                else {
                    throw new InvalidRequestException( "Could not validate the AWS S3 configuration" + configDto.getName() );
                }

                break;
            case "AZURE":
                AzureConfiguration azureConfiguration = configDto.getAzureConfiguration();
                if (StorageConfigUtil.validateAzure( azureConfiguration )) {

                    // Setting up Pojo
                    configToUpdate.setType( AZURE );
                    configToUpdate.setConfig( objectMapper.writeValueAsString( azureConfiguration ) );

                    // Insert into DB
                    if (storageConfigDao.countSourceByType( AZURE ) == 0) {
                        storageConfigDao.insertSource(configToUpdate.getName(), configToUpdate.getType(), configToUpdate.getConfig());
                    }
                    else {
                        storageConfigDao.updateSourceByType( AZURE, configToUpdate.getName(), configToUpdate.getConfig(), configToUpdate.isCurrentUse() );
                    }

                    // Insert into the in-memory HashMap
                    int sourceId = storageConfigDao.SelectSourceByType( AWS ).getId();
                    StorageConfigUtil.insertStorageConfig( sourceId,"AZURE",azureConfiguration );

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
                updatedConfig.setType( AWS );
                updatedConfig.setConfig( objectMapper.writeValueAsString( configDto.getAwsConfiguration() ) );
                break;
            case "AZURE":
                updatedConfig.setType( AZURE );
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
                S3Configuration config = objectMapper.readValue( storageConfig.getConfig(),S3Configuration.class );
                result.setAWS( config );
                break;
            case AZURE:
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
