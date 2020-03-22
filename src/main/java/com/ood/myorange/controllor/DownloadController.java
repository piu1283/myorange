package com.ood.myorange.controllor;

import com.ood.myorange.dao.StorageConfigDao;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.pojo.StorageConfig;
import com.ood.myorange.util.ModelMapperUtil;
import com.ood.myorange.util.StorageUtilFactory;
import com.ood.myorange.util.storageUtils.StorageUtil;
import com.oracle.tools.packager.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ood.myorange.dto.response.BaseResponse.success;

@RestController
@Slf4j
public class DownloadController {

    @Autowired
    StorageUtilFactory storageUtilFactory;

    @Autowired
    StorageConfigDao storageConfigDao;

    // This just an example
    // Not working
    @GetMapping("/d/{key}")
    public BaseResponse getPreSignedURL(@PathVariable("key") int key) {
        Log.info("Trying to get pro-signed url");
        StorageConfig result = storageConfigDao.getDataSourceById( key );
        StorageUtil util = storageUtilFactory.getInstance(result.getType());
        return success("Successfully get download link", util.getDownloadUrl( 1 ));
    }
}