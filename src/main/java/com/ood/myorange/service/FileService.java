package com.ood.myorange.service;

import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.dto.FilesDto;
import com.ood.myorange.pojo.UserFile;

import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
public interface FileService {
    /**
     * get search result of file
     * @param keyword name keyword
     * @param fileType file type (enum)
     * @return list of FilesDto
     */
    List<FilesDto> getSearchResult(String keyword, FileType fileType);

    /**
     * get all files from its parent dir
     * @param dirId dir id
     * @return
     */
    List<FilesDto> getAllFileUnderTarget(int dirId);

    /**
     * judge whether files are valid
     * 1. whether file are deleted
     * 2. whether file is belong to this user
     *
     * @param filesIds dir ids
     * @return invalid integer ids
     */
    List<Integer> isValidFiles(List<Integer> filesIds);

    /**
     * judge whether files are valid
     * 1. whether file are deleted
     * 2. whether file is belong to this user
     *
     * if invalid , throw exception
     * @param userFile
     */
    void checkFile(UserFile userFile);

    /**
     * update name of a file
     * @param name
     * @param id
     */
    void updateFileName(String name, int id);

    /**
     * delete a file through setting 'deleted' to true
     * @param fileId
     */
    void deleteFile(int fileId);

    /**
     * recursively delete files of a dir and its children
     * not real deletion, only update
     *
     * @param dirId
     */
    void deleteFileUnderDirAndItsChildren(int dirId);

    UserFile getUserFileById(int fileId);
}
