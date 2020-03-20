package com.ood.myorange.controllor;

import com.ood.myorange.dto.UserDto;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.exception.ResourceNotFoundException;
import com.ood.myorange.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Chen on 3/19/20.
 */
@RestController
@Slf4j
public class ShareController {

    @Autowired
    RedisUtil redisUtil;

    // abc
    // This just an example, you should return file detail instead of userDto
    @PostMapping("/share/{key}")
    public UserDto getShareFile(@PathVariable("key") String key) {
        String redisShareKey = RedisUtil.getRedisShareKey(key);
        Map<Object, Object> properties = redisUtil.getHashEntries(redisShareKey);
        if (properties == null || properties.isEmpty()) {
            log.warn("Invalid share key: {}", key);
            throw new ResourceNotFoundException("Invalid share key: " + key);
        }
        //... add rest logic

        return new UserDto();
    }
}
