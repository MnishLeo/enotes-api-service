package com.api.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.api.handler.GenericResponse;

public class CommonUtil {

	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {
		GenericResponse response = GenericResponse.builder().httpStatus(status).status("succes").message("succes")
				.data(data).build();
		return response.create();
	}

	public static ResponseEntity<?> createBuildResponse(String message, HttpStatus status) {
		GenericResponse response = GenericResponse.builder().httpStatus(status).status("success").message(message)
				.build();
		return response.create();
	}

	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {
		GenericResponse response = GenericResponse.builder().httpStatus(status).status("failed").message("failed").data(data)
				.build();
		return response.create();
	}

	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {
		GenericResponse response = GenericResponse.builder().httpStatus(status).status("failed").message(message)
				.build();
		return response.create();

	}

	public static String getContentType(String originalFileName) {
		String extention = FilenameUtils.getExtension(originalFileName);
		
		switch (extention) {
		case "pdf" : 
			return "application/pdf";
		case "jpeg":
			return "image/jpeg";
		case "png" :
			return "image/png";
			default :
				return "application/ocet-stream";
		}
	}
}