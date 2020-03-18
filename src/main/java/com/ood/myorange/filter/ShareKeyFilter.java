package com.ood.myorange.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Chen on 3/18/20.
 */
@Component
@Slf4j
public class ShareKeyFilter extends GenericFilterBean {

    private RedisUtil redisUtil;

    private ObjectMapper objectMapper;

    public ShareKeyFilter(RedisUtil redisUtil, ObjectMapper objectMapper) {
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();
        String key = path.replaceAll("/s/", "");
        String redisShareKey = RedisUtil.getRedisShareKey(key);
        Map<Object, Object> properties = redisUtil.getHashEntries(redisShareKey);
        if (properties == null || properties.isEmpty()) {
            log.warn("cannot find shared file, key: [" + key + "]");
            BaseResponse failureDetail = BaseResponse.failure("cannot find shared file, key: [" + key + "]");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write((objectMapper.writeValueAsString(failureDetail)));
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
