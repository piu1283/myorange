package com.ood.myorange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.response.PreSignedUrlResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Guancheng Lai on 04/09/2020
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @GetMapping("/d/{fileId}")
    public PreSignedUrlResponse getPreSignedURL(@PathVariable("fileId") int fileId) throws JsonProcessingException {
        if (fileId < 0) {
            throw new InvalidRequestException( "Invalid file id: " + fileId);
        }

        return downloadService.getPreSignedUrl( fileId );
    }
}