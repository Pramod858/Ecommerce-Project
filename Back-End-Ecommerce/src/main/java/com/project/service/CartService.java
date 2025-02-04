package com.project.service;

import java.util.List;

import com.project.dto.CartDTO;

public interface CartService {

	void addProductToCart(Long productId, Integer quantity, Long cartId);

	List<CartDTO> getAllCarts();

	CartDTO getCartById(Long cartId);

	void removeProductFromCart(Long productId, Long cartId);

	void clearCart(Long cartId);
	

}
