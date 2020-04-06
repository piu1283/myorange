package com.ood.myorange.controllor;

import com.ood.myorange.auth.IAuthenticationFacade;
import com.ood.myorange.dao.StorageConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    IAuthenticationFacade authenticationFacade; // authenticationFacade can be used to obtain the current login user

    @Autowired
    StorageConfigDao storageConfigDao;

    @GetMapping("/download/{fileName}")
    public void getPreSignedURL(@PathVariable("fileName") int fileName) {

    }
}