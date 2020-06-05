package com.example.numbergenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.numbergenerator.dto.TaskDto;
import com.example.numbergenerator.enums.TaskStatus;
import com.example.numbergenerator.exceptions.InvalidTaskException;
import com.example.numbergenerator.model.Task;
import com.example.numbergenerator.repo.TaskRepository;
import com.example.numbergenerator.service.TaskService;
import com.example.numbergenerator.utils.Util;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class NumbergeneratorServiceTests {

	@Mock
	TaskRepository taskRepository;
	@InjectMocks
	TaskService taskservice;
	TaskDto taskDto;
	Task expectedResultTask;
	List<String> expectedResultList;
	
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
		expectedResultTask.setFilePath(Util.getOutputFilePath(expectedResultTask.getTaskId()));
		
		expectedResultList=new ArrayList<String>();
		for(int i=taskDto.getStartNum();i>=0;i-=taskDto.getStep())
		{
			expectedResultList.add(i+"");
		}
	}
	
	@Test
	public void TestTaskExecution() throws Exception {
		Task mockedTask=new Task();
		mockedTask.setTaskId(expectedResultTask.getTaskId());
		
		Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(mockedTask);
		
		Task actualResultTask=taskservice.createTask(taskDto);
		
		assertNotNull(actualResultTask);
		assertEquals(expectedResultTask.getTaskId(),actualResultTask.getTaskId());
		assertEquals(expectedResultTask.getFilePath(),actualResultTask.getFilePath());
		assertEquals(TaskStatus.SUCCESS.name(),actualResultTask.getTaskStatus());
	}
	
	@Test(expected = NullPointerException.class)
	public void TestTaskExecutionFailure() throws Exception {
		Task mockedTask=new Task();
		mockedTask.setTaskId(expectedResultTask.getTaskId());
				
		taskDto.setGoal(null);
		taskservice.createTask(taskDto);		
	}
	
	@Test
	public void TestTaskStatus()
	{
		Task mockedTask=new Task();
		mockedTask.setTaskId(expectedResultTask.getTaskId());
		mockedTask.setTaskStatus(TaskStatus.SUCCESS.name());
		
		Mockito.when(taskRepository.findById(mockedTask.getTaskId())).thenReturn(Optional.of(mockedTask));
		
		String actualTaskStatus=taskservice.getTaskStatus(mockedTask.getTaskId());
		
		expectedResultTask.setTaskStatus(TaskStatus.SUCCESS.name());
		assertEquals(actualTaskStatus,expectedResultTask.getTaskStatus());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void TestTaskStatusNotFound()
	{
		Task mockedTask=new Task();
		mockedTask.setTaskId(expectedResultTask.getTaskId());
		mockedTask.setTaskStatus(TaskStatus.SUCCESS.name());
		
		Mockito.when(taskRepository.findById(mockedTask.getTaskId())).thenReturn(Optional.empty());
		
		taskservice.getTaskStatus(mockedTask.getTaskId());
	}
	
	@Test
	public void TestTaskResult() throws IOException
	{
		Task mockedTask=new Task();
		mockedTask.setTaskId(expectedResultTask.getTaskId());
		mockedTask.setFilePath(expectedResultTask.getFilePath());
		
		Mockito.when(taskRepository.findById(mockedTask.getTaskId())).thenReturn(Optional.of(mockedTask));

		List<String> actualResult=taskservice.getTaskResult(mockedTask.getTaskId());
		assertEquals(expectedResultList, actualResult);		
	}
	
	@Test(expected = InvalidTaskException.class)
	public void TestTaskResultNotFound() throws IOException
	{
		Task mockedTask=new Task();
		mockedTask.setTaskId(expectedResultTask.getTaskId());
		mockedTask.setFilePath(expectedResultTask.getFilePath());
		
		Mockito.when(taskRepository.findById(mockedTask.getTaskId())).thenReturn(Optional.empty());

		taskservice.getTaskResult(mockedTask.getTaskId());
	}

}
