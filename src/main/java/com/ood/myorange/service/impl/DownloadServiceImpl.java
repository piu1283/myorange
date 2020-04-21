package com.ood.myorange.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.auth.CurrentAccount;
import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageConfiguration;
import com.ood.myorange.config.storage.StorageType;
import com.ood.myorange.constant.enumeration.FileStatus;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.dto.response.PreSignedUrlResponse;
import com.ood.myorange.exception.ForbiddenException;
import com.ood.myorange.exception.InternalServerError;
import com.ood.myorange.pojo.OriginalFile;
import com.ood.myorange.pojo.UserFile;
import com.ood.myorange.service.DownloadService;
import com.ood.myorange.service.FileService;
import com.ood.myorange.service.StorageConfigService;
import com.ood.myorange.util.AuthUtil;
import com.ood.myorange.util.StorageConfigUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Guancheng Lai on 04/09/2020
 */
@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    FileService fileService;

    @Autowired
    StorageConfigService storageConfigService;

    @Autowired
    ICurrentAccount currentAccount;

    @Override
    public PreSignedUrlResponse getPreSignedUrl(int fileId) throws JsonProcessingException {
        UserFile file = fileService.getUserFileById( fileId );

        if(!file.getFileStatus().equals( FileStatus.SHARED )){
            fileService.checkFile( file );
            if ( !AuthUtil.canDownload( currentAccount.getUserInfo() ) ){
                throw new ForbiddenException( "User does not have download permission" );
            }
        }

        // Get original file DTO
        OriginalFile originFile = fileService.getOriginalFileByFileId( file.getFileId() );

        String fileObjectName = originFile.getFileMd5() + "_" + originFile.getFileSize();

        // Construct user file name
        String filePrefix = file.getSuffixes();
        String userFileName;
        if (filePrefix.length() != 0) {
            userFileName = file.getFileName() + "." + filePrefix;
        }
        else {
            userFileName = file.getFileName();
        }

        PreSignedUrlResponse preSignedUrlResponse = new PreSignedUrlResponse();

        StorageType storageType = StorageConfigUtil.getStorageConfigurationType( originFile.getSourceId() );
        StorageConfiguration storageConfiguration = StorageConfigUtil.getStorageConfiguration( originFile.getSourceId() );
        switch (storageType) {
            case AWS:
                preSignedUrlResponse = AWSDownload((S3Configuration)storageConfiguration, fileObjectName, userFileName, originFile.getSourceId());
                break;
            case AZURE:
                preSignedUrlResponse = AzureDownload((AzureConfiguration) storageConfiguration, fileObjectName, userFileName);
                break;
            case LOCAL:
                preSignedUrlResponse.setDownloadUrl( "TODO.LOCAL" );
                break;
            default:
                throw new InternalServerError("Invalid storage type: " + storageType);
        }

        return preSignedUrlResponse;
    }

    public PreSignedUrlResponse AWSDownload(S3Configuration s3Configuration, String fileObjectName, String userFileName, int configId) {
        PreSignedUrlResponse preSignedUrlResponse = new PreSignedUrlResponse();

        AmazonS3 s3Client = (AmazonS3) StorageConfigUtil.getStorageClient( StorageType.AWS );
        if (s3Client == null) {
            StorageConfigDto storageConfigDto = new StorageConfigDto();
            StorageConfigUtil.insertStorageConfig( configId,"AWS",s3Configuration  );
        }

        try {
            // Throw exception if file not exist
            S3Object myFile = s3Client.getObject( s3Configuration.getAwsBucketName(), fileObjectName );

            // Expiration date
            DateTime expireDateTime = new DateTime().plusMinutes( 1 );
            Date expireDate = expireDateTime.toDate();

            ResponseHeaderOverrides headerOverrides = new ResponseHeaderOverrides();
            headerOverrides.setContentDisposition("attachment;filename=" + userFileName);
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest( s3Configuration.getAwsBucketName(), fileObjectName )
                            .withMethod( HttpMethod.GET )
                            .withExpiration( expireDate );
            URL url = s3Client.generatePresignedUrl( generatePresignedUrlRequest );
            preSignedUrlResponse.setDownloadUrl( url.toString() );
            preSignedUrlResponse.setExpiredCountdown( new Timestamp( expireDateTime.getMillisOfSecond() ) );
        } catch (AmazonServiceException e) {
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

    private PreSignedUrlResponse AzureDownload(AzureConfiguration storageConfiguration, String fileObjectName, String userFileName) {
        // TODO
        return null;
    }
}
