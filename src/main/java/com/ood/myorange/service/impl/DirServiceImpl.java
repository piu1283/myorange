package com.ood.myorange.service.impl;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.dao.UserDirDao;
import com.ood.myorange.dto.DirsDto;
import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.dto.request.AddDirRequest;
import com.ood.myorange.exception.ForbiddenException;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.pojo.UserDir;
import com.ood.myorange.service.DirService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
@Service
public class DirServiceImpl implements DirService {

    @Autowired
    ICurrentAccount currentAccount;

    @Autowired
    UserDirDao userDirDao;

    @Override
    public List<DirsDto> searchByName(String keyword) {
        List<DirsDto> res = new ArrayList<>();
        if (StringUtils.isBlank(keyword)) {
            return res;
        }
        List<UserDir> userDirs = userDirDao.searchUserDirByKeyWord("%" + keyword + "%");
        userDirs.forEach(userDir -> {
            res.add(convertObject(userDir));
        });
        return res;
    }

    @Override
    public List<DirsDto> getAllDirUnderTarget(int dirId) {
        List<DirsDto> res = new ArrayList<>();
        List<UserDir> userDirs = userDirDao.getUserDirUnderTarget(dirId);
        userDirs.forEach(userDir -> {
            DirsDto dirsDto = convertObject(userDir);
            dirsDto.setCreateDate(userDir.getCreateTime());
            res.add(dirsDto);
        });
        return res;
    }

    @Override
    public UserDir getUserDir(int dirId) {
        return userDirDao.selectByPrimaryKey(new UserDir(dirId));
    }

    @Override
    public DirsDto getRootDir(int userId) {
        UserDir rootDir = userDirDao.getRootDir(userId);
        return convertObject(rootDir);
    }

    @Override
    public List<Integer> isValidDirs(List<Integer> dirIds) {
        UserInfo userInfo = currentAccount.getUserInfo();
        return userDirDao.checkDirsByIdAndUserId(dirIds, userInfo.getId());
    }

    @Override
    public void checkDir(UserDir userDir, boolean checkRoot) {
        if (userDir.getDeleted()) {
            throw new ResourceNotFoundException("Directory you want is not exist.");
        }
        if (!userDir.getUserId().equals(currentAccount.getUserInfo().getId())) {
            throw new ForbiddenException("No permission for the request directory id.");
        }
        if (checkRoot) {
            if (userDir.getDefaultDir()) {
                throw new InvalidRequestException("Cannot modify root directory.");
            }
        }
    }

    @Override
    public void updateDirName(String name, int id) {
        UserDir updateObj = new UserDir(id);
        UserDir userDir = userDirDao.selectByPrimaryKey(updateObj);
        checkDir(userDir, true);
        updateObj.setDirName(name);
        userDirDao.updateByPrimaryKeySelective(updateObj);
    }

    @Override
    public void addDir(AddDirRequest request) {
        UserDir userDir = getUserDir(request.getParentId());
        checkDir(userDir, false);
        UserDir insertObj = new UserDir();
        insertObj.setDirName(request.getName());
        insertObj.setDefaultDir(false);
        insertObj.setParentId(request.getParentId());
        insertObj.setUserId(currentAccount.getUserInfo().getId());
        userDirDao.insertSelective(insertObj);
    }

    @Override
    public void moveDirToTargetDir(Integer dirId, Integer targetId) {
        UserDir userDir = getUserDir(dirId);
        UserDir targetUserDir = getUserDir(targetId);
        checkDir(userDir, true);
        checkDir(targetUserDir,false);
        UserDir dirA = userDirDao.checkDirAIsUnderB(dirId, targetId);
        if (dirA == null || dirA.getDirId() == null) {
            userDirDao.updateParentIdOfDir(dirId, targetId);
        } else {
            throw new InvalidRequestException("Target directory cannot be a child of the from directory.");
        }
    }

    @Override
    public void deleteDir(int dirId) {
        userDirDao.deleteDirAndItsChildByUpdate(dirId);
    }

    @Override
    public void createDefaultDir(int userId) {
        UserDir userDir = new UserDir();
        userDir.setUserId(userId);
        userDir.setParentId(0);
        userDir.setDirName("/");
        userDir.setDefaultDir(true);
        userDir.setDeleted(false);
        userDirDao.insertSelective(userDir);
    }

    private DirsDto convertObject(UserDir userDir) {
        DirsDto dirsDto = new DirsDto();
        dirsDto.setId(userDir.getDirId());
        dirsDto.setName(userDir.getDirName());
        return dirsDto;
    }
}
