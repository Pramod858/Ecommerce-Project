package com.project.controller;

import com.project.dto.CategoryDTO;
import com.project.entity.ApiResponse;
import com.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Category added successfully!"));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @PathVariable Long categoryId) {
        categoryService.updateCategory(categoryDTO, categoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Category updated successfully!"));
    }
    
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Category deleted successfully!"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getCategories() {
        List<CategoryDTO> categories = categoryService.getCategories();
        return ResponseEntity.ok(new ApiResponse<>(true, "Categories retrieved successfully!", categories));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategory(@PathVariable Long categoryId) {
        CategoryDTO category = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category retrieved successfully!", category));
    }

}