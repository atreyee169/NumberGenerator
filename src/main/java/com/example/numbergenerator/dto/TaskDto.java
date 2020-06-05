package com.example.numbergenerator.dto;

import javax.validation.constraints.NotNull;

public class TaskDto {
	
	private Integer goal;
	@NotNull
	private Integer step;
	public Integer getStartNum() {
		return goal;
	}
	public void setGoal(Integer startNum) {
		this.goal = startNum;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	
	
}
