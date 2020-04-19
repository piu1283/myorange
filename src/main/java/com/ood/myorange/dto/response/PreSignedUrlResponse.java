package com.ood.myorange.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by Guancheng Lai on 04/09/2020
 */
@Data
public class PreSignedUrlResponse {
    String downloadUrl;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp expiredCountdown;

    String uploadUrl;
    String uploadKey;
}
