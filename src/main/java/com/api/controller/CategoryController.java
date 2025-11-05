package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.CategoryDto;
import com.api.dto.CategoryResponse;
import com.api.exception.ResourceNotFoundException;
import com.api.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categorydto) {
		Boolean saveCategory = categoryService.saveCategory(categorydto);
		if (saveCategory) {
			return new ResponseEntity<>("saved", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("notSaved", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/")
	public ResponseEntity<?> getAllCategory() {
		List<CategoryDto> allCategory = categoryService.getAllCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveCategory() {
		List<CategoryResponse> allCategory = categoryService.getActiveCategory();
		if (CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		} else {
			return new ResponseEntity<>(allCategory, HttpStatus.OK);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) throws Exception {
//	try{
//		CategoryDto categoryDto = categoryService.getCategoryById(id);
//		if (ObjectUtils.isEmpty(categoryDto)) {
//			return new ResponseEntity<>("Category not found with Id =" + id, HttpStatus.NOT_FOUND);
//		}
//		return new ResponseEntity<>(categoryDto, HttpStatus.OK);
//	}catch(ResourceNotFoundException e)
//	{
//	return new ResponseEntity<>(e.getMessage() , HttpStatus.NOT_FOUND);	
//	} catch(Exception e )
//	{
//		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); 
//	}

		CategoryDto categoryDto = categoryService.getCategoryById(id);
		if (ObjectUtils.isEmpty(categoryDto)) {
			return new ResponseEntity<>("Internal Server error = " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(categoryDto, HttpStatus.OK);
	}

	@GetMapping("/response/{id}")
	public ResponseEntity<?> getCategoryResponseById(@PathVariable Integer id)

	{
		CategoryResponse categoryRes = categoryService.getCategoryResponseId(id);
		if (ObjectUtils.isEmpty(categoryRes)) {
			return new ResponseEntity<>("Category not found = " + id, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(categoryRes, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletedCategoryById(@PathVariable Integer id)

	{
		Boolean categoryId = categoryService.deleteCategoryById(id);
		if (categoryId) {
			return new ResponseEntity<>("Category deleted Successfully = " + id, HttpStatus.OK);
		}
		return new ResponseEntity<>(categoryId, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
