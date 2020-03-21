package com.ood.myorange.controllor;

import com.ood.myorange.auth.IAuthenticationFacade;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.service.StorageConfigService;
import com.ood.myorange.service.impl.StorageConfigServiceImpl;
import com.ood.myorange.util.ModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Guancheng Lai on 3/19/20.
 */

@RestController
@Slf4j
public class StorageConfigController {
    @Autowired
    IAuthenticationFacade authenticationFacade; // authenticationFacade can be used to obtain the current login user

    @Autowired
    StorageConfigService storageConfigService;

    @PostMapping("/view-configuration-list")
    public BaseResponse viewStorageConfigList(
            @RequestBody Integer adminId
    ){
        authenticationFacade.getAuthentication().getPrincipal();
        storageConfigService.getAllConfigurations(adminId);
        return BaseResponse.success();
    }

    @PostMapping("/view-storage-config/{id}")
    public StorageConfigDto viewOneStorageConfig(
            @PathVariable("id") int configId
    ){
//        authenticationFacade.getAuthentication().getPrincipal();
        StorageConfigDto result = storageConfigService.getConfiguration( configId );
        return result;
    }

    @PostMapping("/add-storage-config")
    public BaseResponse addStorageConfig(
            @RequestBody StorageConfigDto storageConfigDto
    ){
//        authenticationFacade.getAuthentication().getPrincipal();
        storageConfigService.addConfiguration( storageConfigDto );
        return BaseResponse.success();
    }

    @PostMapping("/update-storage-config")
    public BaseResponse updateStorageConfig(
            @RequestBody StorageConfigDto storageConfigDto
    ){
//        authenticationFacade.getAuthentication().getPrincipal();
        storageConfigService.updateConfiguration( storageConfigDto );
        return BaseResponse.success();
    }

    @PostMapping("/delete-storage-config")
    public BaseResponse deleteStorageConfig(
            @RequestBody int configId
    ){
        storageConfigService.deleteConfiguration( configId );
//        authenticationFacade.getAuthentication().getPrincipal();
        return BaseResponse.success();
    }
}