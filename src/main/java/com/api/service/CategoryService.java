package com.api.service;

import java.util.List;

import com.api.dto.CategoryDto;
import com.api.dto.CategoryResponse;


public interface CategoryService {

	public Boolean saveCategory(CategoryDto categorydto);

	public List<CategoryDto> getAllCategory();

	public List<CategoryResponse> getActiveCategory();

	public CategoryDto getCategoryById(Integer id) throws Exception;
	
	public CategoryResponse getCategoryResponseId(Integer id);

	public Boolean deleteCategoryById(Integer id);

}
