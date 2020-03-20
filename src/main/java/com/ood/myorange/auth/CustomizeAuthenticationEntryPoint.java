package com.ood.myorange.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Chen on 3/17/20.
 */
@Component
@Slf4j
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.warn("Authentication fail due to client bad request: ",e);
        BaseResponse response = BaseResponse.failure("Unauthorized operation.");
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
