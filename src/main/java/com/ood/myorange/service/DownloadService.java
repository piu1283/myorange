package com.ood.myorange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.response.PreSignedUrlResponse;

/**
 * Created by Guancheng Lai on 04/09/2020
 */
public interface DownloadService {
    PreSignedUrlResponse getPreSignedUrl(int fileId) throws JsonProcessingException;
}
