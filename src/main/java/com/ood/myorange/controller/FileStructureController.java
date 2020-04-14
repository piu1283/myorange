package com.ood.myorange.controller;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.constant.enumeration.FileType;
import com.ood.myorange.dto.DirsDto;
import com.ood.myorange.dto.FilesDto;
import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.dto.request.AddDirRequest;
import com.ood.myorange.dto.request.MoveRequest;
import com.ood.myorange.dto.response.FileDirResponse;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.service.DirService;
import com.ood.myorange.service.FileDirService;
import com.ood.myorange.service.FileService;
import com.ood.myorange.util.NamingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Created by Chen on 3/29/20.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class FileStructureController {

    @Autowired
    FileDirService fileDirService;

    @Autowired
    ICurrentAccount currentAccount;

    @Autowired
    FileService fileService;

    @Autowired
    DirService dirService;

    @GetMapping(value = "/search")
    public FileDirResponse getFileDirSearchResult(@RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "type", required = false) String type) {
        if (StringUtils.isBlank(keyword) && StringUtils.isBlank(type)) {
            throw new InvalidRequestException("Invalid request params: both params cannot be empty at same time.", Arrays.asList("type", "keyword"));
        }
        FileType fileType = null;
        if (!StringUtils.isBlank(type)) {
            fileType = FileType.getFileType(type);
        }
        FileDirResponse result = fileDirService.getFileDirSearchResult(keyword, fileType);
        log.info("Search for file and dir, params: [keyword:{}, type:{}]", keyword, type);
        log.info("Result: {}", result);
        return result;
    }

    @GetMapping("/dirs/{id}")
    public FileDirResponse getAllFilesDirsUnderTargetDir(@PathVariable(name = "id") int dirId, @RequestParam(value = "only_dir", defaultValue = "false") boolean onlyDir) {
        return fileDirService.getFileDirUnderTarget(dirId, onlyDir);
    }

    @GetMapping(value = {"/dirs"})
    public FileDirResponse getAllFilesDirsUnderTargetDir(@RequestParam(value = "only_dir", defaultValue = "false") boolean onlyDir) {
        UserInfo userInfo = currentAccount.getUserInfo();
        DirsDto rootDir = dirService.getRootDir(userInfo.getId());
        return fileDirService.getFileDirUnderTarget(rootDir.getId(), onlyDir);
    }

    @PostMapping("/dirs")
    public void moveDirs(@RequestBody MoveRequest moveRequest) {
        checkMoveRequest(moveRequest);
        if (moveRequest.getFromDirId() == null || moveRequest.getFromDirId() == 0) {
            throw new InvalidRequestException("Empty source directories id.");
        }
        if (moveRequest.getFromDirId().equals(moveRequest.getToId())) {
            throw new InvalidRequestException("Cannot move dir into itself. dir_id: " + moveRequest.getToId());
        }
        dirService.moveDirToTargetDir(moveRequest.getFromDirId(), moveRequest.getToId());
    }

    @PostMapping("/files")
    public void moveFiles(@RequestBody MoveRequest moveRequest) {
        checkMoveRequest(moveRequest);
        if (CollectionUtils.isEmpty(moveRequest.getFromFileId())) {
            throw new InvalidRequestException("Empty source directories id.");
        }
        fileDirService.moveFilesToTarget(moveRequest.getFromFileId(), moveRequest.getToId());
    }

    @PostMapping("/dirs/{id}")
    public void changeDirName(@PathVariable(name = "id") Integer dirId, @RequestBody DirsDto dirsDto) {
        if (!NamingUtil.validDirName(dirsDto.getName())) {
            throw new InvalidRequestException("invalid name of dir: " + dirsDto.getName() + ".");
        }
        dirService.updateDirName(dirsDto.getName(), dirId);
    }

    @PostMapping("/files/{id}")
    public void changeFileName(@PathVariable(name = "id") Integer fileId, @RequestBody FilesDto filesDto) {
        if (!NamingUtil.validFileName(filesDto.getName())) {
            throw new InvalidRequestException("invalid name of file: " + filesDto.getName() + ".");
        }
        fileService.updateFileName(filesDto.getName(), fileId);
    }

    @PutMapping("/dirs")
    public void addDir(@RequestBody AddDirRequest addDirRequest) {
        if (!NamingUtil.validDirName(addDirRequest.getName())) {
            throw new InvalidRequestException("invalid name of dir: " + addDirRequest.getName() + ".");
        }
        dirService.addDir(addDirRequest);
    }

    @DeleteMapping("/dirs/{id}")
    public void deleteDir(@PathVariable(name = "id") int dirId) {
        fileDirService.deleteDirAndFilesRecursively(dirId);
    }

    @DeleteMapping("/files/{id}")
    public void deleteFile(@PathVariable(name = "id") int fileId) {
        fileService.deleteFile(fileId);
    }

    private void checkMoveRequest(MoveRequest moveRequest) {
        if (moveRequest.getToId() == null || moveRequest.getToId() < 1) {
            throw new InvalidRequestException("Empty target directory id.");
        }
    }

}
