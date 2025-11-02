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
import com.api.repository.CategoryRepository;
import com.api.service.CategoryService;

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

		Category category = modelMapper.map(categorydto, Category.class);

//		Category category = new Category();
//		category.setName(categorydto.getName());
//		category.setDescription(categorydto.getDescription());
//		category.setIsActive(categorydto.get IsActive());
		category.setIsDeleted(false);
		category.setCreatedBy(1);
		category.setCreatedOn(new Date());
		Category saveCategory = categoryRepository.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		}
		return true;
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
	public CategoryDto getCategoryById(Integer id) {
		Optional<Category> findByCategoryId = categoryRepository.findByIdAndIsDeletedFalse(id);
		if (findByCategoryId.isPresent()) {
			Category cat = findByCategoryId.get();
			CategoryDto map = modelMapper.map(cat, CategoryDto.class);
			return map;
		}
		return null;
	}

	@Override
	public CategoryResponse getCategoryResponseId(Integer id) {
		Optional<Category> categoryResponsebyId = categoryRepository.findById(id);
		if(categoryResponsebyId.isEmpty())
		{
			return null;
		}
		Category category = categoryResponsebyId.get();
		CategoryResponse map = modelMapper.map(category, CategoryResponse.class);
		return map;
		
	}

	@Override
	public Boolean deleteCategoryById(Integer id) {
		
		Optional<Category> findById = categoryRepository.findById(id);
		if(findById.isPresent())
		{
		Category cat = findById.get();
		cat.setIsDeleted(true);
		categoryRepository.save(cat);
	//	categoryRepository.delete(cat);
		return true;
		}
		else
		{
			//return "no Category found with Id = " + id ;
		}
		return false;
	}

}
