package com.sample.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.sample.task.config.AppConfig;
import com.sample.task.domain.Task;
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
public class TasktMgrControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype());


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    @Test

    public void testTaskCRUD() throws Exception{
        Task task = new Task();
        String tasktSet= UUID.randomUUID().toString();
        task.setTask(tasktSet);
        task.setStartDate(LocalDateTime.now());
        task.setEndDate(LocalDateTime.now());
        task.setPriority("15");



        RequestBuilder requestBuilder= MockMvcRequestBuilders.post("/taskMgr/addTask")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(task));

      /*  mockMvc.perform(get("/taskMgr/listTasks/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());*/
        mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful());
        MvcResult result=mockMvc.perform(get("/taskMgr/listTasks/")
                .param("task","")
                .param("parentTask","")
                .param("startPriority","")
                .param("endPriority","")
                .param("startDate","")
                .param("endDate","")
        )
                .andReturn();
        Type listType = new TypeToken<ArrayList<Task>>(){}.getType();

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                LocalDateTime.parse(json.getAsJsonPrimitive().getAsString())).create();

        List<Task> taskList = gson.fromJson(result.getResponse().getContentAsString(), listType);
        //List<Task> taskList= gson.fromJson(result.getResponse().getContentAsString(),LinkedTreeMap);
        Task taskRes =taskList.stream().filter(taskVar -> taskVar.getTask().equals(tasktSet)).findFirst().get();
        assertEquals(tasktSet,taskRes.getTask());
        taskRes.setPriority("20");
        mockMvc.perform(put("/taskMgr/updateTask")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(taskRes)))
                .andExpect(status().is2xxSuccessful());
        MvcResult resultUpdate=mockMvc.perform(get("/taskMgr/viewTask/"+taskRes.getTask()))
                .andReturn();
        Task taskUpdate=gson.fromJson(resultUpdate.getResponse().getContentAsString(),Task.class);
        assertEquals("20",taskUpdate.getPriority());
        mockMvc.perform(delete("/taskMgr/deleteTask/"+taskRes.getTask()))
                .andExpect(status().is2xxSuccessful());

    }

}
