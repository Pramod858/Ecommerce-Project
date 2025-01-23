package com.project.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.project.entity.Order;
import com.project.entity.OrderStatus;

public class OrderDTO {

    private Long id;
    private Long userId; // Use only the user ID to avoid exposing the full User entity
    private LocalDateTime orderDate;
    private Double totalPrice;
    private List<OrderItemDTO> orderItems; // Updated to List<OrderItemDTO>
    private OrderStatus status = OrderStatus.PENDING;
    private ShippingAddressDTO shippingAddress;

    public OrderDTO() {
    }

    public static OrderDTO fromEntity(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(ShippingAddressDTO.fromEntity(order.getShippingAddress()));
        dto.setOrderItems(order.getOrderItems().stream()
                .map(OrderItemDTO::fromEntity)
                .toList());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public ShippingAddressDTO getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(ShippingAddressDTO shippingAddress) {
		this.shippingAddress = shippingAddress;
	}
	
	
    
}
