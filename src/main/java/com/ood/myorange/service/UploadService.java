package com.ood.myorange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.FileUploadDto;
import com.ood.myorange.dto.response.PreSignedUrlResponse;


/**
 * Created by Guancheng Lai
 */
public interface UploadService {
    PreSignedUrlResponse getPreSignedUrl(FileUploadDto fileUploadDto) throws JsonProcessingException;
    void uploadFinished(FileUploadDto fileUploadDto) throws JsonProcessingException;
}
