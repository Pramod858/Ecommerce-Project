package com.project.service;

import com.project.dto.CartDTO;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.entity.Product;
import com.project.exception.ResourceConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CartItemRepository;
import com.project.repository.CartRepository;
import com.project.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void addProductToCart(Long productId, Integer quantity, Long cartId) throws ResourceNotFoundException, ResourceConflictException {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        if (quantity <= 0 || product.getStock() < quantity) {
            throw new ResourceConflictException("Product", productId);
        }

        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingCartItemOpt.isPresent()) {
            CartItem existingCartItem = existingCartItemOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cartItemRepository.save(newCartItem);
        }
        
        updateCartTotal(cart);

        cartRepository.save(cart);
        
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
    
    private void updateCartTotal(Cart cart) {
    	double totalPrice = cart.getCartItems()
    			.stream()
    			.mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
    			.sum();
    	cart.setTotalPrice(totalPrice);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream()
                .map(CartDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO getCartById(Long cartId) throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));
        return CartDTO.fromEntity(cart);
    }

    @Override
    public void removeProductFromCart(Long productId, Long cartId) throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartId)  // Assuming a single cart per user
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", productId));

        product.setStock(product.getStock() + cartItem.getQuantity());
        productRepository.save(product);

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long cartId) throws ResourceNotFoundException {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));

        List<CartItem> cartItems = cartItemRepository.findAllByCart(cart);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() + cartItem.getQuantity());
            productRepository.save(product);
        }

        cartItemRepository.deleteAll(cartItems);
    }
}