package com.ood.myorange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dao.SysConfigDao;
import com.ood.myorange.exception.InternalServerError;
import com.ood.myorange.pojo.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chen on 4/14/20.
 */
@Component
@Slf4j
public class SysConfigService {

    private static ConcurrentHashMap<Class<?>, Object> configCenter = new ConcurrentHashMap<>();

    @Autowired
    SysConfigDao sysConfigDao;

    @Autowired
    ObjectMapper objectMapper;

    @PostConstruct
    public void init () throws ClassNotFoundException, JsonProcessingException {
        log.info("Start init sysconfig....");
        // using reflection to read config object
        List<SysConfig> sysConfigs = sysConfigDao.selectAll();
        for (SysConfig config : sysConfigs) {
            log.info("Found system config [{}]", config.getClassId());
            String className = config.getClassId();
            String content = config.getConfig();
            Class<?> configClass;
            try {
                configClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                log.error("Cannot found class [{}] when loading the system config.", className);
                log.error("Detail: ", e);
                throw e;
            }
            Object o = objectMapper.readValue(content, configClass);
            configCenter.put(configClass, o);
        }
        log.info("Finish loading system config.");
    }



    public Map<Class<?>, Object> getAllSysConfig() {
        return configCenter;
    }

    public <T> void updateSysConfig(T config) {
        SysConfig sysConfig = new SysConfig();
        try {
            sysConfig.setConfig(objectMapper.writeValueAsString(config));
        } catch (JsonProcessingException e) {
            throw new InternalServerError("update config fail, format error.");
        }
        sysConfig.setClassId(config.getClass().getName());
        sysConfigDao.updateByPrimaryKeySelective(sysConfig);
        configCenter.put(config.getClass(), config);
    }

    public <T> void addSysConfig(T config) {
        SysConfig sysConfig = new SysConfig();
        try {
            sysConfig.setConfig(objectMapper.writeValueAsString(config));
        } catch (JsonProcessingException e) {
            throw new InternalServerError("update config fail, format error.");
        }
        sysConfig.setClassId(config.getClass().getName());
        sysConfigDao.insertSelective(sysConfig);
        configCenter.put(config.getClass(), config);
    }

    @SuppressWarnings("unchecked")
    public <T> T getSysConfig(Class<T> configClass) {
        return (T)configCenter.get(configClass);
    }
}
