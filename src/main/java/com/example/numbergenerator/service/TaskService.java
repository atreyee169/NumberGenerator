package com.example.numbergenerator.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.numbergenerator.dto.TaskDto;
import com.example.numbergenerator.enums.TaskStatus;
import com.example.numbergenerator.exceptions.InvalidTaskException;
import com.example.numbergenerator.model.Task;
import com.example.numbergenerator.repo.TaskRepository;
import com.example.numbergenerator.utils.Util;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepository;
	/**
	 * Get Status of task
	 * @param taskId
	 * @return
	 */
	public String getTaskStatus(Long taskId)
	{
		return taskRepository.findById(taskId).get().getTaskStatus();
	}
	
	/**
	 * Submit a new task
	 * @param taskdto
	 * @return
	 * @throws IOException
	 */
	public Task createTask(TaskDto taskdto) throws IOException
	{
		Task task=new Task();
		task.setStartNum(taskdto.getStartNum());
		task.setStep(taskdto.getStep());
		task.setTaskStatus(TaskStatus.IN_PROGRESS.name());
		
		task=taskRepository.save(task);
		
		try {
		String filePath=Util.getOutputFilePath(task.getTaskId());
		task.setFilePath(filePath);
		task=taskRepository.save(task);
		
		executeTask(filePath,taskdto.getStartNum(),taskdto.getStep());
		
		task.setTaskStatus(TaskStatus.SUCCESS.name());
		task=taskRepository.save(task);
		}
		catch(Exception e)
		{
			task.setTaskStatus(TaskStatus.ERROR.name());
			task=taskRepository.save(task);
			throw e;
		}
		
		return task;
	}
	
	private void executeTask(String filePath,int startnum,int step) throws IOException
	{
		Path path=Paths.get(filePath);
		int num=startnum;
		try(BufferedWriter bufferedWriter=Files.newBufferedWriter(path, StandardOpenOption.CREATE)){
			while(num>=0)
			{
				bufferedWriter.write(num+"\n");
				num-=step;
			}
		}
		catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Get the result for a previously executed task
	 * @param taskId
	 * @return
	 * @throws IOException
	 */
	public List<String> getTaskResult(Long taskId) throws IOException
	{
		Optional<Task> taskOptionl=taskRepository.findById(taskId);
		
		taskOptionl.orElseThrow(()->new InvalidTaskException("Task Id not found!"));
		
		Task task=taskOptionl.get();
		Path path=Paths.get(Util.getOutputFilePath(task.getTaskId()));
		
		List<String> resultList=new ArrayList<String>();
		try(BufferedReader bufferedReader=Files.newBufferedReader(path)){
			String line;
			while((line=bufferedReader.readLine())!=null)
			{
				resultList.add(line);
			}
		}
		catch (IOException e) {
			throw e;
		}
		return resultList;
	}
}
