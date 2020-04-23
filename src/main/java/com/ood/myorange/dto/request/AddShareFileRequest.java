package com.ood.myorange.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AddShareFileRequest {
    private Integer fileId;
    private Timestamp deadline;
    private Integer limitDownloadTimes;
    private Boolean hasPassword;

}
