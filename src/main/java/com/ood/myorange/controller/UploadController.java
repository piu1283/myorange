package com.ood.myorange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.FileUploadDto;
import com.ood.myorange.dto.response.PreSignedUrlResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
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
    @PreAuthorize("hasAuthority('UPLOAD')")
    public PreSignedUrlResponse getPreSignedURL (
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") Long fileSize,
            @RequestParam("fileMD5") String fileMD5,
            @RequestParam("dirId") Integer dirId
    ) throws JsonProcessingException {
        FileUploadDto fileUploadDto = new FileUploadDto();
        fileUploadDto.setFileName( fileName );
        fileUploadDto.setSize( fileSize );
        fileUploadDto.setMD5( fileMD5 );
        fileUploadDto.setDirId( dirId );
        validateFileUploadDto( fileUploadDto,true );
        return uploadService.getPreSignedUrl( fileUploadDto );
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('UPLOAD')")
    public void uploadFinished(
            @RequestBody FileUploadDto fileUploadDto
    ) throws JsonProcessingException {
        validateFileUploadDto( fileUploadDto,false );
        uploadService.uploadFinished( fileUploadDto );
    }

    private void validateFileUploadDto(FileUploadDto fileUploadDto, boolean firstTime) {
        if (fileUploadDto.getSize() <= 0) { throw new InvalidRequestException( "Invalid file size: " + fileUploadDto.getSize()); }
        if (fileUploadDto.getMD5().length() != 32) { throw new InvalidRequestException( "Invalid file md5: " + fileUploadDto.getMD5()); }
        if (fileUploadDto.getFileName().isEmpty()) { throw new InvalidRequestException( "Invalid file name: " + fileUploadDto.getFileName()); }
        if (fileUploadDto.getDirId() < 0) { throw new InvalidRequestException( "Invalid dirID: " + fileUploadDto.getDirId()); }
        if (!firstTime && StringUtils.isBlank( fileUploadDto.getUploadKey() ) ) { throw new InvalidRequestException( "Invalid uploadKey: " + fileUploadDto.getUploadKey()); }
    }
}