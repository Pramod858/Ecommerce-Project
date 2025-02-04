package com.project.service;

import com.project.dto.PaymentDTO;
import com.project.entity.Payment;

import jakarta.validation.Valid;

public interface PaymentService {

	Payment createPayment(@Valid PaymentDTO paymentDTO);

	Payment getPaymentById(Long paymentId);
	
}
