package com.ood.myorange.service.impl;

import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageConfiguration;
import com.ood.myorange.dao.StorageConfigDao;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.pojo.StorageConfig;
import com.ood.myorange.service.StorageConfigService;
import com.ood.myorange.util.ModelMapperUtil;
import com.oracle.tools.packager.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Repository
public class StorageConfigServiceImpl implements StorageConfigService {
    @Autowired
    StorageConfigDao storageConfigDao;

    @Override
    public List<StorageConfigDto> getAllConfigurations() {
        List<StorageConfig> configList = storageConfigDao.selectAll();

        List<StorageConfigDto> result = new ArrayList<>();
        for (StorageConfig c : configList) {
            StorageConfigDto configDto = ModelMapperUtil.mapping(c, StorageConfigDto.class);
            result.add(configDto);
        }
        return result;
    }

    @Override
    public StorageConfigDto getConfiguration(int configId) {
        StorageConfig result = storageConfigDao.selectByPrimaryKey( configId );
        return ModelMapperUtil.mapping(result, StorageConfigDto.class);
    }

    @Override
    public void addConfiguration(StorageConfigDto configDto) {
//        TODO
//
//        StorageConfig storageConfig = ModelMapperUtil.mapping( configDto, StorageConfig.class );
//        Log.info("Adding config");
//        Log.info(storageConfig.toString());
//        StorageConfiguration configuration;
//        switch (configDto.getType()) {
//            case "Azure":
//                configuration = new AzureConfiguration( ModelMapperUtil.mapping( configDto.getConfig(), AzureConfiguration.class ) );
//                break;
//            case "LOCAL":
//                break;
//            case "AWS_S3":
//                configuration = new S3Configuration( ModelMapperUtil.mapping( configDto.getConfig(), S3Configuration.class ) );
//                break;
//        }
//
//        storageConfigDao.insert( storageConfig );
    }

    @Override
    public void updateConfiguration(StorageConfigDto queryDto) {

    }

    @Override
    public void deleteConfiguration(int configId) {

    }
}
