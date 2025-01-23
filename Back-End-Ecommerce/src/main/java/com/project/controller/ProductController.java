package com.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.dto.ProductDTO;
import com.project.service.ProductService;

@RestController
@RequestMapping("api/products")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/category/{categoryId}/product")
    public ResponseEntity<String> addProduct(
            @RequestBody ProductDTO productDTO,
            @PathVariable Long categoryId) {
        productService.addProduct(productDTO, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!");
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping({"/{productId}"})
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
    	ProductDTO product = productService.getProductById(productId);
    	return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDTO productDTO) {
        productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok("Product updated successfully!");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully!");
    }
}
