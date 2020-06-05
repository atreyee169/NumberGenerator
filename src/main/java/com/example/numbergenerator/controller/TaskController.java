package com.example.numbergenerator.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.numbergenerator.dto.TaskDto;
import com.example.numbergenerator.model.Task;
import com.example.numbergenerator.service.TaskService;

@RestController
@Validated
public class TaskController {

	@Autowired
	TaskService taskService;
	
	/**
	 * Mapping generation of numbers
	 * @param taskDto
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/api/generate")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public Map<String, Long> executeTask(@RequestBody TaskDto taskDto) throws IOException
	{
		Map<String, Long> map=new HashMap<String, Long>();
		map.put("task", taskService.createTask(taskDto).getTaskId());
		return map;
	}
	
	/**
	 * Get status for an existing task
	 * @param uuid
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/api/tasks/{uuid}/status")
	@ResponseStatus(HttpStatus.OK)
	public Map<String, String> getTaskStatus(@PathVariable Long uuid)
	{
		Map<String, String> map=new HashMap<String, String>();
		map.put("result", taskService.getTaskStatus(uuid));
		return map;
	}
	
	/**
	 * Read from existing task file
	 * @param uuid
	 * @param action
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/api/tasks/{uuid}")
	public Map<String, String> getTaskResult(@PathVariable Long uuid,@RequestParam @Pattern(regexp ="get_numlist" ) String action) throws IOException
	{
		Map<String, String> map=new HashMap<String, String>();
		String resultstr=taskService.getTaskResult(uuid).stream().collect(Collectors.joining(","));
		System.out.println(resultstr);
		map.put("result", resultstr);
		return map;
	}
}
