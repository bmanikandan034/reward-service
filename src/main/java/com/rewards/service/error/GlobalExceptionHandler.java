package com.rewards.service.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.rewards.service.common.RewardResponseUtil;

import lombok.extern.slf4j.Slf4j;

/***
 * @author Manikandan B
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleNotFound(ResourceNotFoundException e) {

		log.error("Resource not found: {}", e.getMessage());

		return RewardResponseUtil.failure(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneric(Exception e) {

		log.error("Unexpected error occurred", e);
	return RewardResponseUtil.failure("Something went wrong");
	}
	
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<?> handleNoResource(NoResourceFoundException ex) {
	    return ResponseEntity.notFound().build();
	}

}
