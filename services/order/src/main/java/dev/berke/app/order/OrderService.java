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
import dev.berke.app.payment.PaymentRequest;
import dev.berke.app.product.ProductClient;
import dev.berke.app.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        // 1. check the customer: Check if we have our customer or not (use OpenFeign)
        // 2. create basket for customer: createBasket method at basket service
        // 3. add items to basket for customer: addItemToBasket method at basket service
        // 4. calculate total price of basket
        // 5. confirm basket for customer: create a new method at BasketController and
        // BasketService, which
        // will confirm the products to be bought for customer
        // 6. persist order: After confirming basket for customer, save the order object
        // 7. persist order lines: Persist (save) the order lines (like purchasing or
        // saving order lines)
        // 8. start payment process: After persisting the order lines, start payment
        // process. Use
        // payment service -> IyziPayment.java -> createPaymentRequest(). If sync
        // communication needed, use
        // OpenFeign to send request from order to payment service
        // 9. send the order confirmation: If payment is successfully processed, send
        // the order confirmation
        // to the notification microservice (use kafka)

        public Integer createOrder(
                        OrderRequest orderRequest) {
                // 1. check the customer: Check if we have our customer or not (use OpenFeign)
                var customer = this.customerClient.getCustomerById(orderRequest.customerId())
                                .orElseThrow(() -> new BusinessException(
                                                OrderConstants.CUSTOMER_NOT_FOUND_MESSAGE));

                // 2. create basket
                BasketRequest basketRequest = new BasketRequest(
                                orderRequest.customerId(),
                                orderRequest.basketItemRequests());

                // 3. add items to basket
                AddItemToBasketRequest addItemToBasketRequest = new AddItemToBasketRequest(
                                orderRequest.customerId(),
                                orderRequest.basketItemRequests());

                ResponseEntity<BasketResponse> addItemToBasketResponse = basketClient
                                .addItemToBasket(addItemToBasketRequest);

                System.out.println("Items added to basket: " + addItemToBasketResponse);
                ResponseEntity<BasketResponse> basketResponse = basketClient.createBasket(basketRequest);

                // 4. calculate total basket price
                ResponseEntity<BasketTotalPriceResponse> basketTotalPriceResponse = basketClient
                                .calculateTotalBasketPrice(orderRequest.customerId());

                Double totalPrice = basketTotalPriceResponse.getBody().totalPrice();

                // 2. purchase the products: Purchase the products using the product
                // microservice (use RestTemplate)
                var purchasedProducts = this.productClient.purchaseProducts(orderRequest.products());

                // 3. persist order: After purchasing the product, save the order object
                var order = this.orderRepository.save(orderMapper.toOrder(orderRequest));

                // 4. persist order lines: Persist (save) the order lines (like purchasing or
                // saving order lines)
                for (PurchaseRequest purchaseRequest : orderRequest.products()) {
                        orderLineService.saveOrderLine(
                                        new OrderLineRequest(
                                                        null, // id of the orderline
                                                        order.getId(),
                                                        purchaseRequest.productId(),
                                                        purchaseRequest.quantity()));
                }

                // 5. start payment process: After persisting the order lines, start payment
                // process
                var paymentRequest = new PaymentRequest(
                                orderRequest.amount(),
                                orderRequest.paymentMethod(),
                                order.getId(),
                                order.getReference(),
                                customer);

                paymentClient.requestOrderPayment(paymentRequest);

                // 6. send the order confirmation: Send the order confirmation message to the
                // kafka broker
                // and notification microservice will consume from there
                orderProducer.sendOrderConfirmation(
                                new OrderConfirmation(
                                                orderRequest.reference(),
                                                orderRequest.amount(),
                                                orderRequest.paymentMethod(),
                                                customer,
                                                purchasedProducts));

                return order.getId();
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
                                                String.format(
                                                                OrderConstants.ORDER_NOT_FOUND_ERROR_MESSAGE
                                                                                + orderId)));
        }
}

// 2. create basket for customer: createBasket method at basket service

// 3. add items to basket for customer: addItemToBasket method at basket service

// 4. confirm basket for customer: create a new method at BasketController and
// BasketService, which
// will confirm the products to be bought for customer

// 5. persist order: After confirming basket for customer, save the order object

// 6. persist order lines: Persist (save) the order lines (like purchasing or
// saving order lines)
