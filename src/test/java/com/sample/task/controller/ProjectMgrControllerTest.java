package com.sample.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.sample.task.config.AppConfig;
import com.sample.task.domain.Project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
public class ProjectMgrControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    @Test

    public void testProjectCRUD() throws Exception{
        Project project = new Project();
        String projectSet= UUID.randomUUID().toString();
        project.setProject(projectSet);
        project.setStartDate(LocalDateTime.now());
        project.setEndDate(LocalDateTime.now());
        project.setPriority("15");

        mockMvc.perform(options("/projectMgr/addProject")).andExpect(status().isOk());

        RequestBuilder requestBuilder= MockMvcRequestBuilders.post("/projectMgr/addProject")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(project));

      /*  mockMvc.perform(get("/projectMgr/listProjects/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());*/
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());
        MvcResult result=mockMvc.perform(get("/projectMgr/listProjects/"))
                .andReturn();
        Type listType = new TypeToken<ArrayList<Project>>(){}.getType();

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())).create();

        List<Project> projectList = gson.fromJson(result.getResponse().getContentAsString(), listType);
        //List<Project> projectList= gson.fromJson(result.getResponse().getContentAsString(),LinkedTreeMap);
        Project projectRes =projectList.stream().filter(projectVar -> projectVar.getProject().equals(projectSet)).findFirst().get();
        assertEquals(projectSet,projectRes.getProject());
        projectRes.setProject(projectRes.getProject()+"Update");
        mockMvc.perform(put("/projectMgr/updateProject")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(projectRes)))
                .andExpect(status().is2xxSuccessful());
        MvcResult resultUpdate=mockMvc.perform(get("/projectMgr/viewProject/"+projectRes.getId()))
                .andReturn();
        Project projectUpdate=gson.fromJson(resultUpdate.getResponse().getContentAsString(),Project.class);
        assertEquals(projectRes.getProject(),projectUpdate.getProject());
        mockMvc.perform(delete("/projectMgr/deleteProject/"+projectRes.getId()))
                .andExpect(status().is2xxSuccessful());

    }

}
