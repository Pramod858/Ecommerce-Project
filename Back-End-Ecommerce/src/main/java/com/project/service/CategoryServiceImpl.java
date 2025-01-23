package com.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.CategoryDTO;
import com.project.dto.ProductDTO;
import com.project.entity.Category;
import com.project.entity.Product;
import com.project.exception.ResourceConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	
	private final CategoryRepository categoryRepository;
	private final ProductServiceImpl productServiceImpl;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository, ProductServiceImpl productServiceImpl) {
		this.categoryRepository = categoryRepository;
		this.productServiceImpl = productServiceImpl;
	}
	
	
	@Override
	public void addCategory(CategoryDTO categoryDTO) {
		Category existingCategory = categoryRepository.findByName(categoryDTO.getName());
		if (existingCategory != null) {
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

        Category categoryWithSameName = categoryRepository.findByName(categoryDTO.getName());
        if (categoryWithSameName != null && !categoryWithSameName.getId().equals(categoryId)) {
            throw new ResourceConflictException("Category", categoryDTO.getName());
        }

        existingCategory.setName(categoryDTO.getName());

        categoryRepository.save(existingCategory);
    }

	@Override
	public List<CategoryDTO> getCategories() {
		List<Category> categories = categoryRepository.findAll();
	    return categories.stream()
	                     .map(category -> new CategoryDTO(category.getId(), category.getName(),
	                         category.getProducts().stream()
	                             .map(product -> new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getImageUrl()))
	                             .collect(Collectors.toList())))
	                     .collect(Collectors.toList());
	}

	@Override
	public String deleteCategory(Long categoryId) {
		Category existingCategory = categoryRepository.findById(categoryId)
	            .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
		
		for (Product product: existingCategory.getProducts()) {
			productServiceImpl.deleteProduct(product.getId());
		}
		categoryRepository.delete(existingCategory);
		return "Category with CategoryId: " + categoryId + " and its associated products deleted successfully.";
	}

	@Override
	public CategoryDTO getCategory(Long categoryId) {
	    Category existingCategory = categoryRepository.findById(categoryId)
	        .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
	    
	    return new CategoryDTO(existingCategory.getId(), existingCategory.getName(), 
	                           existingCategory.getProducts().stream()
	                               .map(product -> new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getStock(), product.getImageUrl()))
	                               .collect(Collectors.toList()));
	}
}
