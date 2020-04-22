package com.ood.myorange.service.impl;

import com.ood.myorange.constant.enumeration.FileStatus;
import com.ood.myorange.constant.enumeration.ShareType;
import com.ood.myorange.dao.ShareFileDao;
import com.ood.myorange.dto.ShareFileDto;
import com.ood.myorange.exception.ForbiddenException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.ShareFile;
import com.ood.myorange.pojo.UserFile;
import com.ood.myorange.service.FileService;
import com.ood.myorange.service.ShareFileService;
import com.ood.myorange.util.InternetIpUtil;
import jdk.internal.org.objectweb.asm.tree.FrameNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public ShareFileDto getShareFileByShareKey(String shareKey, String password) {
        // valid
        // need password?
        // if need, {password wrong or dont have(throw new forbiddenexception)},{normal output}
        // {normal output}
        ShareFile shareFile = shareFileDao.SelectShareFileByShareKey(shareKey);
        if(shareFile==null) throw new ResourceNotFoundException("share key not found");
        if (shareFile.getShareType() == ShareType.PWD) {
            if (password == null || !shareFile.getSharePass().equals(password)) {
                throw new ForbiddenException("wrong password or password not conveyed, forbidden to visit share file");
            }
        }
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
        ShareFile shareFile = shareFileDao.SelectShareFileByShareId(shareId);
        return mergeShareFileAndUserFileToShareFileDto(shareFile);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteShareFile(int shareId) {
        // 判断所属用户， 但是通过过期删除的不需要判断
        ShareFile shareFile=shareFileDao.SelectShareFileByShareId(shareId);
        if(shareFile==null) throw new ResourceNotFoundException("share id not found");
        if(validateDeadline(shareId)) {
            UserFile userFile = fileService.getUserFileById(shareFile.getFileID());
            if(shareFile.getUserId()!=userFile.getUserId()){
                throw new ForbiddenException("fail to delete, this file not belong to this user");
            }
        }
        // change file status
        fileService.changeFileStatus(shareFile.getFileID(), FileStatus.NORMAL);
        shareFileDao.deleteShareFile(shareId);
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
        ShareType shareType = hasPassword ? ShareType.PWD : ShareType.NONEPWD;
        String sharePass = generateSharePass();
        String shareKey = generateShareKey(userFile);
        String ip = InternetIpUtil.INTERNET_IP;
        shareFileDao.insertShareFile(userFile.getUserId(), userFile.getFileId(), shareType, sharePass, deadline, shareKey, ip);
        if(!hasPassword)
        return getShareFileByShareKey(shareKey, "");
        else{
            ShareFile shareFile=shareFileDao.SelectShareFileByShareKey(shareKey);
            return getShareFileByShareKey(shareKey, shareFile.getSharePass());
        }
    }


    public ShareFileDto mergeShareFileAndUserFileToShareFileDto(ShareFile sf) {
        UserFile userFile = fileService.getUserFileById(sf.getFileID());
        ShareFileDto shareFileDto = new ShareFileDto();

        shareFileDto.setShareId(sf.getId());
        shareFileDto.setCreateDate(sf.getCreateTime());
        shareFileDto.setDeadline(sf.getShareDeadline());
        shareFileDto.setDownloadTimes(sf.getDownloadCount());
        shareFileDto.setFileId(sf.getFileID());
        shareFileDto.setHasPassword(sf.getShareType() == ShareType.PWD);
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

    public String generateSharePass() {
        Random rnd = new Random();
        return Integer.toString(1000 + rnd.nextInt(9000));
    }


    public String generateShareKey(UserFile userFile) {
        return (char) ('a' + userFile.getFileId() % 26)
                + RandomStringUtils.random(4, true, true)
                + userFile.getFileId()
                + (char) ('a' + userFile.getUserId() % 26);
    }

}