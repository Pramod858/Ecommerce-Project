package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.CartDTO;
import com.project.entity.Cart;
import com.project.entity.User;
import com.project.exception.CartNotFoundException;
import com.project.exception.ProductNotAvailableException;
import com.project.exception.ProductNotFoundException;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.UserRepository;
import com.project.security.JwtUtils;
import com.project.service.AuthService;
import com.project.service.CartService;

@RestController
@RequestMapping("api/carts")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/product/{productId}/{quantity}")
	public ResponseEntity<?> addProductToCart(
	        @RequestHeader("Authorization") String token,
	        @PathVariable Long productId,
	        @PathVariable Integer quantity) {

	    try {
	    	User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }

	        // Get the user's cart
	        Cart cart = user.getCart();
	        if (cart == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
	        }

	        // Call the service layer to add the product to the cart
	        cartService.addProductToCart(cart.getId(), productId, quantity);

	        // Return success message
	        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Product added to cart successfully!"));

	    } catch (ProductNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (CartNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (ProductNotAvailableException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}

	
	@GetMapping
	public ResponseEntity<List<CartDTO>> getAllCarts() {
		try {
			List<CartDTO> carts = cartService.getAllCarts();
			return new ResponseEntity<>(carts, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/my-cart")
	public ResponseEntity<?> getCartById(
	        @RequestHeader("Authorization") String token) {
	    try {
	    	User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
            
         // Get the user's cart
	        Cart cart = user.getCart();
	        if (cart == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
	        }

	        // Call the service layer to get the cart DTO
	        CartDTO cartDTO = cartService.getCartById(cart.getId());

	        return ResponseEntity.ok(cartDTO);

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}


	@DeleteMapping("/my-cart/product/{productId}")
	public ResponseEntity<?> removeProductFromCart(
	        @RequestHeader("Authorization") String token,
	        @PathVariable Long productId) {

	    try {
	    	User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
            
	        // Get the user's cart
	        Cart cart = user.getCart();
	        if (cart == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
	        }

	        // Call the service layer to remove the product from the cart
	        cartService.removeProductFromCart(cart.getId(), productId);

	        // If successful, return a success message with HTTP status OK
	        return ResponseEntity.ok(new ResponseMessage("Product removed from cart successfully!"));

	    } catch (CartNotFoundException e) {
	        // Return an error message with HTTP status NOT_FOUND if the cart is not found
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to remove product: Cart not found. " + e.getMessage());

	    } catch (ProductNotFoundException e) {
	        // Return an error message with HTTP status NOT_FOUND if the product is not found in the cart
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to remove product: Product not found in cart. " + e.getMessage());

	    } catch (Exception e) {
	        // Handle any other exceptions with a generic error message
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove product: " + e.getMessage());
	    }
	}

	
	@DeleteMapping("/my-cart/clear")
	public ResponseEntity<?> clearCart(
			@RequestHeader("Authorization") String token) {
	    try {
	    	
	    	User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }

	        // Get the user's cart
	        Cart cart = user.getCart();
	        if (cart == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
	        }
	    	
	        // Call the service layer to clear the cart
	        cartService.clearCart(cart.getId());

	        // Return a success response
	        return ResponseEntity.ok(new ResponseMessage("Cart cleared successfully!"));
	    } catch (CartNotFoundException e) {
	        // Return error response if the cart is not found
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to clear cart: " + e.getMessage());
	    } catch (ProductNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to the product: ");
	    } catch (Exception e) {
	        // Handle any other exceptions
	        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


}