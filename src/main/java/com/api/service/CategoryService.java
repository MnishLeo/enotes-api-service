package com.api.service;

import java.util.List;

import com.api.dto.CategoryDto;
import com.api.dto.CategoryResponse;
import com.api.entity.Category;

public interface CategoryService {
	
	public Boolean saveCategory(CategoryDto categorydto);
	
	public List<CategoryDto> getAllCategory();

	public List<CategoryResponse> getActiveCategory();

}
