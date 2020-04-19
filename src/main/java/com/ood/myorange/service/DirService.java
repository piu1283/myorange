package com.ood.myorange.service;

import com.ood.myorange.dto.DirsDto;
import com.ood.myorange.dto.request.AddDirRequest;
import com.ood.myorange.pojo.UserDir;

import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
public interface DirService {

    /**
     * search dir by keyword
     *
     * @param keyword keyword of name
     * @return list of dirsDto
     */
    List<DirsDto> searchByName(String keyword);

    /**
     * get all dirs from its parent dir
     * @param dirId dir id
     * @return
     */
    List<DirsDto> getAllDirUnderTarget(int dirId);

    /**
     * check whether an dir is deleted
     * @param dirId
     * @return
     */
    UserDir getUserDir(int dirId);

    /**
     * get root Dir by user id
     * @param userId id of user
     * @return
     */
    DirsDto getRootDir(int userId);

    /**
     * judge whether dir is valid
     * 1. whether dir id deleted
     * 2. whether dir is belong to this user
     * @param dirIds dir ids
     * @return invalid integer ids
     */
    List<Integer> isValidDirs(List<Integer> dirIds);

    /**
     * judge whether a dir is valid
     * 1. whether dir is deleted
     * 2. whether dir is belong to this user
     *
     * if invalid , throw exception
     * @param userDir
     */
    void checkDir(UserDir userDir, boolean checkRoot);

    /**
     * update name of a dir
     * @param name
     * @param id
     */
    void updateDirName(String name, int id);

    /**
     * add dir
     * @param
     */
    void addDir(AddDirRequest request);

    /**
     * move dir to target dir
     * @param dirId
     * @param targetId
     */
    void moveDirToTargetDir(Integer dirId, Integer targetId);


    /**
     * delete dir and its children by id
     * @param dirId
     */
    void deleteDir(int dirId);

    /**
     * create default root dir for new user
     * @param userId
     */
    void createDefaultDir(int userId);

}
