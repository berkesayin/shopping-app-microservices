package dev.berke.app.order;

import dev.berke.app.basket.*;
import dev.berke.app.constants.OrderConstants;
import dev.berke.app.customer.CustomerClient;
import dev.berke.app.exception.BusinessException;
// import dev.berke.app.kafka.OrderProducer;
import dev.berke.app.orderline.OrderLineRequest;
import dev.berke.app.orderline.OrderLineService;
import dev.berke.app.payment.PaymentClient;
import dev.berke.app.product.ProductClient;
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
    private final ProductClient productClient;
    private final BasketClient basketClient;
    private final PaymentClient paymentClient;
    // private final OrderProducer orderProducer;

    // Business logic to create order
    // 1. check the customer: Check if we have our customer or not (use OpenFeign)
    // 2. check customer's basket: If empty, tell customer basket is empty, and can not create order
    // 3. calculate total basket price
    // 4. persist order: save the order object
    // 5. persist order lines: Persist (save) the order lines (like purchasing or saving order lines)
    // 6. start payment process: After persisting the order lines, start payment process.
    // Use payment service -> IyziPayment.java -> createPaymentRequest() with async method
    // 7. send the order confirmation: If payment is successfully processed, send order confirmation
    // to notification microservice (use kafka)

    public OrderResponse createOrder(
            OrderRequest orderRequest
    ) {
        // 1. check the customer: check if we have our customer or not
        var customer = this.customerClient.getCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException(
                        OrderConstants.CUSTOMER_NOT_FOUND_MESSAGE));

        System.out.println("Step 1: " + customer.id() + customer.name());

        // 2. check customer's basket: check if customer added products to basket or not
        var basket = this.basketClient.getBasketByCustomerId(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException(
                        OrderConstants.BASKET_EMPTY_MESSAGE));

        System.out.println("Step 2: " + basket.customerId() + basket.items());

        // 3. calculate total basket price
        ResponseEntity<BasketTotalPriceResponse> basketTotalPriceResponse =
                basketClient.calculateTotalBasketPrice(orderRequest.customerId());

        BigDecimal totalPrice = basketTotalPriceResponse.getBody().totalPrice();

        System.out.println("Step 4: " + totalPrice);

        // 4. persist order save the order object
        var order = orderMapper.toOrder(orderRequest);
        order.setTotalAmount(totalPrice);

        var savedOrder = this.orderRepository.save(order);

        System.out.println("Step 5: " + order.getCustomerId() + order.getPaymentMethod());

        // 5. persist order lines: Save the order lines
        for (BasketItem basketItem : basket.items()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            savedOrder.getId(),
                            basketItem.getProductId(),
                            basketItem.getQuantity()));
        }

        // 6. start payment process: After persisting the order lines, start payment
        paymentClient.createPayment(customer.id());

        // 7. send order confirmation



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