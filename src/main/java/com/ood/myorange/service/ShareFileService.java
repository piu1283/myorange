package com.ood.myorange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.dto.ShareFileDto;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Linkun on 4/13/2020
 */

public interface ShareFileService {

    List<ShareFileDto> getAllShareFiles() throws JsonProcessingException;
    ShareFileDto getShareFileByShareKey(String shareKey) throws JsonProcessingException;
    ShareFileDto updateShareFile(int shareId,Timestamp deadline,int limitDownloadTimes,boolean hasPassword) throws JsonProcessingException;
    void deleteShareFile(int shareId) throws JsonProcessingException;
    ShareFileDto addShareFile(int fileID, Timestamp deadline, int limitDownloadTimes, boolean hasPassword) throws JsonProcessingException;

}
