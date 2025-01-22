package com.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.CartDTO;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.entity.Product;
import com.project.exception.CartNotFoundException;
import com.project.exception.ProductNotAvailableException;
import com.project.exception.ProductNotFoundException;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CartItemRepository;
import com.project.repository.CartRepository;
import com.project.repository.ProductRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) 
            throws ProductNotFoundException, CartNotFoundException, ProductNotAvailableException {

        // Validate Cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));

        // Validate Product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        // Validate Product Availability
        if (quantity <= 0 || product.getStock() < quantity) {
            throw new ProductNotAvailableException("Insufficient stock or invalid quantity for product: " + productId);
        }

        // Check if the product is already in the cart
        CartItem existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingCartItem != null) {
            // If the quantity is increasing, deduct the stock accordingly
            int oldQuantity = existingCartItem.getQuantity();
            if (quantity > oldQuantity) {
                int quantityToDeduct = quantity - oldQuantity;
                if (product.getStock() < quantityToDeduct) {
                    throw new ProductNotAvailableException("Insufficient stock for product: " + productId);
                }
                product.setStock(product.getStock() - quantityToDeduct);
            } else if (quantity < oldQuantity) {
                // If the quantity is decreasing, increase the stock
                product.setStock(product.getStock() + (oldQuantity - quantity));
            }

            // Update the quantity in the cart
            existingCartItem.setQuantity(quantity);
            cartItemRepository.save(existingCartItem);
        } else {
            // Create a new CartItem
            if (product.getStock() < quantity) {
                throw new ProductNotAvailableException("Insufficient stock for product: " + productId);
            }

            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cartItemRepository.save(newCartItem);

            // Deduct stock from the product
            product.setStock(product.getStock() - quantity);
        }

        // Save the updated product
        productRepository.save(product);

        // Recalculate and update the cart's total
        updateCartTotal(cart);

        // Save the updated cart
        cartRepository.save(cart);

        // Convert to DTO and return
        return CartDTO.fromEntity(cart);
    }

    /**
     * Helper method to update the cart total.
     * @param cart The cart whose total needs to be updated.
     */
    private void updateCartTotal(Cart cart) {
        double total = cart.getCartItems()
                .stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(total);
    }

	@Override
	public List<CartDTO> getAllCarts() {
		List<Cart> carts = cartRepository.findAll();
		return carts.stream()
				.map(CartDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public CartDTO getCartById(Long cartId) throws CartNotFoundException {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));
		return CartDTO.fromEntity(cart);
	}

	@Override
	public void removeProductFromCart(Long cartId, Long productId) throws CartNotFoundException, ProductNotFoundException {
		Cart cart = cartRepository.findById(cartId)
	            .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));

	    // Find the CartItem
	    CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, 
	            productRepository.findById(productId)
	                    .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId)));
	    
	    
	    if (cartItem == null) {
	        throw new ProductNotFoundException("Product not found in the cart with ID: " + productId);
	    }
	    
	    Product product = cartItem.getProduct();
	    product.setStock(product.getStock() + cartItem.getQuantity());
	    productRepository.save(product);
	    
	    cartItemRepository.delete(cartItem);
	    
	    updateCartTotal(cart);
	    
	    cartRepository.save(cart);
	}

	@Override
	public void clearCart(Long cartId) throws CartNotFoundException {
	    // Validate Cart
	    Cart cart = cartRepository.findById(cartId)
	            .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));

	    // Get all cart items before modifying them
	    List<CartItem> cartItems = cart.getCartItems();

	    if (cartItems.isEmpty()) {
	        throw new CartNotFoundException("No products found in the cart to clear");
	    }

	    // Iterate and remove each item safely
	    for (CartItem cartItem : cartItems) {
	        Product product = cartItem.getProduct();
	        product.setStock(product.getStock() + cartItem.getQuantity()); // Restore stock
	        productRepository.save(product);
	        
	        cartItemRepository.delete(cartItem);
	    }

	    // Clear the cart's item list explicitly
	    cart.getCartItems().clear();  // Ensure list is empty
	    cart.setTotalPrice(0.0);
	    
	    // Save updated cart
	    cartRepository.save(cart);
	}



}
