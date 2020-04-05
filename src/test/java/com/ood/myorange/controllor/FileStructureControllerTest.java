package com.ood.myorange.controllor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dto.DirsDto;
import com.ood.myorange.dto.FilesDto;
import com.ood.myorange.dto.request.AddDirRequest;
import com.ood.myorange.dto.request.MoveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Chen on 4/2/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback()
@WithUserDetails(value = "johnwick123@gmail.com", userDetailsServiceBeanName = "userDetailService")
class FileStructureControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    void getFileDirSearchResult() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/api/search").param("keyword", "file");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.files[0].name", is("file_")))
                .andExpect(jsonPath("$.result_data.files", hasSize(5)))
                .andDo(print());
        request = MockMvcRequestBuilders.get("/api/search").param("keyword", "").param("type", "errortype");
        mockMvc.perform(request).andExpect(status().is4xxClientError());
    }

    @Test
    void getAllFilesDirsUnderTargetDir() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/api/dirs/2");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.name", is("level_1a")))
                .andExpect(jsonPath("$.result_data.files", hasSize(1)))
                .andExpect(jsonPath("$.result_data.dirs", hasSize(2)))
                .andDo(print());
        request = MockMvcRequestBuilders.get("/api/dirs");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.pre_id", is(0)))
                .andExpect(jsonPath("$.result_data.name", is("/")))
                .andDo(print());
        request = MockMvcRequestBuilders.get("/api/dirs/4").param("only_dir", "true");
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.result_data.files", hasSize(0)))
                .andExpect(jsonPath("$.result_data.dirs", hasSize(1)))
                .andDo(print());
    }

    @Test
    void moveDirs() throws Exception {
        RequestBuilder request = null;
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setFromDirId(4);
        moveRequest.setToId(4);
        request = MockMvcRequestBuilders.post("/api/dirs").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(moveRequest));
        mockMvc.perform(request).andExpect(status().is4xxClientError())
                .andDo(print());
        // TODO cannot test move parent into child error due to travis CI only has mysql 5.7, which is not supporting recurrence in general syntax.
//        moveRequest.setFromDirId(5);
//        moveRequest.setToId(4);
//        request = MockMvcRequestBuilders.post("/api/dirs").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(moveRequest));
//        mockMvc.perform(request).andExpect(status().isOk())
//                .andDo(print());
//        request = MockMvcRequestBuilders.get("/api/dirs/4");
//        mockMvc.perform(request).andExpect(status().isOk())
//                .andExpect(jsonPath("$.result_data.dirs", hasSize(2)))
//                .andDo(print());
    }

    @Test
    void moveFiles() throws Exception {
        RequestBuilder request = null;
        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setFromFileId(Arrays.asList(4,5));
        moveRequest.setToId(3);
        request = MockMvcRequestBuilders.post("/api/files").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(moveRequest));
        mockMvc.perform(request).andExpect(status().isOk());
        request = MockMvcRequestBuilders.get("/api/dirs/3");
        mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.result_data.files", hasSize(3))).andDo(print());
    }

    @Test
    void changeDirName() throws Exception {
        RequestBuilder request = null;
        DirsDto dirsDto = new DirsDto();
        dirsDto.setName("/");
        request = MockMvcRequestBuilders.post("/api/dirs/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dirsDto));
        mockMvc.perform(request).andExpect(status().is4xxClientError()).andDo(print());
        dirsDto.setName("dd23");
        request = MockMvcRequestBuilders.post("/api/dirs/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dirsDto));
        mockMvc.perform(request).andExpect(status().is4xxClientError()).andDo(print());
        dirsDto.setName("dd23");
        request = MockMvcRequestBuilders.post("/api/dirs/2").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(dirsDto));
        mockMvc.perform(request).andExpect(status().isOk()).andDo(print());
        request = MockMvcRequestBuilders.get("/api/dirs/2").param("only_dir", "true");
        mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.result_data.name", is("dd23"))).andDo(print());
    }

    @Test
    void changeFileName() throws Exception {
        RequestBuilder request = null;
        FilesDto filesDto = new FilesDto();
        filesDto.setName("123.ww");
        request = MockMvcRequestBuilders.post("/api/files/3").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(filesDto));
        mockMvc.perform(request).andExpect(status().isOk()).andDo(print());
        request = MockMvcRequestBuilders.get("/api/dirs/3");
        mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.result_data.files[0].name", is("123.ww"))).andDo(print());
    }

    @Test
    void addDir() throws Exception {
        RequestBuilder request = null;
        AddDirRequest addDirRequest = new AddDirRequest();
        addDirRequest.setName("dd23");
        addDirRequest.setParentId(3);
        request = MockMvcRequestBuilders.put("/api/dirs").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(addDirRequest));
        mockMvc.perform(request).andExpect(status().isOk()).andDo(print());
        request = MockMvcRequestBuilders.get("/api/dirs/3");
        mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.result_data.dirs[0].name", is("dd23"))).andDo(print());
    }

    @Test
    void deleteFile() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.delete("/api/files/3");
        mockMvc.perform(request).andExpect(status().isOk());
        request = MockMvcRequestBuilders.get("/api/dirs/3");
        mockMvc.perform(request).andExpect(status().isOk()).andExpect(jsonPath("$.result_data.files", hasSize(0))).andDo(print());
    }
}