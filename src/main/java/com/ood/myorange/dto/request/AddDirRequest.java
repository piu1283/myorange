package com.ood.myorange.dto.request;

import lombok.Data;

/**
 * Created by Chen on 4/1/20.
 */
@Data
public class AddDirRequest {
    private String name;
    private Integer parentId;
}
