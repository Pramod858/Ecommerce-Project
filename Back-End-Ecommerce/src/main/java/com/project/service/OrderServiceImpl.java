package com.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dto.OrderDTO;
import com.project.dto.PaymentDTO;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.entity.OrderStatus;
import com.project.entity.Payment;
import com.project.entity.Product;
import com.project.entity.ShippingAddress;
import com.project.entity.User;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CartRepository;
import com.project.repository.OrderRepository;
import com.project.repository.PaymentRepository;
import com.project.repository.ProductRepository;
import com.project.repository.ShippingAddressRepository;
import com.project.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository, ShippingAddressRepository shippingAddressRepository, ProductRepository productRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public void placeOrder(Long userId, Long cartId, Long addressId, PaymentDTO paymentDTO) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId));

        ShippingAddress address = shippingAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("ShippingAddress", addressId));

        if (cart.getCartItems().isEmpty()) {
            throw new ResourceNotFoundException("Cart", cartId);
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setTotalPrice(cart.getTotalPrice());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new ResourceNotFoundException("Product", product.getId());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice() * cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalPrice += orderItem.getPrice();

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(order.getTotalPrice());
        payment.setOrder(savedOrder);
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());
        payment.setCardNumber(paymentDTO.getCardNumber());
        payment.setCardCVV(paymentDTO.getCardCVV());
        payment.setCardExpiry(paymentDTO.getCardExpiry());
        payment.setCardholderName(paymentDTO.getCardholderName());
        paymentRepository.save(payment);

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
    public List<OrderDTO> getOrdersByUser(Long userId) throws ResourceNotFoundException {
        List<Order> userOrders = orderRepository.findByUserId(userId);
        if (userOrders.isEmpty()) {
            throw new ResourceNotFoundException("Orders", userId);
        }
        return userOrders.stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderByOrderId(Long orderId) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return OrderDTO.fromEntity(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) throws ResourceNotFoundException {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        existingOrder.setStatus(orderStatus);
        orderRepository.save(existingOrder);
    }

	@Override
	public void deleteOrder(Long orderId) {
		Order existingOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
		orderRepository.delete(existingOrder);
	}
}