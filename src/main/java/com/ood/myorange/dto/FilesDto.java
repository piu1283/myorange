package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ood.myorange.constant.enumeration.FileType;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by Chen on 3/29/20.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilesDto {
    private int id;
    private String name;
    private String size;
    private FileType type;
    private String suffixes;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone="GMT-5")
    private Timestamp createDate;

}
