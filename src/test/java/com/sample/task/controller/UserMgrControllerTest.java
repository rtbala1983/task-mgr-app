package com.sample.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sample.task.config.AppConfig;
import com.sample.task.domain.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class UserMgrControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    @Test

    public void testUserCRUD() throws Exception{
        User user = new User();
        String firstNameSet=UUID.randomUUID().toString();
        user.setFirstName(firstNameSet);
        user.setLastName("LastName");

        mockMvc.perform(options("/userMgr/addUser")).andExpect(status().isOk());
        user.setEmployeeId("Employeeid");
        Gson gson = new Gson();
        gson.toJson(user);
        RequestBuilder requestBuilder= MockMvcRequestBuilders.post("/userMgr/addUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(user));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());
        MvcResult result=mockMvc.perform(get("/userMgr/listUsers/"))
                .andReturn();
        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        List<User> userList = new Gson().fromJson(result.getResponse().getContentAsString(), listType);
        //List<User> userList= gson.fromJson(result.getResponse().getContentAsString(),LinkedTreeMap);
        User userRes =userList.stream().filter(userVar -> userVar.getFirstName().equals(firstNameSet)).findFirst().get();
        assertEquals(firstNameSet,userRes.getFirstName());
        userRes.setFirstName(userRes.getFirstName()+"Update");
        mockMvc.perform(put("/userMgr/updateUser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(userRes)))
                .andExpect(status().is2xxSuccessful());
        MvcResult resultUpdate=mockMvc.perform(get("/userMgr/viewUser/"+userRes.getId()))
                .andReturn();
        User userUpdate=gson.fromJson(resultUpdate.getResponse().getContentAsString(),User.class);
        assertEquals(userRes.getFirstName(),userUpdate.getFirstName());
        mockMvc.perform(delete("/userMgr/deleteUser/"+userRes.getId()))
                .andExpect(status().is2xxSuccessful());

    }

}
