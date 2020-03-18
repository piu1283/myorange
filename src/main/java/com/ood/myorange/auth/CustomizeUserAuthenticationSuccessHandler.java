package com.ood.myorange.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dto.UserDto;
import com.ood.myorange.dto.UserInfo;
import com.ood.myorange.dto.response.BaseResponse;
import com.ood.myorange.util.ModelMapperUtil;
import jdk.nashorn.internal.runtime.logging.Logger;
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
public class CustomizeUserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        UserInfo details = (UserInfo)authentication.getPrincipal();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        BaseResponse response = BaseResponse.success("Login success.", details);
        log.info("Login success.{}", details);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
