package com.ood.myorange.service.impl;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.dao.UserFileDao;
import com.ood.myorange.dto.FilesDto;
import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.exception.ForbiddenException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.UserFile;
import com.ood.myorange.service.FileService;
import com.ood.myorange.util.SizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    UserFileDao userFileDao;

    @Autowired
    ICurrentAccount currentAccount;

    @Override
    public List<FilesDto> getSearchResult(String keyword, FileType fileType) {
        List<FilesDto> res = new ArrayList<>();
        List<UserFile> file = userFileDao.getFileByKeyWordAndType("%" + keyword + "%", fileType);
        file.forEach(userFile -> {
            FilesDto filesDto = convertObject(userFile);
            filesDto.setSuffixes(userFile.getSuffixes());
            res.add(filesDto);
        });
        return res;
    }

    @Override
    public List<FilesDto> getAllFileUnderTarget(int dirId) {
        List<FilesDto> res = new ArrayList<>();
        List<UserFile> file = userFileDao.getFilesByTargetDir(dirId);
        file.forEach(userFile -> {
            FilesDto filesDto = convertObject(userFile);
            filesDto.setSize(SizeUtil.getPrintSize(userFile.getFileSize()));
            filesDto.setCreateDate(userFile.getCreateTime());
            filesDto.setSuffixes(userFile.getSuffixes());
            res.add(filesDto);
        });
        return res;
    }

    @Override
    public List<Integer> isValidFiles(List<Integer> filesIds) {
        UserInfo userInfo = currentAccount.getUserInfo();
        return userFileDao.checkFilesByIdAndUserId(filesIds, userInfo.getId());
    }

    @Override
    public void checkFile(UserFile userFile) {
        if (userFile.getDeleted()) {
            throw new ResourceNotFoundException("File you want is not exist.");
        }
        if (!userFile.getUserId().equals(currentAccount.getUserInfo().getId())) {
            throw new ForbiddenException("No permission for the request file id.");
        }
    }

    @Override
    public void updateFileName(String name, int id) {
        UserFile updateObj = new UserFile(id);
        UserFile userFile = getUserFileById(id);
        checkFile(userFile);
        updateObj.setFileName(name);
        userFileDao.updateByPrimaryKeySelective(updateObj);
    }

    @Override
    public void deleteFile(int fileId) {
        UserFile userFile = getUserFileById(fileId);
        checkFile(userFile);
        userFileDao.deleteFile(fileId);
    }

    @Override
    public void deleteFileUnderDirAndItsChildren(int dirId) {
        userFileDao.deleteFilesUnderDirAndItsChildrenByUpdate(dirId);
    }

    @Override
    public UserFile getUserFileById(int fileId) {
        return userFileDao.selectByPrimaryKey(new UserFile(fileId));
    }


    private FilesDto convertObject(UserFile userFile) {
        FilesDto filesDto = new FilesDto();
        filesDto.setId(userFile.getFileId());
        filesDto.setName(userFile.getFileName());
        filesDto.setType(userFile.getFileType());
        return filesDto;
    }
}
