package dev.berke.app.order;

import dev.berke.app.basket.*;
import dev.berke.app.constants.OrderConstants;
import dev.berke.app.customer.CustomerClient;
import dev.berke.app.exception.BusinessException;
import dev.berke.app.kafka.OrderConfirmation;
import dev.berke.app.kafka.OrderProducer;
import dev.berke.app.orderline.OrderLineRequest;
import dev.berke.app.orderline.OrderLineService;
import dev.berke.app.payment.PaymentClient;
import dev.berke.app.product.ProductClient;
import dev.berke.app.product.PurchaseRequest;
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
        private final OrderProducer orderProducer;

        // Business logic to create order
        // 1. check the customer: Check if we have our customer or not (use OpenFeign)
        // 2. purchase the products: Purchase the products using the product
        // microservice (use RestTemplate)
        // 3. persist order: After purchasing the product, save the order object
        // 4. persist order lines: Persist (save) the order lines (like purchasing or
        // saving order lines)
        // 5. start payment process: After persisting the order lines, start payment
        // process
        // 6. send the order confirmation: Send the order confirmation to the
        // notification microservice (use kafka)
        // (send a message to Kafka broker)

        // Updated business logic to create order
        // 1. check the customer: Check if we have our customer or not (use OpenFeign)
        // 2. check customer's basket: If it is empty, tell customer his basket is
        // empty, and can not create order
        // 3. check each product's available quantity in the database
        // 4. calculate total basket price
        // 5. persist order: save the order object
        // 6. persist order lines: Persist (save) the order lines (like purchasing or
        // saving order lines)
        // 7. start payment process: After persisting the order lines, start payment
        // process. Use
        // payment service -> IyziPayment.java -> createPaymentRequest(). If sync
        // communication needed,
        // use OpenFeign to send request from order to payment service
        // 8. send the order confirmation: If payment is successfully processed, send
        // the order confirmation
        // to the notification microservice (use kafka)
        // 9. calculate the new available quantities for each product after order
        // confirmation

        public OrderResponse createOrder(OrderRequest orderRequest) {
                // 1. check the customer: check if we have our customer or not (use OpenFeign)
                var customer = this.customerClient.getCustomerById(orderRequest.customerId())
                                .orElseThrow(() -> new BusinessException(
                                                OrderConstants.CUSTOMER_NOT_FOUND_MESSAGE));
                System.out.println("Step 1: " + customer.id() + customer.name());

                // 2. check customer's basket: check if customer added products to basket or not
                var basket = this.basketClient.getBasketByCustomerId(orderRequest.customerId())
                                .orElseThrow(() -> new BusinessException(
                                                OrderConstants.BASKET_EMPTY_MESSAGE));

                System.out.println("Step 2: " + basket.customerId() + basket.items());

                // 3. check each product's available quantity
                checkProductQuantities(basket);

                // 4. calculate total basket price
                ResponseEntity<BasketTotalPriceResponse> basketTotalPriceResponse = basketClient
                                .calculateTotalBasketPrice(orderRequest.customerId());

                BigDecimal totalPrice = basketTotalPriceResponse.getBody().totalPrice();

                System.out.println("Step 4: " + totalPrice);

                // 5. persist order save the order object
                var order = orderMapper.toOrder(orderRequest);
                order.setTotalAmount(totalPrice);
                var savedOrder = this.orderRepository.save(order);

                System.out.println("Step 5: " + order.getCustomerId() + order.getPaymentMethod());

                // 6. persist order lines: Persist (save) the order lines (like purchasing or
                // saving order lines)
                for (BasketItem basketItem : basket.items()) {
                        orderLineService.saveOrderLine(
                                        new OrderLineRequest(
                                                        null, // id of the orderline
                                                        savedOrder.getId(),
                                                        basketItem.getProductId(),
                                                        basketItem.getQuantity()));
                }

                // 7. start payment process: After persisting the order lines, start payment
                paymentClient.createPayment(
                                customer.id()
                // orderRequest.amount()
                );

                return new OrderResponse(savedOrder.getId(), savedOrder.getReference());
        }

        private void checkProductQuantities(
                        BasketResponse basket) {
                List<BasketItem> basketItems = basket.items();

                for (BasketItem item : basketItems) {
                        Integer availableQuantity = productClient
                                        .getAvailableQuantityByProductId(item.getProductId());

                        System.out.println("Step 3: " + availableQuantity);

                        if (item.getQuantity() > availableQuantity) {
                                throw new BusinessException(
                                                OrderConstants.PRODUCT_QUANTITY_NOT_AVAILABLE + item.getProductId());
                        }
                }
        }

        /*
         * 
         * public List < OrderResponse > getAllOrders() {
         * return orderRepository.findAll()
         * .stream()
         * .map(orderMapper::fromOrder)
         * .collect(Collectors.toList());
         * }
         * 
         * public OrderResponse getOrderById(Integer orderId) {
         * return orderRepository.findById(orderId)
         * .map(orderMapper::fromOrder)
         * .orElseThrow(() -> new EntityNotFoundException(
         * String.format(
         * OrderConstants.ORDER_NOT_FOUND_ERROR_MESSAGE +
         * orderId)));
         * }
         * 
         */
}
