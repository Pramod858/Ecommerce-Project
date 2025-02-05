package com.project.controller;

import com.project.dto.OrderDTO;
import com.project.dto.PaymentDTO;
import com.project.entity.ApiResponse;
import com.project.entity.OrderStatusUpdateRequest;
import com.project.entity.User;
import com.project.exception.ResourceNotFoundException;
import com.project.service.AuthService;
import com.project.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @PostMapping("/place-order/{addressId}")
    public ResponseEntity<ApiResponse<Void>> createOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long addressId,
            @RequestBody @Valid PaymentDTO paymentDTO) {
        try {
            User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid JWT token"));
            }
            orderService.placeOrder(user.getId(), user.getCart().getId(), addressId, paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Order placed successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse<>(true, "Orders retrieved successfully!", orders));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUser(@RequestHeader("Authorization") String token) {
        try {
            User user = authService.getUserFromToken(token);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            List<OrderDTO> orders = orderService.getOrdersByUser(user.getId());
            return ResponseEntity.ok(new ApiResponse<>(true, "Orders retrieved successfully!", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            		.body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            		.body(new ApiResponse<>(false, "An error occurred: " + e.getMessage()));
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusUpdateRequest request) {
        try {
            orderService.updateOrderStatus(orderId, request.getStatus());
            return ResponseEntity.ok(new ApiResponse<>(true, "Order status updated successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/my-order/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderByOrderId(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrderByOrderId(orderId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Order retrieved successfully!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            		.body(new ApiResponse<>(false, e.getMessage()));
        }
    }
    
    @DeleteMapping("/delete-order/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
    	try {
    		orderService.deleteOrder(orderId);
    		return ResponseEntity.ok(new ApiResponse<>(true, "Order deleted successfully!"));
    	} catch (ResourceNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND)
            		.body(new ApiResponse<>(false, e.getMessage()));
		}
    }
}