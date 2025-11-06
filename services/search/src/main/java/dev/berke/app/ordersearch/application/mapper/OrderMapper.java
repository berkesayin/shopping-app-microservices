package dev.berke.app.ordersearch.application.mapper;

import org.springframework.stereotype.Component;
import dev.berke.app.consumer.event.OrderCreatedEvent;
import dev.berke.app.ordersearch.domain.document.OrderDocument;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDocument toDocument(OrderCreatedEvent event) {
        if (event == null) {
            return null;
        }

        return OrderDocument.builder()
                .orderId(event.orderId())
                .reference(event.reference())
                .orderDate(event.orderDate())
                .status(event.status())
                .totalAmount(event.totalAmount())
                .paymentMethod(event.paymentMethod())
                .customer(toCustomerDocument(event.customer()))
                .shippingAddress(toAddressDocument(event.activeShippingAddress()))
                .billingAddress(toAddressDocument(event.activeBillingAddress()))
                .items(event.items().stream().map(this::toItemDocument).collect(Collectors.toList()))
                .build();
    }

    private OrderDocument.CustomerDocument toCustomerDocument(OrderCreatedEvent.CustomerInfo customerInfo) {
        if (customerInfo == null) {
            return null;
        }
        return OrderDocument.CustomerDocument.builder()
                .id(customerInfo.id())
                .fullName(customerInfo.fullName())
                .email(customerInfo.email())
                .build();
    }

    private OrderDocument.AddressDocument toAddressDocument(OrderCreatedEvent.AddressInfo addressInfo) {
        if (addressInfo == null) {
            return null;
        }
        return OrderDocument.AddressDocument.builder()
                .contactName(addressInfo.contactName())
                .city(addressInfo.city())
                .country(addressInfo.country())
                .address(addressInfo.address())
                .zipCode(addressInfo.zipCode())
                .build();
    }

    private OrderDocument.ItemDocument toItemDocument(OrderCreatedEvent.ItemInfo itemInfo) {
        if (itemInfo == null) {
            return null;
        }
        return OrderDocument.ItemDocument.builder()
                .productId(itemInfo.productId())
                .productName(itemInfo.productName())
                .manufacturer(itemInfo.manufacturer())
                .quantity(itemInfo.quantity())
                .pricePerUnit(itemInfo.basePrice())
                .build();
    }
}