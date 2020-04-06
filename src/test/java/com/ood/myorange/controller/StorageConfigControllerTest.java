package com.ood.myorange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.dto.StorageConfigDto;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Guancheng Lai on 04/05/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback()
@WithUserDetails(value = "admin", userDetailsServiceBeanName = "adminDetailService")
public class StorageConfigControllerTest {

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
    public void viewAllStorageConfig() throws Exception {
        testListOfConfig();
    }

    @Test
    public void viewOneStorageConfig() throws Exception {

        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/admin/config/storage/1");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(1) ))
                .andExpect( jsonPath("$.result_data.name", is("myS3") ))
                .andExpect( jsonPath("$.result_data.type", is("AWS") ))
                .andExpect( jsonPath("$.result_data.aws_access_key_id", is("keyid1") ))
                .andExpect( jsonPath("$.result_data.aws_secret_access_key", is("accesskey1") ))
                .andExpect( jsonPath("$.result_data.aws_region", is("us-east-2") ))
                .andExpect( jsonPath("$.result_data.aws_bucket_name", is("my-bucket-glai-01") ))
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/2");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(2) ))
                .andExpect( jsonPath("$.result_data.name", is("myAzure") ))
                .andExpect( jsonPath("$.result_data.type", is("Azure") ))
                .andExpect( jsonPath("$.result_data.azure_token", is("azureazure123456") ))
                .andDo( print() );
    }

    @Test
    public void addStorageConfig() throws Exception {
        testListOfConfig();

        RequestBuilder request = null;
        request = MockMvcRequestBuilders.delete("/admin/config/storage/1");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.delete("/admin/config/storage/2");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.result_data", hasSize( 0 ) ) )
                .andDo( print() );

        StorageConfigDto storageConfigRequest = new StorageConfigDto();
        storageConfigRequest.setName( "myS3_2.0" );
        storageConfigRequest.setType( "AWS" );
        storageConfigRequest.setAwsAccessKeyId( "keyid2" );
        storageConfigRequest.setAwsSecretAccessKey( "accesskey2" );
        storageConfigRequest.setAwsRegion( "us-east-3" );
        storageConfigRequest.setAwsBucketName( "my-bucket-glai-02" );

        request = MockMvcRequestBuilders.post("/admin/config/storage")
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( objectMapper.writeValueAsString( storageConfigRequest ) );
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/3");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(3) ))
                .andExpect( jsonPath("$.result_data.name", is("myS3_2.0") ))
                .andExpect( jsonPath("$.result_data.type", is("AWS") ))
                .andExpect( jsonPath("$.result_data.aws_access_key_id", is("keyid2") ))
                .andExpect( jsonPath("$.result_data.aws_secret_access_key", is("accesskey2") ))
                .andExpect( jsonPath("$.result_data.aws_region", is("us-east-3") ))
                .andExpect( jsonPath("$.result_data.aws_bucket_name", is("my-bucket-glai-02") ))
                .andDo( print() );
    }


    @Test
    public void editStorageConfig() throws Exception {
        testListOfConfig();

        StorageConfigDto storageConfigRequest = new StorageConfigDto();
        storageConfigRequest.setName( "myS3_2.0" );
        storageConfigRequest.setType( "AWS" );
        storageConfigRequest.setAwsAccessKeyId( "keyid2" );
        storageConfigRequest.setAwsSecretAccessKey( "accesskey2" );
        storageConfigRequest.setAwsRegion( "us-east-3" );
        storageConfigRequest.setAwsBucketName( "my-bucket-glai-02" );

        RequestBuilder request = null;
        request = MockMvcRequestBuilders.put("/admin/config/storage")
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( objectMapper.writeValueAsString( storageConfigRequest ) );
        mockMvc.perform( request )
                .andExpect( status().isOk() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/1");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(1) ))
                .andExpect( jsonPath("$.result_data.name", is("myS3_2.0") ))
                .andExpect( jsonPath("$.result_data.type", is("AWS") ))
                .andExpect( jsonPath("$.result_data.aws_access_key_id", is("keyid2") ))
                .andExpect( jsonPath("$.result_data.aws_secret_access_key", is("accesskey2") ))
                .andExpect( jsonPath("$.result_data.aws_region", is("us-east-3") ))
                .andExpect( jsonPath("$.result_data.aws_bucket_name", is("my-bucket-glai-02") ))
                .andDo( print() );

        storageConfigRequest = new StorageConfigDto();
        storageConfigRequest.setName( "myAzure2.0" );
        storageConfigRequest.setType( "Azure" );
        storageConfigRequest.setAzureToken( "azure_token_2.0" );

        request = MockMvcRequestBuilders.put("/admin/config/storage")
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( objectMapper.writeValueAsString( storageConfigRequest ) );
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/2");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(2) ))
                .andExpect( jsonPath("$.result_data.name", is("myAzure2.0") ))
                .andExpect( jsonPath("$.result_data.type", is("Azure") ))
                .andExpect( jsonPath("$.result_data.azure_token", is("azure_token_2.0") ))
                .andDo( print() );
    }

    @Test
    public void deleteStorageConfig() throws Exception {
        testListOfConfig();

        RequestBuilder request = null;
        request = MockMvcRequestBuilders.delete("/admin/config/storage/3");
        mockMvc.perform( request )
                .andExpect( status().is4xxClientError() )
                .andDo( print() );

        request = MockMvcRequestBuilders.delete("/admin/config/storage/2");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/2");
        mockMvc.perform( request )
                .andExpect( status().is4xxClientError() )
                .andDo( print() );

        request = MockMvcRequestBuilders.delete("/admin/config/storage/1");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/1");
        mockMvc.perform( request )
                .andExpect( status().is4xxClientError() )
                .andDo( print() );
    }

    void testListOfConfig() throws Exception {
        RequestBuilder request = null;
        request = MockMvcRequestBuilders.get("/admin/config/storage");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data[0].id", is(1) ))
                .andExpect( jsonPath("$.result_data[0].name", is("myS3") ))
                .andExpect( jsonPath("$.result_data[0].type", is("AWS") ))
                .andExpect( jsonPath("$.result_data[0].aws_access_key_id", is("keyid1") ))
                .andExpect( jsonPath("$.result_data[0].aws_secret_access_key", is("accesskey1") ))
                .andExpect( jsonPath("$.result_data[0].aws_region", is("us-east-2") ))
                .andExpect( jsonPath("$.result_data[0].aws_bucket_name", is("my-bucket-glai-01") ))
                .andExpect( jsonPath("$.result_data[1].id", is(2) ))
                .andExpect( jsonPath("$.result_data[1].name", is("myAzure") ))
                .andExpect( jsonPath("$.result_data[1].type", is("Azure") ))
                .andExpect( jsonPath("$.result_data[1].azure_token", is("azureazure123456") ))
                .andDo( print() );
    }
}