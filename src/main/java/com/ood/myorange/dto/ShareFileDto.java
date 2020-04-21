package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ood.myorange.constant.enumeration.FileType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

/**
 * Created by Linkun on 4/13/20.
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShareFileDto {
    private String shareIp;
    private int shareId;
    private int fileId;
    private String name;
    private FileType type;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp deadline;
    private int limitDownloadTimes;
    private int DownloadTimes;
    private boolean hasPassword;
    private String shareKey;
    private String password;
    private Long size;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;

}
