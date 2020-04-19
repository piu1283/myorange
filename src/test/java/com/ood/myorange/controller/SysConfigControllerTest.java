package com.ood.myorange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.config.sys.MailConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Chen on 4/18/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback()
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "adminDetailService")
public class SysConfigControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( webApplicationContext ).apply( springSecurity()).build();
    }

    @Test
    public void getMailConfig() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/admin/mail");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.mail_address", is("myorange098@gmail.com")))
                .andExpect(jsonPath("$.result_data.sender", is("mo")))
                .andDo(print());
    }

    @Test
    public void updateMailConfig() throws Exception {
        MailConfig mailConfig = new MailConfig();
        mailConfig.setHost("123");
        mailConfig.setMailAddress("456@123.com");
        mailConfig.setSender("789");
        mailConfig.setPort(123);
        mailConfig.setPassword("123123");
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.post("/admin/mail").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(mailConfig));
        mockMvc.perform(request).andExpect(status().isOk())
                .andDo(print());
        request = MockMvcRequestBuilders.get("/admin/mail");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.mail_address", is("456@123.com")))
                .andExpect(jsonPath("$.result_data.sender", is("789")))
                .andDo(print());
    }

    @Test
    public void testSendingEmail() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.post("/admin/mail/test").param("addr", "111@123sjsj123.com");
        mockMvc.perform(request).andExpect(status().isOk())
                .andDo(print());
    }
}