package com.project.dto;

import com.project.entity.CartItem;

class CartItemDTO {

    private Long id;

    private ProductDTO product;

    private Integer quantity;

    public static CartItemDTO fromEntity(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setProduct(ProductDTO.fromEntity(cartItem.getProduct())); 
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) { 
        this.product = product; 
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}