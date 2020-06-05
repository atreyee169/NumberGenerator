package com.example.numbergenerator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.numbergenerator.controller.TaskController;
import com.example.numbergenerator.dto.TaskDto;
import com.example.numbergenerator.enums.TaskStatus;
import com.example.numbergenerator.model.Task;
import com.example.numbergenerator.service.TaskService;
import com.example.numbergenerator.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest({TaskController.class})
public class NumbergeneratorControllerTests {

	@Autowired
	MockMvc mockMvc;
	@MockBean
	TaskService taskService;
	@InjectMocks
	TaskController taskController;
	TaskDto taskDto;
	Task expectedResultTask;

	@Before
	public void init()
	{
		taskDto=new TaskDto();
		taskDto.setGoal(20);
		taskDto.setStep(2);
		
		expectedResultTask=new Task();
		expectedResultTask.setStartNum(taskDto.getStartNum());
		expectedResultTask.setStep(taskDto.getStep());
		expectedResultTask.setTaskId(1);
		expectedResultTask.setTaskStatus(TaskStatus.SUCCESS.name());
		expectedResultTask.setFilePath(Util.getOutputFilePath(expectedResultTask.getTaskId()));
	}
	@Test
	public void testTaskCreation() throws Exception
	{
		Mockito.when(taskService.createTask(Mockito.any(TaskDto.class))).thenReturn(expectedResultTask);
		mockMvc.perform(post("/api/generate").content(asJsonString(taskDto)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isAccepted()).andExpect(jsonPath("task").value(expectedResultTask.getTaskId()));
	}
	
	@Test
	public void testTaskStatus() throws Exception
	{
		Mockito.when(taskService.getTaskStatus(Mockito.anyLong())).thenReturn(expectedResultTask.getTaskStatus());
		mockMvc.perform(get("/api/tasks/{uuid}/status",expectedResultTask.getTaskId()).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk()).andExpect(jsonPath("result").value(expectedResultTask.getTaskStatus()));
	}
	
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
