package com.ood.myorange.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageType;
import com.ood.myorange.dto.FileUploadDto;
import com.ood.myorange.dto.response.PreSignedUrlResponse;
import com.ood.myorange.exception.InternalServerError;
import com.ood.myorange.pojo.OriginalFile;
import com.ood.myorange.service.*;
import com.ood.myorange.util.NamingUtil;
import com.ood.myorange.util.StorageConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Guancheng Lai
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    ICurrentAccount currentAccount;

    @Autowired
    FileService fileService;

    @Override
    public PreSignedUrlResponse getPreSignedUrl(FileUploadDto fileUploadDto) throws JsonProcessingException {
        int configId = currentAccount.getUserInfo().getSourceId();

        // Check duplicate
        boolean originalFileExist = fileService.checkOriginFileExist( fileUploadDto,configId );
        if (originalFileExist) {
            PreSignedUrlResponse preSignedUrlResponse = new PreSignedUrlResponse();
            OriginalFile of = fileService.InsertOrUpdateOriginFile( fileUploadDto,configId );
            fileService.addUserFile( fileUploadDto,of.getOriginId() );
            return preSignedUrlResponse;
        }

        PreSignedUrlResponse preSignedUrlResponse;
        StorageType storageType = StorageConfigUtil.getStorageConfigurationType( configId );

        switch (storageType) {
            case AWS :
                S3Configuration s3Configuration = (S3Configuration) StorageConfigUtil.getStorageConfiguration( configId );
                preSignedUrlResponse = AWSUpload(
                        s3Configuration,
                        NamingUtil.generateOriginFileId(fileUploadDto.getMD5(),String.valueOf( fileUploadDto.getSize())),
                        fileUploadDto.getFileName()
                );

                break;
            case AZURE :
                // TODO
                preSignedUrlResponse = new PreSignedUrlResponse();
                preSignedUrlResponse.setUploadUrl( "TODO.AZURE/FINISHED.UPLOAD" );
                break;
            case LOCAL :
                // TODO
                preSignedUrlResponse = new PreSignedUrlResponse();
                preSignedUrlResponse.setUploadUrl( "TODO.LOCAL/FINISHED.UPLOAD" );
                break;
            default:
                throw new InternalError( "Unknown storage type:" + storageType );
        }

        return preSignedUrlResponse;
    }

    @Override
    public void uploadFinished(FileUploadDto fileUploadDto) throws JsonProcessingException {
        int configId = currentAccount.getUserInfo().getSourceId();
        StorageType storageType = StorageConfigUtil.getStorageConfigurationType( configId );
        switch (storageType) {
            case AWS:
                try {
                    S3Configuration s3Configuration = (S3Configuration) StorageConfigUtil.getStorageConfiguration( configId );

                    AmazonS3 s3Client = (AmazonS3) StorageConfigUtil.getStorageClient( StorageType.AWS );
                    if (s3Client == null) {
                        log.error( "NEED ATTENTION! StorageUtil has a NULL AWS S3 Client." );
                        StorageConfigUtil.insertStorageConfig( configId,"AWS", s3Configuration);
                        s3Client = (AmazonS3) StorageConfigUtil.getStorageClient( StorageType.AWS );
                    }

                    if (!s3Client.doesBucketExistV2( fileUploadDto.getUploadKey() )) {
                        throw new InternalServerError( "Could not found the temp bucket, upload failed." );
                    }

                    S3Object s3Object = s3Client.getObject( fileUploadDto.getUploadKey(),fileUploadDto.getFileName() );
                    s3Client.copyObject(
                            fileUploadDto.getUploadKey(),
                            fileUploadDto.getFileName(),
                            s3Configuration.getAwsBucketName(),
                            NamingUtil.generateOriginFileId( fileUploadDto.getMD5(),String.valueOf( fileUploadDto.getSize() ) )
                    );

                    ObjectListing objectListing = s3Client.listObjects(fileUploadDto.getUploadKey());
                    while (true) {
                        Iterator<S3ObjectSummary> objIter = objectListing.getObjectSummaries().iterator();
                        while (objIter.hasNext()) {
                            s3Client.deleteObject(fileUploadDto.getUploadKey(), objIter.next().getKey());
                        }

                        // If the bucket contains many objects, the listObjects() call
                        // might not return all of the objects in the first listing. Check to
                        // see whether the listing was truncated. If so, retrieve the next page of objects
                        // and delete them.
                        if (objectListing.isTruncated()) {
                            objectListing = s3Client.listNextBatchOfObjects(objectListing);
                        } else {
                            break;
                        }
                    }

                    s3Client.deleteBucket( fileUploadDto.getUploadKey() );

                } catch (AmazonServiceException e) {
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

                break;
            case AZURE:
                // TODO
                break;
            case LOCAL:
                // TODO
                break;
            default:
                throw new InternalServerError( "Invalid storage type: " + storageType );
        }

        OriginalFile of = fileService.InsertOrUpdateOriginFile( fileUploadDto,configId );
        fileService.addUserFile( fileUploadDto,of.getOriginId() );
    }

    private PreSignedUrlResponse AWSUpload(S3Configuration s3Configuration, String fileObjectName, String fileRealName) {
        PreSignedUrlResponse preSignedUrlResponse = new PreSignedUrlResponse();
        try {
            AmazonS3 s3Client = (AmazonS3) StorageConfigUtil.getStorageClient( StorageType.AWS );

            try {
                S3Object s3Object = s3Client.getObject( s3Configuration.getAwsBucketName(),fileObjectName );
            }
            catch (AmazonS3Exception e) {
                log.info( "File Already exist" );
            }

            String tempBucketName = AWSCreateTempBucket( s3Client,fileObjectName );
            String preSignedURL = AWSGenerateURL( s3Client,tempBucketName,fileObjectName,fileRealName );

            log.info("AWS Upload Pre-Signed URL has been generated: " + preSignedURL );
            preSignedUrlResponse.setUploadUrl( preSignedURL );
            preSignedUrlResponse.setUploadKey( tempBucketName );
        } catch (
        AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
            System.out.println( "Failed to generate pre-signed URL" );
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            System.out.println( "Failed to generate pre-signed URL" );
        }

        return preSignedUrlResponse;
    }

    private String AWSCreateTempBucket(AmazonS3 s3Client, String fileObjectName) {
        String tempBucketName = "my-orange-" +
                fileObjectName.substring( 0,32 ).toLowerCase() + "-" +
                System.currentTimeMillis();


        while (s3Client.doesBucketExistV2( tempBucketName )) {
            tempBucketName = "my-orange-" +
                    fileObjectName.substring( 0,32 ).toLowerCase() + "-" +
                    System.currentTimeMillis();
            log.info( tempBucketName + ", size = " + tempBucketName.length() );
        }

        List<CORSRule.AllowedMethods> ruleAM = new ArrayList<CORSRule.AllowedMethods>();
        ruleAM.add(CORSRule.AllowedMethods.PUT);
        CORSRule rule = new CORSRule().withId("CORSRule").withAllowedMethods(ruleAM)
                .withAllowedOrigins( Arrays.asList("*")).withMaxAgeSeconds(3000)
                .withExposedHeaders(Arrays.asList("x-amz-server-side-encryption"));

        List<CORSRule> rules = new ArrayList<CORSRule>();
        rules.add( rule );

        // Add the rules to a new CORS configuration.
        BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
        configuration.setRules(rules);

        try {
            Bucket tempBucketForUpload = s3Client.createBucket( tempBucketName );
            s3Client.setBucketCrossOriginConfiguration( tempBucketName, configuration );
        }
        catch (AmazonS3Exception e) {
            e.printStackTrace();
        }

        return tempBucketName;
    }

    private String AWSGenerateURL(AmazonS3 s3Client, String tempBucketName, String fileObjectName, String fileRealName) {
        // Set the pre-signed URL to expire after 60 seconds.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
//        expTimeMillis += 1000 * 60 * 60; // one hour
//        expTimeMillis += 1000 * 60 * 60 * 24 ; // one day
        expTimeMillis += 1000 * 60 * 60 * 24 * 7; // one week
        expiration.setTime(expTimeMillis);
//            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(storageConfigDto.getAwsBucketName(), fileObjectName)
//                    .withMethod( HttpMethod.PUT)
//                    .withExpiration(expiration);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(tempBucketName, fileRealName)
                .withMethod( HttpMethod.PUT )
                .withExpiration(expiration);

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}
