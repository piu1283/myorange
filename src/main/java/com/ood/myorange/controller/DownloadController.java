package com.ood.myorange.controller;

import com.ood.myorange.dao.StorageConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    StorageConfigDao storageConfigDao;

    @GetMapping("/download/{fileName}")
    public void getPreSignedURL(@PathVariable("fileName") int fileName) {

    }
}