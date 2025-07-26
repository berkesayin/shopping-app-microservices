package dev.berke.app.order;

import dev.berke.app.basket.*;
import dev.berke.app.constants.OrderConstants;
import dev.berke.app.customer.Address;
import dev.berke.app.customer.CustomerClient;
import dev.berke.app.customer.CustomerResponse;
import dev.berke.app.events.OrderCreatedEvent;
import dev.berke.app.exception.BusinessException;
import dev.berke.app.events.OrderReceivedEvent;
import dev.berke.app.kafka.OrderEventProducer;
import dev.berke.app.orderline.OrderLineRequest;
import dev.berke.app.orderline.OrderLineService;
import dev.berke.app.payment.PaymentClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineService orderLineService;
    private final OrderMapper orderMapper;
    private final CustomerClient customerClient;
    private final BasketClient basketClient;
    private final PaymentClient paymentClient;
    private final OrderEventProducer orderEventProducer;

    // Business logic to create order
    // 1. validate customer and basket
    // 2. create order with PENDING_PAYMENT status and save order lines
    // 3. initiate payment: iyzi payment
    // 4. handle payment confirmed
    // 5. publish OrderCreatedEvent for order index
    // 6. publish OrderReceivedEvent for customer email
    // if payment failed: update order status to PAYMENT_FAILED
    // 7. return order response

    @Transactional
    public OrderResponse createOrder(
            OrderRequest orderRequest,
            String customerId
    ) {
        // 1. validate customer and basket
        var customer = this.customerClient.getProfile()
                .orElseThrow(() -> new BusinessException(
                        OrderConstants.CUSTOMER_NOT_FOUND_MESSAGE));

        if (!customer.id().equals(customerId)) {
            throw new IllegalStateException("Auth token mismatch: " +
                    "Token owner does not match the operation.");
        }

        var basket = this.basketClient.getBasket()
                .orElseThrow(() -> new BusinessException(
                        OrderConstants.BASKET_EMPTY_MESSAGE));

        var productsToPurchase = basket.items();

        ResponseEntity<BasketTotalPriceResponse> basketTotalPriceResponse =
                basketClient.getTotalBasketPrice();

        BigDecimal totalPrice = basketTotalPriceResponse.getBody().totalPrice();

        log.info("Validated customer '{}' and basket. " + "Products: {}" +
                "Total basket price: {}", customer.id(), productsToPurchase, totalPrice);

        // 2. create order with PENDING_PAYMENT status and save order lines
        var order = orderMapper.toOrder(
                orderRequest,
                customerId,
                customer.email(),
                totalPrice
        );
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        var savedOrder = this.orderRepository.save(order);

        log.info("Saved new order with reference '{}' and status PENDING_PAYMENT",
                savedOrder.getReference());

        for (BasketItem basketItem : basket.items()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            savedOrder.getId(),
                            basketItem.productId(),
                            basketItem.quantity()
                    )
            );
        }

        // handle payment process
        try {
            // 3. initiate payment
            log.info("Initiating payment for order reference: {}", savedOrder.getReference());
            paymentClient.createPayment();

            // 4. handle payment confirmed
            savedOrder.setStatus(OrderStatus.PROCESSING);
            this.orderRepository.save(savedOrder);

            // 5. publish OrderCreatedEvent for order index
            OrderCreatedEvent orderCreatedEvent =
                    buildOrderCreatedEvent(savedOrder, customer, basket.items());

            orderEventProducer.sendOrderCreated(orderCreatedEvent);

            // 6. publish OrderReceivedEvent for customer email
            orderEventProducer.sendOrderConfirmation(new OrderReceivedEvent(
                    customer.name() + " " + customer.surname(),
                    customer.email(),
                    savedOrder.getReference(),
                    savedOrder.getPaymentMethod(),
                    basket.items(),
                    savedOrder.getTotalAmount()
            ));

            // 7. return order response
            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getReference()
            );

        } catch (RuntimeException e) {
            log.error("Payment failed for order reference: '{}'. " +
                    "Updating status to PAYMENT_FAILED.", savedOrder.getReference(), e);

            savedOrder.setStatus(OrderStatus.PAYMENT_FAILED);
            this.orderRepository.save(savedOrder);
            throw new BusinessException("Payment was declined: " + e.getMessage());
        }
    }

    private OrderCreatedEvent buildOrderCreatedEvent(
            Order order,
            CustomerResponse customer,
            List<BasketItem> items
    ) {
        Address activeShippingAddress = customer
                .shippingAddresses()
                .stream()
                .filter(a -> a.id().equals(customer.activeShippingAddressId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Active shipping address " +
                        "not found for customer " + customer.id()));

        Address activeBillingAddress = customer
                .billingAddresses()
                .stream()
                .filter(a -> a.id().equals(customer.activeBillingAddressId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Active billing address " +
                        "not found for customer " + customer.id()));

        // map basket items to the event's item info records
        List<OrderCreatedEvent.ItemInfo> itemInfos = items.stream()
                .map(item -> new OrderCreatedEvent.ItemInfo(
                        item.productId(),
                        item.productName(),
                        item.manufacturer(),
                        item.categoryId(),
                        item.quantity(),
                        item.pricePerUnit()
                )).collect(Collectors.toList());

        return OrderCreatedEvent.builder()
                .orderId(order.getId().toString())
                .reference(order.getReference())
                .orderDate(order.getCreatedDate())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod().name())
                .customer(new OrderCreatedEvent.CustomerInfo(
                        customer.id(),
                        customer.name() + " " + customer.surname(),
                        customer.email()
                ))
                .shippingAddress(new OrderCreatedEvent.AddressInfo(
                        activeShippingAddress.contactName(),
                        activeShippingAddress.city(),
                        activeShippingAddress.country(),
                        activeShippingAddress.address(),
                        activeShippingAddress.zipCode()
                ))
                .billingAddress(new OrderCreatedEvent.AddressInfo(
                        activeBillingAddress.contactName(),
                        activeBillingAddress.city(),
                        activeBillingAddress.country(),
                        activeBillingAddress.address(),
                        activeBillingAddress.zipCode()
                ))
                .items(itemInfos)
                .build();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No order found with the provided ID: %d", orderId)
                ));
    }

}