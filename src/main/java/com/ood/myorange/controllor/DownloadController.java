package com.ood.myorange.controllor;

import com.ood.myorange.auth.IAuthenticationFacade;
import com.ood.myorange.dao.StorageConfigDao;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.pojo.StorageConfig;
import com.ood.myorange.util.ModelMapperUtil;
import com.ood.myorange.util.StorageUtilFactory;
import com.ood.myorange.util.storageUtils.StorageUtil;
import com.oracle.tools.packager.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ood.myorange.dto.response.BaseResponse.success;

@RestController
@Slf4j
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    IAuthenticationFacade authenticationFacade; // authenticationFacade can be used to obtain the current login user

    @Autowired
    StorageUtilFactory storageUtilFactory;

    @Autowired
    StorageConfigDao storageConfigDao;

    // This just an example
    // Not working
    @GetMapping("/download/{fileName}")
    public void getPreSignedURL(@PathVariable("fileName") int fileName) {
        authenticationFacade.getAuthentication().getPrincipal();
        StorageConfig result = storageConfigDao.getDataSourceById( fileName );
        StorageUtil util = storageUtilFactory.getInstance(result.getType());
        Log.info("Successfully get download link: " + util.getDownloadUrl( 1 ));
    }
}