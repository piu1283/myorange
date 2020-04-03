package com.ood.myorange.dto.request;

import lombok.Data;

import java.util.List;

/**
 * Created by Chen on 3/31/20.
 */
@Data
public class MoveRequest {
    private List<Integer> fromFileId;
    private Integer fromDirId;
    private Integer toId;
}
