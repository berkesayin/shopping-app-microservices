package dev.berke.app.events;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        String reference,
        Instant orderDate,
        String status,
        BigDecimal totalAmount,
        String paymentMethod,
        CustomerInfo customer,
        AddressInfo activeShippingAddress,
        AddressInfo activeBillingAddress,
        List<ItemInfo> items
) {
    // denormalization for order index documents
    public record CustomerInfo(
            String id,
            String fullName,
            String email
    ) {}

    public record AddressInfo(
            String contactName,
            String city,
            String country,
            String address,
            String zipCode
    ) {}

    public record ItemInfo(
            Integer productId,
            String productName,
            String manufacturer,
            Integer categoryId,
            Integer quantity,
            BigDecimal basePrice
    ) {}

    //  static factory method with @Builder
    @Builder
    public static OrderCreatedEvent build(
            String orderId,
            String reference,
            Instant orderDate,
            String status,
            BigDecimal totalAmount,
            String paymentMethod,
            CustomerInfo customer,
            AddressInfo shippingAddress,
            AddressInfo billingAddress,
            List<ItemInfo> items
    ) {
        return new OrderCreatedEvent(
                orderId,
                reference,
                orderDate,
                status,
                totalAmount,
                paymentMethod,
                customer,
                shippingAddress,
                billingAddress,
                items
        );
    }
}