package com.ood.myorange.service.impl;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.constant.enumeration.FileStatus;
import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.dao.OriginalFileDao;
import com.ood.myorange.dao.UserFileDao;
import com.ood.myorange.dto.FileUploadDto;
import com.ood.myorange.dto.FilesDto;
import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.exception.ForbiddenException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.OriginalFile;
import com.ood.myorange.pojo.UserFile;
import com.ood.myorange.service.FileService;
import com.ood.myorange.util.FileTypeUtil;
import com.ood.myorange.util.NamingUtil;
import com.ood.myorange.util.SizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
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
    OriginalFileDao originalFileDao;

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteFileUnderDirAndItsChildren(int dirId) {
        int userId = currentAccount.getUserInfo().getId();
        List<Integer> fileIds = userFileDao.getAllFileIdUnderDir(dirId);
        if (!CollectionUtils.isEmpty(fileIds)) {
            userFileDao.deleteFilesByFileIds(fileIds);
            userFileDao.updateUsedSizeDecreaseByFileIds(fileIds, userId);
        }
    }

    @Override
    public UserFile getUserFileById(int fileId) {
        return userFileDao.selectByPrimaryKey(new UserFile(fileId));
    }

    @Override
    public OriginalFile getOriginalFileByFileId(int fileId) {
        return originalFileDao.getByFileId(fileId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int addUserFile(FileUploadDto uploadDto, int originId) {
        String fullName = uploadDto.getFileName();
        String[] splitRes = NamingUtil.splitFileName(fullName);
        String fileName = splitRes[0];
        String suffixes = splitRes[1];
        int userId = currentAccount.getUserInfo().getId();
        UserFile userFile = userFileDao.getUserFileByNameAndSuffixesAndDirId(splitRes[0], splitRes[1], uploadDto.getDirId());
        int fileId = 0;
        // if file exist
        if (userFile != null) {
            // first change origin count
            originalFileDao.decreaseRefCountByOriginId(userFile.getOriginId());
            userFile.setFileName(fileName);
            userFile.setSuffixes(suffixes);
            userFile.setCreateTime(new Timestamp(System.currentTimeMillis()));
            userFile.setDeleted(false);
            userFile.setDirId(uploadDto.getDirId());
            userFile.setModifyTime(null);
            userFile.setFileType(FileTypeUtil.getFileTypeBySuffixes(suffixes));
            fileId = userFile.getFileId();
            // then update file
            userFileDao.updateByPrimaryKeySelective(userFile);
        } else {
            // if not exist
            userFile = new UserFile();
            userFile.setFileName(fileName);
            userFile.setSuffixes(suffixes);
            userFile.setDirId(uploadDto.getDirId());
            userFile.setUserId(userId);
            userFile.setOriginId(originId);
            userFile.setFileSize( uploadDto.getSize() );
            userFile.setFileType(FileTypeUtil.getFileTypeBySuffixes(suffixes));
            userFileDao.insertSelective(userFile);
            fileId = userFile.getFileId();
        }
        return fileId;
    }

    @Override
    public boolean checkOriginFileExist(FileUploadDto uploadDto, int sourceId) {
        String originId = NamingUtil.generateOriginFileId(uploadDto.getMD5(), String.valueOf(uploadDto.getSize()));
        OriginalFile originalFile = originalFileDao.getByOriginFileId(originId, sourceId);
        return originalFile != null;
    }

    @Override
    public OriginalFile InsertOrUpdateOriginFile(FileUploadDto uploadDto, int sourceId) {
        String originFileId = NamingUtil.generateOriginFileId(uploadDto.getMD5(), String.valueOf(uploadDto.getSize()));
        OriginalFile of = new OriginalFile();
        of.setOriginFileId(originFileId);
        of.setFileCount(1);
        of.setFileMd5(uploadDto.getMD5());
        of.setFileSize(uploadDto.getSize());
        of.setSourceId(sourceId);
        originalFileDao.insertOrUpdateOriginFile(of);
        return originalFileDao.getByOriginFileId(originFileId, sourceId);
    }

    @Override
    public void changeFileStatus(int fileId, FileStatus status) {
        UserFile uf = new UserFile();
        uf.setFileStatus(status);
        uf.setFileId(fileId);
        userFileDao.updateByPrimaryKeySelective(uf);
    }


    private FilesDto convertObject(UserFile userFile) {
        FilesDto filesDto = new FilesDto();
        filesDto.setId(userFile.getFileId());
        filesDto.setName(userFile.getFileName());
        filesDto.setType(userFile.getFileType());
        return filesDto;
    }
}
