package com.project.service;

import java.util.List;

import com.project.dto.CartDTO;
import com.project.exception.CartNotFoundException;
import com.project.exception.ProductNotAvailableException;
import com.project.exception.ProductNotFoundException;

public interface CartService {

	CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) throws ProductNotFoundException, CartNotFoundException, ProductNotAvailableException;

	List<CartDTO> getAllCarts();

	CartDTO getCartById(Long cartId) throws CartNotFoundException;

	void removeProductFromCart(Long cartId, Long productId) throws CartNotFoundException, ProductNotFoundException;

	void clearCart(Long cartId) throws CartNotFoundException, ProductNotFoundException;


}
