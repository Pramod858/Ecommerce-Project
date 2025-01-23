package com.project.service;

import java.util.List;

import com.project.dto.CategoryDTO;

public interface CategoryService {

	void addCategory(CategoryDTO categoryDTO);
	
	void updateCategory(CategoryDTO categoryDTO, Long categoryId);

	List<CategoryDTO> getCategories();

	String deleteCategory(Long categoryId);

	CategoryDTO getCategory(Long categoryId);

}
