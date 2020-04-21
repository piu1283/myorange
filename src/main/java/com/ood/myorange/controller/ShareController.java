package com.ood.myorange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.dto.ShareFileDto;
import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.dto.request.ShareFileRequest;
import com.ood.myorange.exception.InvalidRequestException;
import com.ood.myorange.pojo.UserFile;
import com.ood.myorange.service.FileService;
import com.ood.myorange.service.ShareFileService;
import com.ood.myorange.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Chen Chen on 3/19/20.
 */
@RestController
@Slf4j
public class ShareController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ICurrentAccount currentAccount;

    @Autowired
    FileService fileService;

    @Autowired
    ShareFileService shareFileService;

    @PutMapping("/shares")
    public ShareFileDto addShareFile(@RequestParam(value="fileId") int fileID, @RequestBody ShareFileRequest  shareFileRequest){
        //check if file belong to this user
        UserInfo userInfo = currentAccount.getUserInfo();
        UserFile userFile=fileService.getUserFileById(fileID);
        if(userInfo.getId()!=userFile.getUserId()){
            throw new InvalidRequestException("This file is not belong to current user");
        }
        ShareFileDto result = shareFileService.addShareFile(fileID,shareFileRequest.getDeadline(),shareFileRequest.getLimitDownloadTimes(),shareFileRequest.getHasPassword());
        log.info("Add share file, params: [fileID:{}, deadline:{}, limitDownloadTimes:{}, hasPassword:{}]", fileID, shareFileRequest.getDeadline(), shareFileRequest.getLimitDownloadTimes(),shareFileRequest.getHasPassword());
        return result;
    }

    @GetMapping("/shares")
    public List<ShareFileDto> getAllShareFile(){
        return shareFileService.getAllShareFiles();
    }

    @GetMapping("/shares/{shareKey}")
    public ShareFileDto getShareFileByShareKey(@PathVariable(value="shareKey") String shareKey){
        return shareFileService.getShareFileByShareKey(shareKey);
    }

    @PostMapping("/shares/{shareId}")
    public ShareFileDto updateShareFile(@PathVariable("shareId") int shareId, @RequestBody ShareFileRequest shareFileRequest){
        return shareFileService.updateShareFile(shareId,shareFileRequest.getDeadline(),shareFileRequest.getLimitDownloadTimes(),shareFileRequest.getHasPassword());
    }

    @DeleteMapping("/shares/{shareId}")
    public void deleteShareFile(@PathVariable("shareId") int shareId){
        shareFileService.deleteShareFile(shareId);
    }



}
