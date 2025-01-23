package com.project.dto;

import com.project.entity.OrderItem;

public class OrderItemDTO {
	
	private Long id;
    private Long productId; // Reference to the Product ID
    private String productName; // Product name for clarity
    private Integer quantity;
    private Double price; // Price for the quantity of the product
    private String productImgUrl;
	
	public OrderItemDTO() {
	}
	
	public static OrderItemDTO fromEntity(OrderItem orderItem) {
		OrderItemDTO dto = new OrderItemDTO();
		dto.setId(orderItem.getId());
		dto.setProductId(orderItem.getProduct().getId());
		dto.setProductName(orderItem.getProduct().getName());
		dto.setQuantity(orderItem.getQuantity());
		dto.setPrice(orderItem.getProduct().getPrice() * orderItem.getQuantity());
		dto.setProductImgUrl(orderItem.getProduct().getImageUrl());
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getProductImgUrl() {
		return productImgUrl;
	}

	public void setProductImgUrl(String productImgUrl) {
		this.productImgUrl = productImgUrl;
	}
	
}
