package com.ood.myorange.service.impl;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.dao.UserDirDao;
import com.ood.myorange.dao.UserFileDao;
import com.ood.myorange.dto.DirsDto;
import com.ood.myorange.dto.FilesDto;
import com.ood.myorange.dto.response.FileDirResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.pojo.UserDir;
import com.ood.myorange.service.DirService;
import com.ood.myorange.service.FileDirService;
import com.ood.myorange.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
@Service
public class FileDirServiceImpl implements FileDirService {

    @Autowired
    FileService fileService;

    @Autowired
    DirService dirService;

    @Autowired
    UserDirDao userDirDao;

    @Autowired
    UserFileDao userFileDao;

    @Autowired
    ICurrentAccount currentAccount;

    @Override
    public FileDirResponse getFileDirSearchResult(String keyword, FileType fileType) {
        List<FilesDto> fileRes = fileService.getSearchResult(keyword, fileType);
        List<DirsDto> dirRes = new ArrayList<>();
        if (!StringUtils.isBlank(keyword)) {
            dirRes = dirService.searchByName(keyword);
        }
        FileDirResponse response = new FileDirResponse();
        response.setDirs(dirRes);
        response.setFiles(fileRes);
        return response;
    }

    @Override
    public FileDirResponse getFileDirUnderTarget(int dirId, boolean onlyDir) {
        UserDir userDir = dirService.getUserDir(dirId);
        dirService.checkDir(userDir, false);
        FileDirResponse response = new FileDirResponse();
        List<DirsDto> dirsDtos = dirService.getAllDirUnderTarget(dirId);
        List<FilesDto> filesDtos = new ArrayList<>();
        if(!onlyDir) {
            filesDtos = fileService.getAllFileUnderTarget(dirId);
        }
        response.setName(userDir.getDirName());
        response.setCurrId(userDir.getDirId());
        response.setPreId(userDir.getParentId());
        response.setDirs(dirsDtos);
        response.setFiles(filesDtos);
        return response;
    }


    @Override
    public void moveFilesToTarget(List<Integer> fileIds, int targetDirId) {
        List<Integer> invalidFiles = fileService.isValidFiles(fileIds);
        if (!invalidFiles.isEmpty()) {
            throw new InvalidRequestException("Invalid directory ids in request.", invalidFiles);
        }
        UserDir userDir = dirService.getUserDir(targetDirId);
        dirService.checkDir(userDir, false);
        userFileDao.updateParentIdOfFiles(fileIds, targetDirId);
    }

    @Override
    // using Transactional
    @Transactional
    public void deleteDirAndFilesRecursively(int dirId) {
        UserDir userDir = dirService.getUserDir(dirId);
        dirService.checkDir(userDir, true);
        dirService.deleteDir(dirId);
        fileService.deleteFileUnderDirAndItsChildren(dirId);
    }

}
