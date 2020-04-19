package com.ood.myorange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dao.UserDao;
import com.ood.myorange.dto.UserProfile;
import com.ood.myorange.pojo.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
@WithUserDetails(value = "johnwick123@gmail.com", userDetailsServiceBeanName = "userDetailService")
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserDao userDao;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( webApplicationContext ).apply( springSecurity()).build();
    }

    @Test
    public void changeUserProfile() throws Exception {
        UserProfile uf = new UserProfile();
        uf.setGender("F");
        uf.setPassword("123456");
        uf.setFirstName("111");
        uf.setLastName("222");
        String requestJson = "{\"password\":\"111111\",\"birthday\":\"2017-09-15\",\"first_name\":\"111\",\"last_name\":\"222\",\"gender\":\"F\"}";
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.post("/api/profile").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson);
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.first_name", is("111")))
                .andExpect(jsonPath("$.result_data.download_access", is(true)))
                .andExpect(jsonPath("$.result_data.birthday", is("2017-09-15")))
                .andDo(print());
        User user = userDao.selectByPrimaryKey(new User(1));
        assertTrue(new BCryptPasswordEncoder().matches("111111", user.getPassword()));
    }

    @Test
    public void getUserProfile() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/api/profile");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.first_name", is("John")))
                .andExpect(jsonPath("$.result_data.download_access", is(true)))
                .andExpect(jsonPath("$.result_data.birthday", is("2019-09-30")))
                .andDo(print());
    }
}