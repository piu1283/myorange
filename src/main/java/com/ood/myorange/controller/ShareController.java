package com.ood.myorange.controller;

import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.dto.ShareFileDto;
import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.dto.request.AddShareFileRequest;
import com.ood.myorange.dto.request.ShareFileRequest;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.pojo.UserFile;
import com.ood.myorange.service.FileService;
import com.ood.myorange.service.ShareFileService;
import com.ood.myorange.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Chen Chen on 3/19/20.
 */
@RestController
@Slf4j
public class ShareController {

    @Autowired
    ICurrentAccount currentAccount;

    @Autowired
    FileService fileService;

    @Autowired
    ShareFileService shareFileService;

    @PutMapping("/api/shares")
    public ShareFileDto addShareFile(@RequestBody AddShareFileRequest addShareFileRequest) {
        UserFile userFile = fileService.getUserFileById(addShareFileRequest.getFileId());
        validateFileBelongToThisUser(userFile);
        ShareFileDto result = shareFileService.addShareFile(addShareFileRequest.getFileId(), addShareFileRequest.getDeadline(), addShareFileRequest.getLimitDownloadTimes(), addShareFileRequest.getHasPassword());
        log.info("Add share file, params: [fileID:{}, deadline:{}, limitDownloadTimes:{}, hasPassword:{}]", addShareFileRequest.getFileId(), addShareFileRequest.getDeadline(), addShareFileRequest.getLimitDownloadTimes(), addShareFileRequest.getHasPassword());
        return result;
    }

    @GetMapping("/api/shares")
    public List<ShareFileDto> getAllShareFile() {
        return shareFileService.getAllShareFiles();
    }

    @GetMapping("/shares/{key}")
    public ShareFileDto getShareFileByShareKey(@PathVariable(value = "key") String shareKey, @RequestParam(value = "password", defaultValue = "") String password) {
        return shareFileService.getShareFileByShareKey(shareKey, password);
    }

    @PostMapping("/api/shares/{id}")
    public ShareFileDto updateShareFile(@PathVariable("id") int shareId, @RequestBody ShareFileRequest shareFileRequest) {
        return shareFileService.updateShareFile(shareId, shareFileRequest.getDeadline(), shareFileRequest.getLimitDownloadTimes(), shareFileRequest.getHasPassword());
    }

    @DeleteMapping("/api/shares/{id}")
    public void deleteShareFile(@PathVariable("id") int shareId) {
        shareFileService.deleteShareFile(shareId);
    }

    public void validateFileBelongToThisUser(UserFile userFile) {
        UserInfo userInfo = currentAccount.getUserInfo();
        if (userInfo.getId() != userFile.getUserId()) {
            throw new InvalidRequestException("This file is not belong to current user");
        }
    }

}
