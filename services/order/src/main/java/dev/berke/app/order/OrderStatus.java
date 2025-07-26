package dev.berke.app.order;

public enum OrderStatus {
    PAYMENT_FAILED,
    PENDING_PAYMENT,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED,
    REFUNDED
}
