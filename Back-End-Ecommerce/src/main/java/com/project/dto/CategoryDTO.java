package com.project.dto;

import java.util.List;

public class CategoryDTO {
	
	private Long id;
	
	private String name;
	
	private List<ProductDTO> products;

	public CategoryDTO() {
	}

	public Long getId() {
		return id;
	}
	
	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CategoryDTO(Long id, String name, List<ProductDTO> products) {
		super();
		this.id = id;
		this.name = name;
		this.products = products;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}

}
