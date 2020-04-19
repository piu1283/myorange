package com.ood.myorange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.FileUploadDto;
import com.ood.myorange.dto.response.PreSignedUrlResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Guancheng Lai on 04/09/2020
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @GetMapping("/upload")
    public PreSignedUrlResponse getPreSignedURL (
            @RequestParam(value = "md5", required = true) String md5,
            @RequestParam(value = "size", required = true) Long size,
            @RequestParam(value = "fileName", required = true) String fileName,
            @RequestParam(value = "dirId", required = true) Integer dirId
    ) throws JsonProcessingException {
        if (size <= 0) { throw new InvalidRequestException( "Invalid file size: " + size); }
        if (md5.length() != 32) { throw new InvalidRequestException( "Invalid file md5: " + md5); }
        if (fileName.isEmpty()) { throw new InvalidRequestException( "Invalid file name: " + fileName); }
        if (dirId < 0) { throw new InvalidRequestException( "Invalid dirID: " + dirId); }

        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setMD5( md5 );
        fileUploadDto.setSize( size );
        fileUploadDto.setName( fileName );
        fileUploadDto.setDirId( dirId );
        return uploadService.getPreSignedUrl( fileUploadDto );
    }

    @PostMapping("/upload")
    public void uploadFinished(
            @RequestParam(value = "dirId", required = true) int dirId,
            @RequestParam(value = "fileName", required = true) String fileName,
            @RequestParam(value = "md5", required = true) String md5,
            @RequestParam(value = "size", required = true) Long size,
            @RequestParam(value = "uploadKey", required = true) String uploadKey
    ) throws JsonProcessingException {
        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setMD5( md5 );
        fileUploadDto.setSize( size );
        fileUploadDto.setName( fileName );
        fileUploadDto.setDirId( dirId );
        fileUploadDto.setUploadKey( uploadKey );
        uploadService.uploadFinished( fileUploadDto );
    }
}