package com.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.OrderDTO;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.entity.Product;
import com.project.entity.ShippingAddress;
import com.project.exception.CartNotFoundException;
import com.project.exception.OrderNotFoundException;
import com.project.exception.ProductNotAvailableException;
import com.project.exception.UserNotFoundException;
import com.project.repository.CartItemRepository;
import com.project.repository.CartRepository;
import com.project.repository.OrderItemRepository;
import com.project.repository.OrderRepository;
import com.project.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	

	@Override
	@Transactional
	public void placeOrder(Long cartId, ShippingAddress address) throws CartNotFoundException, ProductNotAvailableException {
	    // Validate cart
	    Cart cart = cartRepository.findById(cartId)
	            .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));

	    if (cart.getCartItems().isEmpty()) {
	        throw new ProductNotAvailableException("Cart is empty. Cannot place order.");
	    }

	    // Check stock availability for all items
	    for (CartItem cartItem : cart.getCartItems()) {
	        if (cartItem.getProduct().getStock() < cartItem.getQuantity()) {
	            throw new ProductNotAvailableException("Insufficient stock for product: " + cartItem.getProduct().getName());
	        }
	    }

	    // Create order
	    Order order = new Order();
	    order.setUser(cart.getUser());
	    order.setShippingAddress(address);

	    List<OrderItem> orderItems = new ArrayList<>();
	    double totalPrice = 0.0;

	    for (CartItem cartItem : cart.getCartItems()) {
	        // Deduct stock for the product
	        Product product = cartItem.getProduct();
	        product.setStock(product.getStock() - cartItem.getQuantity());
	        
	        productRepository.save(product);

	        // Create order item
	        OrderItem orderItem = new OrderItem();
	        orderItem.setProduct(product);
	        orderItem.setQuantity(cartItem.getQuantity());
	        double itemPrice = product.getPrice() * cartItem.getQuantity();
	        orderItem.setPrice(itemPrice); // Calculate and set price
	        orderItem.setOrder(order); // Link order item to the order
	        orderItems.add(orderItem);

	        // Add to total order price
	        totalPrice += itemPrice;
	    }

	    // Save order first (to generate an order ID)
	    order.setTotalPrice(totalPrice);
	    order.setOrderItems(orderItems);
	    order = orderRepository.save(order);

	    // Save order items
	    for (OrderItem orderItem : orderItems) {
	        // Persist each order item
	        orderItemRepository.save(orderItem);
	    }

	    // Remove all CartItems from the database
	    cart.getCartItems().forEach(cartItem -> cartItemRepository.delete(cartItem));

	    // Clear the cart (in memory and database)
	    cart.getCartItems().clear();
	    cart.setTotalPrice(0.0);
	    cartRepository.save(cart);
	}


	@Override
	public List<OrderDTO> getAllOrders() {
		return orderRepository.findAll().stream()
				.map(OrderDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderDTO> getOrdersByUser(Long userId) throws UserNotFoundException {
		List<Order> userOrders = orderRepository.findByUserId(userId);
		if (userOrders.isEmpty()) {
			throw new UserNotFoundException("No orders found for user with Id: " + userId);
		}
		return userOrders.stream()
				.map(OrderDTO::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public OrderDTO getOrderByOrderId(Long orderId) throws OrderNotFoundException {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException("Order not found with Id: " + orderId));
		if (order.getOrderItems() == null) {
			order.setOrderItems(new ArrayList<>());
		}
		return OrderDTO.fromEntity(order);
	}
}
