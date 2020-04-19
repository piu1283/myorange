package com.ood.myorange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dao.ShareFileDao;
import com.ood.myorange.dto.ShareFileDto;
import com.ood.myorange.pojo.ShareFile;
import com.ood.myorange.service.FileService;
import com.ood.myorange.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 2/24/20.
 */
@Service
public class ShareFileServiceImpl implements ShareFileService {
    @Autowired
    ShareFileDao shareFileDao;

    @Autowired
    FileService fileService;


    @Override
    public List<ShareFileDto> getAllShareFiles() throws JsonProcessingException {
        return null;
    }

    @Override
    public ShareFileDto addShareFile(int fileID, Timestamp deadline, int limitDownloadTimes, boolean hasPassword) throws JsonProcessingException {
        ShareFileDto shareFileDto=new ShareFileDto();
        //check if file is already shared
        //check file belong to user or not
        checkFileAndUser(fileID);
        //get data
        shareFileDto
        return shareFileDto;
    }

    public boolean

}
