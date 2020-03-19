package com.ood.myorange.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dto.AdminInfo;
import com.ood.myorange.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
public class CustomizeAdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        AdminInfo adminInfo = (AdminInfo) authentication.getPrincipal();
        BaseResponse response = BaseResponse.success("Login success.", adminInfo);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        log.info("Login success. {}", adminInfo);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
