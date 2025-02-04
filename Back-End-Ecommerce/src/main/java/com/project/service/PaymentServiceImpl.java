package com.project.service;

import com.project.dto.PaymentDTO;
import com.project.entity.Payment;
import com.project.entity.User;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.PaymentRepository;
import com.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Payment createPayment(PaymentDTO paymentDTO) {
        Optional<User> user = userRepository.findById(paymentDTO.getUserId());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User", paymentDTO.getUserId());
        }

        Payment payment = new Payment();
        payment.setUser(user.get());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long paymentId) throws ResourceNotFoundException {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));
    }
}