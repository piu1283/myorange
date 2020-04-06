package com.ood.myorange.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.fms.model.InternalErrorException;
import com.amazonaws.services.fms.model.ResourceNotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageConfiguration;
import com.ood.myorange.dao.StorageConfigDao;
import com.ood.myorange.pojo.StorageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class StorageConfigUtil {
    @Autowired
    StorageConfigDao storageConfigDao;

    @Autowired
    ObjectMapper objectMapper;

    public static HashMap<StorageConfiguration.StorageType, StorageConfiguration> configurationHashMap = new HashMap<>();

    @PostConstruct
    public void init() throws JsonProcessingException {
        log.info( "Start Initializing the StorageConfigUtil..." );
        List<StorageConfig> query = storageConfigDao.selectAll();
        for (StorageConfig cfg : query) {
            int configId = cfg.getId();
            switch (cfg.getType()) {
                case AWS:
                    S3Configuration s3Configuration =  objectMapper.readValue( cfg.getConfig(),S3Configuration.class );
                    //if (validateS3( s3Configuration ) ){
                    if ( true ){
                        configurationHashMap.put( StorageConfiguration.StorageType.AWS,s3Configuration );
                    }
                    else {
                        throw new InternalErrorException( "Having trouble to validate the provided AWS S3 Configuration, " +
                                "configId:" + configId + "configName: " + cfg.getName());
                    }
                    break;

                case Azure:
                    AzureConfiguration azureConfiguration = new AzureConfiguration( ModelMapperUtil.mapping( cfg.getConfig(),AzureConfiguration.class ) );
                    if (validateAzure( azureConfiguration ) ){
                        configurationHashMap.put( StorageConfiguration.StorageType.Azure,azureConfiguration );
                    }
                    else {
                        throw new InternalErrorException( "Having trouble to validate the provided Azure Configuration, " +
                                "configId:" + configId + "configName: " + cfg.getName());
                    }
                    break;

                default:
                    break;
            }
            log.info( "Initializing the StorageConfigUtil, config #" + configId + " finished");
        }

        log.info( "Successfully initialized the StorageConfigUtil" );
    }

    public static boolean validateAzure(AzureConfiguration config) {
        //TODO
        return true;
    }

    public static boolean validateS3(S3Configuration config) {
        String access_key_id = config.getAws_access_key_id();
        String secret_key_id = config.getAws_secret_access_key();
        Regions clientRegion = Regions.fromName( config.getRegion() );
        String bucketName = config.getBucketName();
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(access_key_id,secret_key_id);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(clientRegion)
                    .build();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long ts = timestamp.getTime();
            String validationFileName = String.valueOf( ts ) + String.valueOf( Math.random() % 10000 ) + "validation.txt";
            s3Client.putObject(bucketName, validationFileName, "Validation for connection");

            S3Object myFile = s3Client.getObject( bucketName, validationFileName );
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);

            // Create delete request
            request.setKeys( Arrays.asList( new DeleteObjectsRequest.KeyVersion("validation.txt") ));

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

    public static void putStorageConfiguration(StorageConfiguration.StorageType type, StorageConfiguration storageConfiguration) {
        configurationHashMap.put( type,storageConfiguration );
    }

    public static void eraseStorageConfiguration(StorageConfiguration.StorageType type) {
        configurationHashMap.remove( type );
    }

    public static  StorageConfiguration getStorageConfiguration(StorageConfiguration.StorageType type) {
        if (!configurationHashMap.containsKey( type )) {
            throw new ResourceNotFoundException( "Cannot find config by the configId: " + type );
        }

        return configurationHashMap.get( type );
    }
}
