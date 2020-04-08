package com.ood.myorange.dto.response;

import com.ood.myorange.dto.DirsDto;
import com.ood.myorange.dto.FilesDto;
import lombok.Data;

import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
@Data
public class FileDirResponse {
    private Integer preId;
    private Integer currId;
    private String name;
    private List<FilesDto> files;
    private List<DirsDto> dirs;
}
