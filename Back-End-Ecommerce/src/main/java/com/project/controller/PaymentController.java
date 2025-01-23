package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.entity.Payment;
import com.project.entity.User;
import com.project.service.AuthService;
import com.project.service.CartService;
import com.project.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private AuthService authService;

	@PostMapping("/create")
	public ResponseEntity<?> createPayment(
	        @RequestHeader("Authorization") String token,
	        @Valid @RequestBody Payment payment) {
	    try {
	        // Extract user from the token
	        User user = authService.getUserFromToken(token);
	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token");
	        }

	        // Set the user in the payment object (IMPORTANT)
	        payment.setUser(user);

	        // Call the service to create the payment
	        Payment createdPayment = paymentService.createPayment(payment);

	        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Failed to create payment: " + e.getMessage()));
	    }
	}

	
	@GetMapping("/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            if (payment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
            }

            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Failed to retrieve payment: " + e.getMessage()));
        }
    }
     
}

