package com.example.numbergenerator.exceptions;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler({NoSuchElementException.class,InvalidTaskException.class})
	public ResponseEntity<String> handle(Exception ex) 
	{
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException(IOException ioex)
	{
		return new ResponseEntity<String>("File not found:"+ioex.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
