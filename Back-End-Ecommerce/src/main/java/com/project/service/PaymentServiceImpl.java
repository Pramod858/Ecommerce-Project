package com.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.entity.Order;
import com.project.entity.Payment;
import com.project.entity.User;
import com.project.exception.OrderNotFoundException;
import com.project.exception.UserNotFoundException;
import com.project.repository.OrderRepository;
import com.project.repository.PaymentRepository;
import com.project.repository.UserRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Payment createPayment(Payment payment) throws Exception {
	    // Check if the user exists (this check is now redundant since the user is already validated in the controller)
	    Optional<User> user = userRepository.findById(payment.getUser().getId());
	    if (!user.isPresent()) {
	        throw new UserNotFoundException("User not found");
	    }

	    // Check if the order exists
	    Optional<Order> order = orderRepository.findById(payment.getOrder().getId());
	    if (!order.isPresent()) {
	        throw new OrderNotFoundException("Order not found");
	    }

	    // Set payment details if needed
	    payment.setPaymentStatus(false); // Assuming payment is pending initially

	    // Save the payment
	    return paymentRepository.save(payment);
	}

	@Override
	public Payment getPaymentById(Long paymentId) {
		// TODO Auto-generated method stub
		return paymentRepository.findById(paymentId).orElse(null);
	}
}
