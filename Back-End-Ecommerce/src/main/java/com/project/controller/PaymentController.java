package com.project.controller;

import com.project.dto.PaymentDTO;
import com.project.entity.ApiResponse;
import com.project.entity.Payment;
import com.project.exception.ResourceNotFoundException;
import com.project.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Payment>> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            Payment createdPayment = paymentService.createPayment(paymentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Payment created successfully!", createdPayment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Failed to create payment: " + e.getMessage()));
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<Payment>> getPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Payment retrieved successfully!", payment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Failed to retrieve payment: " + e.getMessage()));
        }
    }
}