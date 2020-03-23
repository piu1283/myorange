package com.ood.myorange.controllor;

import com.ood.myorange.auth.IAuthenticationFacade;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.service.StorageConfigService;
import com.ood.myorange.service.impl.StorageConfigServiceImpl;
import com.ood.myorange.util.ModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guancheng Lai on 3/19/20.
 */

@RestController
@Slf4j
@RequestMapping("/Admin")
public class StorageConfigController {

    @Autowired
    StorageConfigService storageConfigService;

    @GetMapping("/config/storage")
    public List<StorageConfigDto> viewAllStorageConfig(){
        return storageConfigService.getAllConfigurations();
    }

    @GetMapping("/config/storage/{configId}")
    public StorageConfigDto viewOneStorageConfig(
            @PathVariable("configId") int configId
    ){
        return storageConfigService.getConfiguration( configId );
    }

    @PostMapping("/config/delete/add/{configId}")
    public void addStorageConfig(
            @RequestBody StorageConfigDto storageConfigDto
    ){
        //TODO
    }

    @PostMapping("/config/storage/update/{configId}")
    public void updateStorageConfig(
            @PathVariable("configId") int configId,
            @RequestBody StorageConfigDto storageConfigDto
    ){
        //TODO
    }

    @PostMapping("/config/storage/delete/{configId}")
    public void deleteStorageConfig(
            @RequestBody int configId
    ){
        //TODO
    }
}