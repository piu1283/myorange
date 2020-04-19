package com.ood.myorange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.constant.enumeration.ShareType;
import com.ood.myorange.dao.ShareFileDao;
import com.ood.myorange.dto.ShareFileDto;
import com.ood.myorange.pojo.ShareFile;
import com.ood.myorange.pojo.UserFile;
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

        List<ShareFile> shareFileList=shareFileDao.SelectAllShareFileInfo();
        List<ShareFileDto> result=new ArrayList<>();
        for (ShareFile sf : shareFileList) {
            ShareFileDto shareFileDto=convertShareFilePojoToShareFileDto(sf);
            result.add(shareFileDto);
        }
        return result;
    }

    @Override
    public ShareFileDto getShareFileByShareKey(String shareKey) throws JsonProcessingException{
        ShareFile shareFile=shareFileDao.SelectShareFileByShareKey(shareKey);
        ShareFileDto result = convertShareFilePojoToShareFileDto(shareFile);
        return result;
    }

    @Override
    public ShareFileDto updateShareFile(int shareId,Timestamp deadline,int limitDownloadTimes,boolean hasPassword) throws JsonProcessingException{
        ShareType shareType=hasPassword?ShareType.getShareType("pwd"):ShareType.getShareType("nonepwd");
        shareFileDao.updateShareFileById(shareId,deadline,limitDownloadTimes,shareType);
        ShareFile shareFile=shareFileDao.selectByPrimaryKey(shareId);
        ShareFileDto result = convertShareFilePojoToShareFileDto(shareFile);
        return result;
    }

    @Override
    public void deleteShareFile(int shareId) throws JsonProcessingException{
        shareFileDao.deleteByPrimaryKey(shareId);
    }

    @Override
    public ShareFileDto addShareFile(int fileID, Timestamp deadline, int limitDownloadTimes, boolean hasPassword) throws JsonProcessingException {
//        ShareFileDto shareFileDto=new ShareFileDto();
//        //check if file is already shared
//        //check file belong to user or not
//        checkFileAndUser(fileID);
//        //get data
//        shareFileDto
        return null;
    }

    /*---------------------------------------------------helper-function-------------------------------------------------*/

    public ShareFileDto convertShareFilePojoToShareFileDto(ShareFile sf){
        UserFile userFile=fileService.getUserFileById(sf.getFileID());
        ShareFileDto shareFileDto=new ShareFileDto();

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
        return shareFileDto;
    }

}
