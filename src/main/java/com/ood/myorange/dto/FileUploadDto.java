package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * TODO check API define
 * Created by Chen on 4/15/20.
 */
@Data
@ToString
public class FileUploadDto {
    private String fileName;
    private Long size;
    private String MD5;
    private Integer dirId;
    private String uploadKey; // Only needed in the second upload request
}
