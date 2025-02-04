package com.project.service;

import java.util.List;

import com.project.dto.CategoryDTO;

import jakarta.validation.Valid;

public interface CategoryService {

	void addCategory(@Valid CategoryDTO categoryDTO);

	void updateCategory(@Valid CategoryDTO categoryDTO, Long categoryId);

	List<CategoryDTO> getCategories();

	CategoryDTO getCategory(Long categoryId);

	void deleteCategory(Long categoryId);


}
