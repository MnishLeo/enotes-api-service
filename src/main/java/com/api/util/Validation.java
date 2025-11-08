package com.api.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.api.dto.CategoryDto;
import com.api.exception.ValidationException;

@Component
public class Validation {

	public void categoryValidation(CategoryDto categoryDto) {
		Map<String, Object> error = new LinkedHashMap<>();

		if (ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("Category Json name should not be null or Empty");
		} else {
			if (ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name", "should not be null");

			} else {
				if (ObjectUtils.isEmpty(categoryDto.getName().length() > 10)) {
					error.put("name", "name is greater than 10 words ");
				}
			}
		
			 if (ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description", "description field is empty or null");

			}
			if (ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive", "isActive field is null or empty");

			} else {
				if (!categoryDto.getIsActive() == Boolean.TRUE || !categoryDto.getIsActive() == Boolean.FALSE)
					error.put("isActive", "inValid value isActive field");
			}
		}
		if(!error.isEmpty())
		{
			throw new ValidationException(error);
		}
	}
	
	
}
