package dev.berke.app.order;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderMapper {

    public Order toOrder(
            OrderRequest orderRequest,
            String customerId,
            String customerEmail,
            BigDecimal totalAmount
    ) {
        return Order.builder()
                .reference(orderRequest.reference())
                .customerId(customerId)
                .customerEmail(customerEmail)
                .totalAmount(totalAmount)
                .paymentMethod(orderRequest.paymentMethod())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReference()
        );
    }

}