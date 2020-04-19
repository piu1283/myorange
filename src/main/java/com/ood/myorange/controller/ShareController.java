package com.ood.myorange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ood.myorange.auth.ICurrentAccount;
import com.ood.myorange.dto.ShareFileDto;
import com.ood.myorange.service.FileService;
import com.ood.myorange.service.ShareFileService;
import com.ood.myorange.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Chen on 3/19/20.
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

//    @PutMapping("/shares")
//    public ShareFileDto addShareFile(@RequestParam(value="file_id") int fileID, @RequestParam(value="deadline") Timestamp deadline,
//                                     @RequestParam(value="limitDownloadTimes") int limitDownloadTimes, @RequestParam(value="hasPassword") boolean hasPassword){
//        UserInfo userInfo = currentAccount.getUserInfo();
//
//        ShareFileDto result = shareFileService.addShareFile(fileID,deadline,limitDownloadTimes,hasPassword);
//        log.info("Add share file, params: [fileID:{}, deadline:{}, limitDownloadTimes:{}, hasPassword:{}]", fileID, deadline, limitDownloadTimes, hasPassword);
//        log.info("Result: {}", result);
//        return result;
//    }

    @GetMapping("/shares")
    public List<ShareFileDto> getAllShareFile() throws JsonProcessingException {
        return shareFileService.getAllShareFiles();
    }

    @GetMapping("/shares/{shareKey}")
    public ShareFileDto getShareFileByShareKey(@PathVariable(value="shareKey") String shareKey) throws JsonProcessingException {
        return shareFileService.getShareFileByShareKey(shareKey);
    }

    @PostMapping("/shares/{shareId}")
    public ShareFileDto updateShareFile(@PathVariable("shareId") int shareId,
                                        @RequestParam(value="deadline") Timestamp deadline,
                                        @RequestParam(value="limitDownloadTimes") int limitDownloadTimes,
                                        @RequestParam(value="hasPassword") boolean hasPassword) throws JsonProcessingException{
        return shareFileService.updateShareFile(shareId,deadline,limitDownloadTimes,hasPassword);
    }

    @DeleteMapping("/shares/{shareId}")
    public void deleteShareFile(@PathVariable("shareId") int shareId) throws JsonProcessingException {
        shareFileService.deleteShareFile(shareId);
    }

//    // This just an example, you should return file detail instead of userDto
//    @PostMapping("/share/{key}")
//    public UserDto getShareFile(@PathVariable("key") String key) {
//        String redisShareKey = RedisUtil.getRedisShareKey(key);
//        Map<Object, Object> properties = redisUtil.getHashEntries(redisShareKey);
//        if (properties == null || properties.isEmpty()) {
//            log.warn("Invalid share key: {}", key);
//            throw new ResourceNotFoundException("Invalid share key: " + key);
//        }
//        //... add rest logic
//
//        return new UserDto();
//    }
}
