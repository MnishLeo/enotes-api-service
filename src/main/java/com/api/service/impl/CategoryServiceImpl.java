package com.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.api.EnotesApiSericeApplication;
import com.api.dto.CategoryDto;
import com.api.dto.CategoryResponse;
import com.api.entity.Category;
import com.api.exception.ExistDataException;
import com.api.exception.ResourceNotFoundException;
import com.api.repository.CategoryRepository;
import com.api.service.CategoryService;

import jakarta.validation.Validation;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final EnotesApiSericeApplication enotesApiSericeApplication;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	CategoryServiceImpl(EnotesApiSericeApplication enotesApiSericeApplication) {
		this.enotesApiSericeApplication = enotesApiSericeApplication;
	}

	@Override
	public Boolean saveCategory(CategoryDto categorydto) {

		Boolean exist = categoryRepository.existsByName(categorydto.getName().trim());
		if(exist)
		{
			throw new ExistDataException ("Category Already Present");
		}
		
		
		
		
		Category category = modelMapper.map(categorydto, Category.class);

		if (ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false);
			// category.setCreatedBy(1);
			category.setCreatedOn(new Date());
		} else {
			updateCategory(category);
		}

//		Category category = new Category();
//		category.setName(categorydto.getName());
//		category.setDescription(categorydto.getDescription());
//		category.setIsActive(categorydto.get IsActive());

		Category saveCategory = categoryRepository.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		}
		return true;
	}

	private void updateCategory(Category category) {
		Optional<Category> findById = categoryRepository.findById(category.getId());
		if (findById.isPresent()) {
			Category existedCategory = findById.get();
			category.setCreatedBy(existedCategory.getCreatedBy());
			category.setCreatedOn(existedCategory.getCreatedOn());
			category.setIsDeleted(existedCategory.getIsDeleted());
			// category.setUpdatedBy(1);
			category.setUpdatedOn(new Date());
		}
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepository.findByIsDeletedFalse();

		List<CategoryDto> categoryDtolist = categories.stream().map(cat -> modelMapper.map(cat, CategoryDto.class))
				.toList();
		return categoryDtolist;
	}

	@Override
	public List<CategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
		List<CategoryResponse> categoryActivelist = categories.stream()
				.map(cat -> modelMapper.map(cat, CategoryResponse.class)).toList();
		return categoryActivelist;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) throws Exception {
		Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not Found with id = " + id));
		if (!ObjectUtils.isEmpty(category)) {
			if (category.getName() == null) {
				throw new IllegalArgumentException("name is null");
			}
//			Category cat = findByCategoryId.get();
			CategoryDto map = modelMapper.map(category, CategoryDto.class);
			return map;
		}
		return null;
	}

	@Override
	public CategoryResponse getCategoryResponseId(Integer id) {
		Optional<Category> categoryResponsebyId = categoryRepository.findById(id);
		if (categoryResponsebyId.isEmpty()) {
			return null;
		}
		Category category = categoryResponsebyId.get();
		CategoryResponse map = modelMapper.map(category, CategoryResponse.class);
		return map;

	}

	@Override
	public Boolean deleteCategoryById(Integer id) {

		Optional<Category> findById = categoryRepository.findById(id);
		if (findById.isPresent()) {
			Category cat = findById.get();
			cat.setIsDeleted(true);
			categoryRepository.save(cat);
			// categoryRepository.delete(cat);
			return true;
		} else {
			// return "no Category found with Id = " + id ;
		}
		return false;
	}

}
