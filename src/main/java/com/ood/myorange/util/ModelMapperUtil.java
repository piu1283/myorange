package com.ood.myorange.util;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Chen on 2/25/20.
 */
@UtilityClass
public class ModelMapperUtil {
    private static final ModelMapper ModelMapper = new ModelMapper();

    public <T> T mapping(Object sourceObj, Class<T> targetClazz) {
       return ModelMapper.map(sourceObj, targetClazz);
    }
}
