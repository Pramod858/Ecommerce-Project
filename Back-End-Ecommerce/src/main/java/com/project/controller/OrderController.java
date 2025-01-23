package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.OrderDTO;
import com.project.entity.Cart;
import com.project.entity.ShippingAddress;
import com.project.entity.User;
import com.project.exception.CartNotFoundException;
import com.project.exception.OrderNotFoundException;
import com.project.exception.ProductNotAvailableException;
import com.project.exception.UserNotFoundException;
import com.project.repository.ShippingAddressRepository;
import com.project.service.AuthService;
import com.project.service.OrderService;

@RestController
@RequestMapping("api/orders")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
    private ShippingAddressRepository shippingAddressRepository;
	
	@PostMapping("/place-order/{addressId}")
	public ResponseEntity<?> placeOrder(
	        @RequestHeader("Authorization") String token,
	        @PathVariable Long addressId) {
	    try {
	        // Get user from token using AuthService
	        User user = authService.getUserFromToken(token);
	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
	        }

	        // Get the user's cart
	        Cart cart = user.getCart();
	        if (cart == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
	        }

	        // Get the shipping address from the repository
	        ShippingAddress address = shippingAddressRepository.findById(addressId)
	                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

	        // Place the order by passing the cart ID and address
	        orderService.placeOrder(cart.getId(), address);

	        // Return response indicating order was successfully placed
	        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Order placed successfully!"));
	    } catch (CartNotFoundException | ProductNotAvailableException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid address ID");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}

	
	@GetMapping
	public ResponseEntity<?> getAllOrders() {
		List<OrderDTO> ordersResponse = orderService.getAllOrders();
		return ResponseEntity.ok(ordersResponse);
	}
	
	@GetMapping("/my-orders")
	public ResponseEntity<?> getOrdersByUser(@RequestHeader("Authorization") String token) {
	    try {
	    	User user = authService.getUserFromToken(token);  // Use AuthService
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
            }
	    	
	        List<OrderDTO> orders = orderService.getOrdersByUser(user.getId());
	        return ResponseEntity.ok(orders); // Return with 200 OK status
	    } catch (UserNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Return 404 if user/orders not found
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Catch any unexpected exceptions
	    }
	}
	
	
	@GetMapping("/my-order/{orderId}")
	public ResponseEntity<?> getOrderByOrderId(@PathVariable Long orderId) {
	    try {
	        OrderDTO order = orderService.getOrderByOrderId(orderId);
	        return ResponseEntity.ok(order);
	    } catch (OrderNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}
}