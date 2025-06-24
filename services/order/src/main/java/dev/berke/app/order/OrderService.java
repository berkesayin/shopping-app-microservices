package dev.berke.app.order;

import dev.berke.app.basket.*;
import dev.berke.app.constants.OrderConstants;
import dev.berke.app.customer.CustomerClient;
import dev.berke.app.exception.BusinessException;
import dev.berke.app.kafka.OrderConfirmRequest;
import dev.berke.app.kafka.OrderProducer;
import dev.berke.app.orderline.OrderLineRequest;
import dev.berke.app.orderline.OrderLineService;
import dev.berke.app.payment.PaymentClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineService orderLineService;
    private final OrderMapper orderMapper;
    private final CustomerClient customerClient;
    private final BasketClient basketClient;
    private final PaymentClient paymentClient;
    private final OrderProducer orderProducer;

    // Business logic to create order
    // 1. check the customer: check if we have our customer or not (with OpenFeign)
    // 2. check customer's basket: if empty, tell customer basket is empty, and can not create order
    // 3. calculate total basket price
    // 4. persist order: save the order object
    // 5. persist order lines: Persist (save) the order lines (like purchasing or saving order lines)
    // 6. start payment process: after persisting the order lines, start payment process.
    // Use payment service -> IyziPayment.java -> createPaymentRequest() with async method
    // Payment service handles sending payment confirmation to notification service
    // 7. send order confirm.: if payment is successful, send order confirm. to notification service (with kafka)
    // 8. send order details to order index at elasticsearch

    public OrderResponse createOrder(
            OrderRequest orderRequest,
            String customerId
    ) {
        // 1. check the customer: check if we have our customer or not
        var customer = this.customerClient.getCustomerById(customerId)
                .orElseThrow(() -> new BusinessException(
                        OrderConstants.CUSTOMER_NOT_FOUND_MESSAGE));

        var customerName = customer.name() + " " + customer.surname();
        System.out.println("Customer name: " + customerName);

        // 2. check customer's basket: check if customer added products to basket or not
        var basket = this.basketClient.getBasketByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException(
                        OrderConstants.BASKET_EMPTY_MESSAGE));

        var productsToPurchase = basket.items();
        System.out.println("Step 2: " + basket.customerId() + productsToPurchase);

        // 3. calculate total basket price
        ResponseEntity<BasketTotalPriceResponse> basketTotalPriceResponse =
                basketClient.calculateTotalBasketPrice(customerId);

        BigDecimal totalPrice = basketTotalPriceResponse.getBody().totalPrice();
        System.out.println("Step 4: " + totalPrice);

        // 4. save the order object
        var order = orderMapper.toOrder(orderRequest, customerId);
        order.setCustomerEmail(customer.email());
        order.setTotalAmount(totalPrice);

        var savedOrder = this.orderRepository.save(order);

        System.out.println("Step 5: " + order.getCustomerId() + order.getPaymentMethod());

        // 5. save the order lines (products purchased by customer)
        for (BasketItem basketItem : basket.items()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            savedOrder.getId(),
                            basketItem.getProductId(),
                            basketItem.getQuantity()
                    )
            );
        }

        // 6. start payment process: after saving the order lines, start payment
        paymentClient.createPayment(customer.id());

        // 7. send order confirmation
        orderProducer.sendOrderConfirmation(
                new OrderConfirmRequest(
                        customerName,
                        customer.email(),
                        orderRequest.reference(),
                        orderRequest.paymentMethod(),
                        productsToPurchase,
                        totalPrice
                )
        );

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getReference()
        );
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