package com.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.api.util.CommonUtil;

@ControllerAdvice
public class GlobalExceptionHandling {

	@ExceptionHandler(exception = Exception.class)
	public ResponseEntity<?> globalException(Exception e) {
		//return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(exception = NullPointerException.class)
	public ResponseEntity<?> handleNullPointerException(Exception e) {
		//return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(exception = ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(Exception e) {
		//return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(exception = ExistDataException.class)
	public ResponseEntity<?> handleExistDataException(ExistDataException e) {
		//return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		return CommonUtil.createErrorResponseMessage(e.getMessage(),HttpStatus.CONFLICT);
	}

}
