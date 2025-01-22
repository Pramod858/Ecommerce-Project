package com.project.entity;

public enum OrderStatus {
    PENDING,   // Order placed but not yet processed
    PROCESSING, // Order is being prepared
    SHIPPED,   // Order has been shipped
    DELIVERED, // Order has been delivered
    CANCELED   // Order has been canceled
}
