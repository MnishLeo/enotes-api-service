package com.api.util;


import org.springframework.stereotype.Component;

import com.api.dto.TodoDto;
import com.api.dto.TodoDto.StatusDto;
import com.api.enums.TodoStatus;
import com.api.exception.ResourceNotFoundException;

@Component
public class Validation {

	public void todoValidation(TodoDto todoDto) throws ResourceNotFoundException {
		StatusDto reqStatus = todoDto.getStatus();
		Boolean statusFound = false;
		for (TodoStatus st : TodoStatus.values()) {
			if (st.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		if (!statusFound) {
			throw new ResourceNotFoundException("invalid status");
		}
	}

}
