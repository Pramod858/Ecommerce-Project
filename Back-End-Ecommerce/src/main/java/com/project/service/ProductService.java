package com.project.service;

import java.util.List;

import com.project.dto.ProductDTO;

public interface ProductService {

	void addProduct(ProductDTO productDTO, Long categoryId);

	List<ProductDTO> getAllProducts();

	List<ProductDTO> getProductsByCategory(Long categoryId);

	void updateProduct(Long productId, ProductDTO productDTO);

	void deleteProduct(Long productId);

	ProductDTO getProductById(Long productId);

}
