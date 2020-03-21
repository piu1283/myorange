package com.ood.myorange.controllor;

import com.ood.myorange.util.StorageUtilFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DownloadController {

    @Autowired
    StorageUtilFactory storageUtilFactory;

    // This just an example
    // Not working
    @PostMapping("/d/{key}")
    public String getPreSignedURL(@PathVariable("key") int key) {
//        StorageConfig storageConfig = ModelMapperUtil(key, balabalaba);
//        StorageUtilImpl util = storageUtil.getInstance(storageConfig.getType());
//        return util.getUrl( 10 );
        return "";
    }
}