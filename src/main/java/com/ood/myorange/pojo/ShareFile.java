package com.ood.myorange.pojo;

import com.ood.myorange.constant.enumeration.FileType;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by Linkun on 4/13/20.
 */
@Data
@ToString
@Table(name = "share_file")
public class ShareFile {
    @Id
    @Column(name = "id")
    private int id;
    private int fileID;
    private int fileName;
    private FileType fileType;
    private Timestamp deadline;
    private int limitDownloadTimes;
    private int downloadTimes;
    private boolean hasPassword;
    private String shareKey;
    private String password;
    private String size;
    private Timestamp createdTime;
}
