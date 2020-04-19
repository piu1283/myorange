package com.ood.myorange.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.fms.model.ResourceNotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.config.storage.*;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.exception.InternalServerError;
import com.ood.myorange.service.StorageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Guancheng Lai
 */
@Component
@Slf4j
public class StorageConfigUtil {

    @Autowired
    StorageConfigService storageConfigService;

    @Autowired
    ObjectMapper objectMapper;

    public static HashMap<Integer, StorageConfiguration> storageConfigurationHashMap = new HashMap<>();
    public static HashMap<Integer, StorageType> storageTypeHashMap = new HashMap<>();
    public static HashMap<StorageType, Object> storageClientHashMap = new HashMap<>();

    @PostConstruct
    public void init() throws JsonProcessingException {
        log.info( "Start Initializing the StorageConfigUtil..." );

        List<StorageConfigDto> query = storageConfigService.getAllConfigurations();
        for (StorageConfigDto cfg : query) {
            int configId = cfg.getId();
            switch (cfg.getType()) {
                case "AWS":
                    insertStorageConfig( configId,cfg.getType(),cfg.getAwsConfiguration() );
                    break;
                case "AZURE":
                    insertStorageConfig( configId,cfg.getType(),cfg.getAzureConfiguration() );
                    break;
                case "LOCAL":
                    break;
                default:
                    throw new InternalServerError( "Invalid Storage Type: " + cfg.getType() );
            }

            log.info( "Initializing the StorageConfigUtil, config #" + configId + " finished, (" + cfg.getType() + ")");
        }

        log.info( "Successfully initialized the StorageConfigUtil" );
    }

    public static void insertStorageConfig(int configId, String storageType, StorageConfiguration storageConfiguration) {
        switch (storageType) {
            case "AWS":
//                if (!validateS3((S3Configuration) storageConfiguration )) {
//                    throw new InternalServerError("Failed to validate the provided AWS S3 credential");
//                }

                putStorageConfigurationType( configId,StorageType.AWS );
                putStorageConfiguration( configId,storageConfiguration );
                AmazonS3 s3Client = null;
                try {
                    s3Client = AmazonS3ClientBuilder.standard()
                            .withCredentials( new AWSStaticCredentialsProvider(new BasicAWSCredentials( ((S3Configuration) storageConfiguration ).getAwsAccessKeyId(),((S3Configuration) storageConfiguration ).getAwsSecretAccessKey() ) ))
                            .withRegion(((S3Configuration) storageConfiguration ).getAwsRegion())
                            .build();
                }catch (AmazonServiceException e) {
                    // The call was transmitted successfully, but Amazon S3 couldn't process
                    // it, so it returned an error response.
                    e.printStackTrace();
                    log.info("Failed to validate the AWS S3 Credential");
                } catch (SdkClientException e) {
                    // Amazon S3 couldn't be contacted for a response, or the client
                    // couldn't parse the response from Amazon S3.
                    e.printStackTrace();
                    log.info("Failed to validate the AWS S3 Credential");
                }

                if (s3Client != null) {
                    storageClientHashMap.put( StorageType.AWS,s3Client );
                }
                else {
                    throw new InternalServerError( "[Fatal] Fail to start AWS S3 Client." );
                }

                break;
            case "AZURE":

                putStorageConfigurationType( configId,StorageType.AZURE );
                putStorageConfiguration( configId,(AzureConfiguration) storageConfiguration );
                // TODO
                break;
            case "LOCAL":
                putStorageConfigurationType( configId,StorageType.LOCAL );
                putStorageConfiguration( configId,null );
                break;
            default:
                throw new InternalServerError("Invalid storage type: " + storageType);
        }
    }

    /************************************* Start Storage Configuration Hash Map Accessible Function *********************************************/
    public static StorageConfiguration getStorageConfiguration(int configId) {
        if (!storageConfigurationHashMap.containsKey( configId )) {
            throw new ResourceNotFoundException( "Cannot find config by the configId: " + configId );
        }

        return storageConfigurationHashMap.get( configId );
    }

    private static void putStorageConfiguration(int configId, StorageConfiguration storageConfiguration) {
        storageConfigurationHashMap.put( configId,storageConfiguration );
    }

    public static void eraseStorageConfiguration(int configId) {
        storageConfigurationHashMap.remove( configId );
        storageTypeHashMap.remove( configId );
        storageClientHashMap.remove( configId );
    }
    /************************************* End Storage Configuration Hash Map Accessible Function *********************************************/

    /************************************* Start Storage Type Hash Map Accessible Function *********************************************/
    public static StorageType getStorageConfigurationType(int configId) {
        if (!storageTypeHashMap.containsKey( configId )) {
            throw new ResourceNotFoundException( "Cannot find config by the configId: " + configId );
        }

        return storageTypeHashMap.get( configId );
    }

    private static void putStorageConfigurationType(int configId, StorageType type) {
        storageTypeHashMap.put( configId,type );
    }

    /************************************* End Storage Type Hash Map Accessible Function *********************************************/

    /************************************* Start Storage Credential Validation *********************************************/
    public static boolean validateAzure(AzureConfiguration config) {
        //TODO
        return true;
    }

    public static boolean validateS3(S3Configuration config) {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(config.getAwsAccessKeyId(),config.getAwsSecretAccessKey())))
                    .withRegion( Regions.fromName( config.getAwsRegion() ) )
                    .build();

            // Put object onto AWS S3
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long ts = timestamp.getTime();
            String validationFileName = "validation/validating_" + String.valueOf( ts ) + "_" + String.valueOf( Math.random() % 10000 );
            s3Client.putObject(config.getAwsBucketName(), validationFileName, "Validation for connection");

            // Validate if exist
            S3Object myFile = s3Client.getObject( config.getAwsBucketName(), validationFileName );

            // Create delete request
            DeleteObjectsRequest request = new DeleteObjectsRequest(config.getAwsBucketName());
            request.setKeys( Arrays.asList( new DeleteObjectsRequest.KeyVersion(validationFileName) ));

            // Send Delete Objects Request
            DeleteObjectsResult result = s3Client.deleteObjects(request);

            // Printing Deleted Object Keys
            if (result.getDeletedObjects() != null) {
//                result.getDeletedObjects().stream().forEach(e -> System.out.println(e.getKey()));
                log.info("Successfully validate the AWS S3 Credential, the upload and download functionality seems to be working correctly.");
            }
            else {
                log.info("Successfully validate the AWS S3 Credential, but have trouble deleting the validation file:" + validationFileName);
            }

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            log.info("Failed to validate the AWS S3 Credential");
            return false;
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            log.info("Failed to validate the AWS S3 Credential");
            return false;
        }

        return true;
    }

    /************************************ End Storage Credential Validation *********************************************/

    /************************************ Start Storage Client Retrieval Functions *********************************************/
    public static Object getStorageClient(StorageType storageType) {
        switch (storageType) {
            case AWS:
                return (AmazonS3) storageClientHashMap.get( StorageType.AWS );
            case AZURE:
                return null;
            default:
                return null;
        }
    }
    /************************************ End Storage Client Retrieval Functions *********************************************/
}
