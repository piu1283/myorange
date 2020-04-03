package com.ood.myorange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by Chen on 3/29/20.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DirsDto {
    private int id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp createDate;
}
