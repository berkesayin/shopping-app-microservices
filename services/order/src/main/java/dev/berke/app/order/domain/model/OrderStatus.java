package dev.berke.app.order.domain.model;

public enum OrderStatus {
    PAYMENT_FAILED,
    PENDING_PAYMENT,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED,
    REFUNDED
}
