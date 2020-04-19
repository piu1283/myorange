package com.ood.myorange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.auth.CurrentAccount;
import com.ood.myorange.dao.UserDao;
import com.ood.myorange.dao.UserDirDao;
import com.ood.myorange.dto.AdminUserDto;
import com.ood.myorange.pojo.Admin;
import com.ood.myorange.pojo.User;
import com.ood.myorange.pojo.UserDir;
import com.ood.myorange.service.AdminService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
public class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDirDao dirDao;

    @Autowired
    private AdminService adminService;

    @Autowired
    CurrentAccount currentAccount;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void getAllUserData() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/admin/users");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.users", hasSize(2)))
                .andExpect(jsonPath("$.result_data.users[0].first_name", is("John")))
                .andDo(print());
    }

    @Test
    public void testGetUserData() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/admin/users/1");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.first_name", is("John")))
                .andExpect(jsonPath("$.result_data.download_access", is(true)))
                .andDo(print());
    }

    @Test
    public void changeUserPermissionAndMemory() throws Exception {
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setDownloadAccess(false);
        adminUserDto.setUploadAccess(false);
        adminUserDto.setBlockedStatus(true);
        adminUserDto.setTotalStorage(10240L);
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.post("/admin/users/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(adminUserDto));
        mockMvc.perform(request).andExpect(status().isOk())
                .andDo(print());
        request = MockMvcRequestBuilders.get("/admin/users/1");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.total_storage", is(10240)))
                .andExpect(jsonPath("$.result_data.upload_access", is(false)))
                .andExpect(jsonPath("$.result_data.download_access", is(false)))
                .andExpect(jsonPath("$.result_data.blocked_status", is(true)))
                .andDo(print());

    }

    @Test
    public void deleteUser() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.delete("/admin/users/1");
        mockMvc.perform(request).andExpect(status().isOk())
                .andDo(print());
        User user = userDao.selectByPrimaryKey(new User(1));
        assertEquals(true, user.getDeleted());
    }

    @Test
    public void changeAdminPassword() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.post("/admin/password").contentType(MediaType.APPLICATION_JSON_VALUE).content("{\"password\":\"111111\"}");
        mockMvc.perform(request).andExpect(status().isOk())
                .andDo(print());
        Admin admin = adminService.getAdminByName("admin");
        boolean matches = new BCryptPasswordEncoder().matches("111111", admin.getPassword());
        assertTrue(matches);
    }

    @Test
    public void addUser() throws Exception {
        AdminUserDto adminUserDto = new AdminUserDto();
        adminUserDto.setFirstName("ffff");
        adminUserDto.setLastName("ddddd");
        adminUserDto.setTotalStorage(10240L);
        adminUserDto.setSourceId(1);
        adminUserDto.setEmail("123@gmail.com");
        adminUserDto.setGender("F");
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.put("/admin/users").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(adminUserDto));
        mockMvc.perform(request).andExpect(status().isOk())
                .andDo(print());
        request = MockMvcRequestBuilders.get("/admin/users/3");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.total_storage", is(10240)))
                .andExpect(jsonPath("$.result_data.upload_access", is(true)))
                .andExpect(jsonPath("$.result_data.download_access", is(true)))
                .andExpect(jsonPath("$.result_data.blocked_status", is(false)))
                .andExpect(jsonPath("$.result_data.first_name", is("ffff")))
                .andExpect(jsonPath("$.result_data.source_id", is(1)))
                .andExpect(jsonPath("$.result_data.gender", is("F")))
                .andExpect(jsonPath("$.result_data.email", is("123@gmail.com")))
                .andDo(print());
        UserDir rootDir = dirDao.getRootDir(3);
        assertEquals(0, rootDir.getParentId().intValue());
    }
}