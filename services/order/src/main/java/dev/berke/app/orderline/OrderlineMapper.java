package dev.berke.app.orderline;

import dev.berke.app.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderlineMapper {

    public Orderline toOrderLine(OrderlineRequest orderlineRequest) {
        return Orderline.builder()
                .id(orderlineRequest.id())
                .order(
                        Order.builder()
                                .id(orderlineRequest.orderId())
                                .build()
                )
                .productId(orderlineRequest.productId())
                .quantity(orderlineRequest.quantity())
                .build();
    }

    public OrderlineResponse toOrderLineResponse(Orderline orderline) {
        return new OrderlineResponse(orderline.getId(), orderline.getQuantity());
    }
}