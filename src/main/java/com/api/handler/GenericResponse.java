package com.api.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder  
public class GenericResponse {

	private HttpStatus httpStatus;
	private String status;
	private String message;
	private Object data;
	
	public ResponseEntity<?> create() {
		Map<String,Object> map = new LinkedHashMap<>();
		map.put("status", status);
		map.put("message", message);
		if(!ObjectUtils.isEmpty(data))
		{
			map.put("data", data);
		}
		return new ResponseEntity<>(map,httpStatus);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public GenericResponse(HttpStatus httpStatus, String status, String message, Object data) {
		super();
		this.httpStatus = httpStatus;
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	
}
