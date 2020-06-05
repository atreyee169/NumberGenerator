package com.example.numbergenerator.utils;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class Util {

	public static String getOutputFilePath(long taskId)
	{
		return System.getProperty("java.io.tmpdir")+File.separator+taskId+"_output.txt";
	}
}
