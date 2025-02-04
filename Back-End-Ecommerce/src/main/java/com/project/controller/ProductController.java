package com.project.controller;

import com.project.dto.ProductDTO;
import com.project.entity.ApiResponse;
import com.project.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/category/{categoryId}/product")
    public ResponseEntity<ApiResponse<Void>> addProduct(
            @Valid @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId) {
        productService.addProduct(productDTO, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Product added successfully!"));
    }
    
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDTO productDTO) {
        productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully!"));
    }
    
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted successfully!"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(true, "Products retrieved successfully!", products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product retrieved successfully!", product));
    }

    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Products retrieved successfully!", products));
    }
}