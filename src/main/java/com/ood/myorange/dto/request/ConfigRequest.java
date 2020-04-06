package com.ood.myorange.dto.request;

import lombok.Data;

/**
 * Created by Guancheng Lai on 3/31/20.
 */
@Data
public class ConfigRequest {
    private String name;
    private String type;
    private String aws_access_key_id;
    private String aws_secret_access_key;
    private String region;
    private String bucketName;
    private String azure_token;
}
