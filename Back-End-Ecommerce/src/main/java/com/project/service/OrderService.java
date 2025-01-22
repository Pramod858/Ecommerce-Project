package com.project.service;

import java.util.List;

import com.project.dto.OrderDTO;
import com.project.entity.ShippingAddress;
import com.project.exception.CartNotFoundException;
import com.project.exception.OrderNotFoundException;
import com.project.exception.ProductNotAvailableException;
import com.project.exception.UserNotFoundException;

public interface OrderService {

	List<OrderDTO> getAllOrders();

	List<OrderDTO> getOrdersByUser(Long userId) throws UserNotFoundException;

	OrderDTO getOrderByOrderId(Long orderId) throws OrderNotFoundException;

	void placeOrder(Long cartId, ShippingAddress address) throws CartNotFoundException, ProductNotAvailableException;

}
