package com.ood.myorange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ood.myorange.config.storage.AzureConfiguration;
import com.ood.myorange.config.storage.LocalConfiguration;
import com.ood.myorange.config.storage.S3Configuration;
import com.ood.myorange.config.storage.StorageType;
import com.ood.myorange.dao.StorageConfigDao;
import com.ood.myorange.dto.StorageConfigDto;
import com.ood.myorange.pojo.StorageConfig;
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

    @Autowired
    StorageConfigDao storageConfigDao;

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
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_access_key_id", is("keyid1") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_secret_access_key", is("accesskey1") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_region", is("us-east-2") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_bucket_name", is("my-bucket-glai-01") ))
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/2");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(2) ))
                .andExpect( jsonPath("$.result_data.name", is("myAzure") ))
                .andExpect( jsonPath("$.result_data.type", is("AZURE") ))
                .andExpect( jsonPath("$.result_data.azure_configuration.azure_token", is("azureazure123456") ))
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/3");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(3) ))
                .andExpect( jsonPath("$.result_data.name", is("myLocal") ))
                .andExpect( jsonPath("$.result_data.type", is("LOCAL") ))
                .andExpect( jsonPath("$.result_data.local_configuration.local_path", is("/home/myorange/storage/v1") ))
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
                .andExpect( jsonPath( "$.result_data", hasSize( 1 ) ) )
                .andDo( print() );

        StorageConfigDto storageConfigRequest = new StorageConfigDto();
        storageConfigRequest.setName( "myS3_2.0" );
        storageConfigRequest.setType( "AWS" );
        S3Configuration s3Configuration = new S3Configuration();
        s3Configuration.setAwsAccessKeyId( "keyid2" );
        s3Configuration.setAwsSecretAccessKey( "accesskey2" );
        s3Configuration.setAwsRegion( "us-east-3" );
        s3Configuration.setAwsBucketName( "my-bucket-glai-02"  );
        storageConfigRequest.setAwsConfiguration( s3Configuration );

        request = MockMvcRequestBuilders.post("/admin/config/storage")
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( objectMapper.writeValueAsString( storageConfigRequest ) );
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/4");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(4) ))
                .andExpect( jsonPath("$.result_data.name", is("myS3_2.0") ))
                .andExpect( jsonPath("$.result_data.type", is("AWS") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_access_key_id", is("keyid2") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_secret_access_key", is("accesskey2") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_region", is("us-east-3") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_bucket_name", is("my-bucket-glai-02") ))
                .andDo( print() );
    }


    @Test
    public void editStorageConfig() throws Exception {
        testListOfConfig();

        StorageConfigDto storageConfigRequest = new StorageConfigDto();
        storageConfigRequest.setName( "myS3_2.0" );
        storageConfigRequest.setType( "AWS" );
        S3Configuration s3Configuration = new S3Configuration();
        s3Configuration.setAwsAccessKeyId( "keyid2" );
        s3Configuration.setAwsSecretAccessKey( "accesskey2" );
        s3Configuration.setAwsRegion( "us-east-3" );
        s3Configuration.setAwsBucketName( "my-bucket-glai-02"  );
        storageConfigRequest.setAwsConfiguration( s3Configuration );

        RequestBuilder request = null;
        request = MockMvcRequestBuilders.put("/admin/config/storage")
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( objectMapper.writeValueAsString( storageConfigRequest ) );
        mockMvc.perform( request )
                .andExpect( status().isOk() );

        request = MockMvcRequestBuilders.get("/admin/config/storage");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.result_data", hasSize( 3 ) ) )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/1");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(1) ))
                .andExpect( jsonPath("$.result_data.name", is("myS3_2.0") ))
                .andExpect( jsonPath("$.result_data.type", is("AWS") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_access_key_id", is("keyid2") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_secret_access_key", is("accesskey2") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_region", is("us-east-3") ))
                .andExpect( jsonPath("$.result_data.aws_configuration.aws_bucket_name", is("my-bucket-glai-02") ))
                .andDo( print() );

        storageConfigRequest = new StorageConfigDto();
        storageConfigRequest.setName( "myAzure2.0" );
        storageConfigRequest.setType( "AZURE" );
        AzureConfiguration azureConfiguration = new AzureConfiguration();
        azureConfiguration.setAzureToken( "azure_token_2.0" );
        storageConfigRequest.setAzureConfiguration( azureConfiguration );

        request = MockMvcRequestBuilders.put("/admin/config/storage")
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( objectMapper.writeValueAsString( storageConfigRequest ) );
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.result_data", hasSize( 3 ) ) )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/2");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(2) ))
                .andExpect( jsonPath("$.result_data.name", is("myAzure2.0") ))
                .andExpect( jsonPath("$.result_data.type", is("AZURE") ))
                .andExpect( jsonPath("$.result_data.azure_configuration.azure_token", is("azure_token_2.0") ))
                .andDo( print() );

        storageConfigRequest = new StorageConfigDto();
        storageConfigRequest.setName( "myLocal2.0" );
        storageConfigRequest.setType( "LOCAL" );
        storageConfigRequest.setLocalConfiguration( new LocalConfiguration("/home/myorange/storage/v2") );
        String js = objectMapper.writeValueAsString( storageConfigRequest );

        request = MockMvcRequestBuilders.put("/admin/config/storage")
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .content( js );
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        StorageConfig storageConfig = storageConfigDao.SelectSourceByType( StorageType.LOCAL );


        request = MockMvcRequestBuilders.get("/admin/config/storage");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.result_data", hasSize( 3 ) ) )
                .andDo( print() );


        request = MockMvcRequestBuilders.get("/admin/config/storage/3");
        mockMvc.perform( request ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.result_data.id", is(3) ))
                .andExpect( jsonPath("$.result_data.name", is("myLocal2.0") ))
                .andExpect( jsonPath("$.result_data.type", is("LOCAL") ))
                .andExpect( jsonPath("$.result_data.local_configuration.local_path", is("/home/myorange/storage/v2") ))
                .andDo( print() );
    }

    @Test
    public void deleteStorageConfig() throws Exception {
        testListOfConfig();

        RequestBuilder request = null;
        request = MockMvcRequestBuilders.delete("/admin/config/storage/4");
        mockMvc.perform( request )
                .andExpect( status().is4xxClientError() )
                .andDo( print() );

        request = MockMvcRequestBuilders.delete("/admin/config/storage/3");
        mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andDo( print() );

        request = MockMvcRequestBuilders.get("/admin/config/storage/3");
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
                .andExpect( jsonPath("$.result_data[0].aws_configuration.aws_access_key_id", is("keyid1") ))
                .andExpect( jsonPath("$.result_data[0].aws_configuration.aws_secret_access_key", is("accesskey1") ))
                .andExpect( jsonPath("$.result_data[0].aws_configuration.aws_region", is("us-east-2") ))
                .andExpect( jsonPath("$.result_data[0].aws_configuration.aws_bucket_name", is("my-bucket-glai-01") ))
                .andExpect( jsonPath("$.result_data[1].id", is(2) ))
                .andExpect( jsonPath("$.result_data[1].name", is("myAzure") ))
                .andExpect( jsonPath("$.result_data[1].type", is("AZURE") ))
                .andExpect( jsonPath("$.result_data[1].azure_configuration.azure_token", is("azureazure123456") ))
                .andDo( print() );
    }
}