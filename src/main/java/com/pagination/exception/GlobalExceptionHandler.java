package com.pagination.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<Map<String,String>> handleEmployeeNotFound(EmployeeNotFoundException exception){
		
		Map<String,String>  response = new HashMap<>();
		response.put("error", exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);	
	}
	
	public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException exception){
		Map<String, String> errors = new HashMap<>();
		
		exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		
		return ResponseEntity.badRequest().body(errors);
	}

}
