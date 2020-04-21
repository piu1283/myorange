package com.ood.myorange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.constant.enumeration.FileStatus;
import com.ood.myorange.constant.enumeration.ShareType;
import com.ood.myorange.dao.ShareFileDao;
import com.ood.myorange.dto.ShareFileDto;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.ShareFile;
import com.ood.myorange.pojo.UserFile;
import com.ood.myorange.service.FileService;
import com.ood.myorange.service.ShareFileService;
import com.ood.myorange.util.InternetIpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.activity.InvalidActivityException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.alibaba.druid.util.FnvHash.Constants.CURRENT_TIMESTAMP;

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
    public List<ShareFileDto> getAllShareFiles() {
        List<ShareFile> shareFileList = shareFileDao.SelectAllShareFileInfo();
        List<ShareFileDto> result = new ArrayList<>();
        for (ShareFile sf : shareFileList) {
            ShareFileDto shareFileDto = mergeShareFileAndUserFileToShareFileDto(sf);
            result.add(shareFileDto);
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ShareFileDto getShareFileByShareKey(String shareKey) {
        ShareFile shareFile = shareFileDao.SelectShareFileByShareKey(shareKey);
        if (validateDeadline(shareFile.getId())) {
            return mergeShareFileAndUserFileToShareFileDto(shareFile);
        } else {
            //deadline expire
            deleteShareFile(shareFile.getId());
            throw new ResourceNotFoundException("deadline expired, this file is not shared");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ShareFileDto updateShareFile(int shareId, Timestamp deadline, int limitDownloadTimes, boolean hasPassword) {
        ShareType shareType = hasPassword ? ShareType.PWD : ShareType.NONEPWD;
        shareFileDao.updateShareFileById(shareId, deadline, limitDownloadTimes, shareType);
        ShareFile shareFile = shareFileDao.selectByPrimaryKey(shareId);
        return mergeShareFileAndUserFileToShareFileDto(shareFile);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteShareFile(int shareId) {
        shareFileDao.deleteByPrimaryKey(shareId);
        // change file status
        ShareFile shareFile = shareFileDao.selectByPrimaryKey(shareId);
        fileService.changeFileStatus(shareFile.getFileID(), FileStatus.NORMAL);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ShareFileDto addShareFile(int fileID, Timestamp deadline, int limitDownloadTimes, boolean hasPassword) {
        //check if file is already shared
        //if yes, update and return exist data
        UserFile userFile = fileService.getUserFileById(fileID);
        if (userFile.getFileStatus() == FileStatus.SHARED) {
            ShareFile shareFile = shareFileDao.SelectShareFileByFileId(fileID);
            updateShareFile(shareFile.getId(), deadline, limitDownloadTimes, hasPassword);
            return mergeShareFileAndUserFileToShareFileDto(shareFile);
        }
        //generate data and return
        //update user file status
        fileService.changeFileStatus(userFile.getFileId(), FileStatus.SHARED);
        ShareType shareType = hasPassword ? ShareType.PWD:ShareType.NONEPWD;
        String sharePass=generateSharePass();
        String shareKey=generateShareKey(userFile,deadline);
        String ip= InternetIpUtil.INTERNET_IP;
        shareFileDao.insertShareFile(userFile.getUserId(), userFile.getFileId(), shareType, sharePass, deadline, shareKey,ip);
        return getShareFileByShareKey(shareKey);
    }

    /*---------------------------------------------------helper-function-------------------------------------------------*/

    public ShareFileDto mergeShareFileAndUserFileToShareFileDto(ShareFile sf) {
        UserFile userFile = fileService.getUserFileById(sf.getFileID());
        ShareFileDto shareFileDto = new ShareFileDto();

        shareFileDto.setShareId(sf.getId());
        shareFileDto.setCreateDate(sf.getCreateTime());
        shareFileDto.setDeadline(sf.getShareDeadline());
        shareFileDto.setDownloadTimes(sf.getDownloadCount());
        shareFileDto.setFileId(sf.getFileID());
        shareFileDto.setHasPassword(sf.getShareType() == ShareType.getShareType("pwd"));
        shareFileDto.setLimitDownloadTimes(sf.getDownloadLimitation());
        shareFileDto.setName(userFile.getFileName());
        shareFileDto.setPassword(sf.getSharePass());
        shareFileDto.setSize(userFile.getFileSize());
        shareFileDto.setShareKey(sf.getShareKey());
        shareFileDto.setType(userFile.getFileType());
        shareFileDto.setShareIp(sf.getShareUrl());
        return shareFileDto;
    }

    public boolean validateDeadline(Integer shareId) {
        if (shareFileDao.SelectShareFileIfDeadlineNotExpired(shareId) == null) {
            return false;
        } else
            return true;
    }

    public String generateSharePass(){
        Random rnd=new Random();
        return Integer.toString(1000+rnd.nextInt(9000));
    }

    public String generateShareKey(UserFile userFile, Timestamp deadline){
        return userFile.getFileName()+userFile.getUserId()+deadline.toString();
    }

}
