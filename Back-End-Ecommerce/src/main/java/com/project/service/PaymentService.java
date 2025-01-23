package com.project.service;

import com.project.entity.Payment;

import jakarta.validation.Valid;

public interface PaymentService {

	Payment createPayment(Payment payment) throws Exception;

	Payment getPaymentById(Long paymentId);

}
