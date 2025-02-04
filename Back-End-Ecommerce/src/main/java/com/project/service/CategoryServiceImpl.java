package com.project.service;

import com.project.dto.CategoryDTO;
import com.project.entity.Category;
import com.project.exception.ResourceConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.getName()) != null) {
            throw new ResourceConflictException("Category", categoryDTO.getName());
        }

        Category newCategory = new Category();
        newCategory.setName(categoryDTO.getName());
        categoryRepository.save(newCategory);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        if (categoryRepository.findByName(categoryDTO.getName()) != null && !existingCategory.getName().equals(categoryDTO.getName())) {
            throw new ResourceConflictException("Category", categoryDTO.getName());
        }

        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
    }

    @Override
    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll().stream()
            .map(category -> new CategoryDTO(category.getId(), category.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        return new CategoryDTO(existingCategory.getId(), existingCategory.getName());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

        categoryRepository.delete(existingCategory);
    }
}