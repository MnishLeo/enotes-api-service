package com.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.api.entity.Category;
import com.api.repository.CategoryRepository;
import com.api.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	@Override
	public Boolean saveCategory(Category category) {
		category.setIsActive(true);
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());
		Category saveCategory = categoryRepository.save(category);
		if(ObjectUtils.isEmpty(saveCategory))
		{
			return false;
		}
		return true;
	}

	@Override
	public List<Category> getAllCategory() {
		List<Category> categories = categoryRepository.findAll();
		
		return categories;
	}

}
