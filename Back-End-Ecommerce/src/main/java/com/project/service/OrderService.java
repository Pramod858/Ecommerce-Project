package com.project.service;

import java.util.List;

import com.project.dto.OrderDTO;
import com.project.dto.PaymentDTO;

import jakarta.validation.Valid;

public interface OrderService {

	void placeOrder(Long id, Long cartId, Long addressId, @Valid PaymentDTO paymentDTO);

	List<OrderDTO> getAllOrders();

	List<OrderDTO> getOrdersByUser(Long id);

	OrderDTO getOrderByOrderId(Long orderId);

	void updateOrderStatus(Long orderId, String status);

	void deleteOrder(Long orderId);

}
