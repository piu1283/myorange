package com.ood.myorange.service;

import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.dto.response.FileDirResponse;

import java.util.List;

/**
 * Created by Chen on 3/29/20.
 */
public interface FileDirService {

    /**
     * get search result of file and dir
     * @param keyword search keyword for dir and file name
     * @param fileType type for only file
     * @return
     */
    FileDirResponse getFileDirSearchResult(String keyword, FileType fileType);

    /**
     * get all files and dirs from target dir
     * @param dirId dir Id
     * @param onlyDir if only need dirs under target
     * @return
     */
    FileDirResponse getFileDirUnderTarget(int dirId, boolean onlyDir);

    /**
     * move files to target dir
     * @param fileId
     * @param targetDirId
     */
    void moveFilesToTarget(List<Integer> fileId, int targetDirId);

    /**
     * recursively delete dir and its children and all the files under them
     * not real deleted, only update
     *
     * @param dirId dir Id
     */
    void deleteDirAndFilesRecursively(int dirId);
}
