package com.ood.myorange.service.impl;

import com.ood.myorange.auth.ICurrentAccount;
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
import com.ood.myorange.util.ShareUtil;
import com.ood.myorange.util.SizeUtil;
import com.ood.myorange.util.TimeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Linkun on 2/24/20.
 */
@Service
public class ShareFileServiceImpl implements ShareFileService {
    @Autowired
    ShareFileDao shareFileDao;

    @Autowired
    FileService fileService;

    @Autowired
    ICurrentAccount currentAccount;

    @Override
    public List<ShareFileDto> getAllShareFiles() {
        int userId = currentAccount.getUserInfo().getId();
        List<ShareFile> shareFileList = shareFileDao.SelectAllShareFileInfo(userId);
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
        ShareFile shareFile = shareFileDao.SelectShareFileByShareKey(shareKey);
        if (shareFile == null) throw new ResourceNotFoundException("share key not found");
        if (shareFile.getShareType() == ShareType.PWD) {
            if (!shareFile.getSharePass().equals(password)) {
                throw new ForbiddenException("wrong password or password not conveyed, forbidden to visit share file");
            }
        }
        if (validateDeadline(shareFile)) {
            return mergeShareFileAndUserFileToShareFileDto(shareFile);
        } else {
            //deadline expire
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
        ShareFile shareFile = shareFileDao.SelectShareFileByShareId(shareId);
        if (shareFile == null) throw new ResourceNotFoundException("share id not found");
        UserFile userFile = fileService.getUserFileById(shareFile.getFileID());
        if (!shareFile.getUserId().equals(userFile.getUserId())) {
            throw new ForbiddenException("fail to delete, this file not belong to this user");
        }
        fileService.changeFileStatus(shareFile.getFileID(), FileStatus.NORMAL);
        shareFileDao.deleteShareFile(shareId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ShareFileDto addShareFile(int fileID, Timestamp deadline, int limitDownloadTimes, boolean hasPassword) {
        UserFile userFile = fileService.getUserFileById(fileID);
        if (userFile.getFileStatus() == FileStatus.SHARED) {
            ShareFile shareFile = shareFileDao.SelectShareFileByFileId(fileID);
            return updateShareFile(shareFile.getId(), deadline, limitDownloadTimes, hasPassword);
        }
        fileService.changeFileStatus(userFile.getFileId(), FileStatus.SHARED);
        ShareType shareType = hasPassword ? ShareType.PWD : ShareType.NONEPWD;
        String sharePass = generateSharePass();
        String shareKey = generateShareKey(userFile);
        if (limitDownloadTimes <= 0) {
            limitDownloadTimes = -1;
        }
        shareFileDao.insertShareFile(userFile.getUserId(), userFile.getFileId(), shareType, sharePass, deadline, shareKey, limitDownloadTimes);
        ShareFile sf = shareFileDao.SelectShareFileByShareKey(shareKey);
        return mergeShareFileAndUserFileToShareFileDto(sf);
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
        if(sf.getShareType() != ShareType.PWD){
            shareFileDto.setPassword("");
        }else {
            shareFileDto.setPassword(sf.getSharePass());
        }
        shareFileDto.setShareKey(sf.getShareKey());
        shareFileDto.setType(userFile.getFileType());
        shareFileDto.setSuffixes(userFile.getSuffixes());
        shareFileDto.setSize(SizeUtil.getPrintSize(userFile.getFileSize()));
        // add share url logic
        String shareUrl = ShareUtil.generateShareUrl(sf.getShareKey());
        shareFileDto.setShareUrl(shareUrl);
        return shareFileDto;
    }

    public boolean validateDeadline(ShareFile share) {
        return share.getShareDeadline().getTime() >= TimeUtil.getCurrentTimeStamp().getTime();
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
