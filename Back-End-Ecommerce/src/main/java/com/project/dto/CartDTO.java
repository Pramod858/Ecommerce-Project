package com.project.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.project.entity.Cart;

public class CartDTO {
    
    private Long id;

    private List<CartItemDTO> cartItems;
    
    private double totalPrice;

    public CartDTO() {
    }
    
    public static CartDTO fromEntity(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setCartItems(
                cart.getCartItems().stream()
                        .map(CartItemDTO::fromEntity) 
                        .collect(Collectors.toList())
        );
        cartDTO.setTotalPrice(cart.getTotalPrice());
        return cartDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}