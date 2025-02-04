package com.project.controller;

import com.project.dto.CartDTO;
import com.project.entity.ApiResponse;
import com.project.entity.User;
import com.project.exception.ResourceConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.service.AuthService;
import com.project.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class CartController {

    private final CartService cartService;
    private final AuthService authService;

    public CartController(CartService cartService, AuthService authService) {
        this.cartService = cartService;
        this.authService = authService;
    }

    @PostMapping("/product/{productId}/{quantity}")
    public ResponseEntity<ApiResponse<Void>> addProductToCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId,
            @PathVariable Integer quantity) {

        try {
        	User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            cartService.addProductToCart(productId, quantity, user.getCart().getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Product added to cart successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (ResourceConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred"));
        }
    }
    
    @DeleteMapping("/my-cart/product/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeProductFromCart(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId) {
        try {
        	User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            cartService.removeProductFromCart(productId, user.getCart().getId());
            return ResponseEntity.ok(new ApiResponse<>(true, "Product removed from cart successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to remove product from cart"));
        }
    }

    @DeleteMapping("/my-cart/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @RequestHeader("Authorization") String token) {
        try {
        	User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            cartService.clearCart(user.getCart().getId());
            return ResponseEntity.ok(new ApiResponse<>(true, "Cart cleared successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to clear cart"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartDTO>>> getAllCarts() {
        try {
            List<CartDTO> carts = cartService.getAllCarts();
            return ResponseEntity.ok(new ApiResponse<>(true, "Carts retrieved successfully!", carts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            		.body(new ApiResponse<>(false, "Failed to retrieve cart"));
        }
    }

    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse<CartDTO>> getCartById(
            @RequestHeader("Authorization") String token) {
        try {
        	User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            CartDTO cart = cartService.getCartById(user.getCart().getId());
            return ResponseEntity.ok(new ApiResponse<>(true, "Cart retrieved successfully!", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to retrieve cart"));
        }
    }

}