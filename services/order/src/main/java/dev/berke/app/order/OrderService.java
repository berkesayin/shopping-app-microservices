package dev.berke.app.order;

import dev.berke.app.constants.OrderConstants;
import dev.berke.app.customer.CustomerClient;
import dev.berke.app.exception.BusinessException;
import dev.berke.app.orderline.OrderLineRequest;
import dev.berke.app.orderline.OrderLineService;
import dev.berke.app.product.ProductClient;
import dev.berke.app.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;

    // Business logic to create order
    // 1. check the customer: Check if we have our customer or not (use OpenFeign)
    // 2. purchase the products: Purchase the products using the product microservice (use RestTemplate)
    // 3. persist order: After purchasing the product, save the order object
    // 4. persist order lines: Persist (save) the order lines (like purchasing or saving order lines)
    // 5. start payment process: After persisting the order lines, start payment process
    // 6. send the order confirmation: Send the order confirmation to the notification microservice (use kafka)
    // (send a message to Kafka broker)

    public Integer createOrder(OrderRequest orderRequest) {

        // 1. check the customer: Check if we have our customer or not (use OpenFeign)
        var customer = this.customerClient.getCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException(
                        OrderConstants.CUSTOMER_NOT_FOUND_MESSAGE
                ));

        // 2. purchase the products: Purchase the products using the product microservice (use RestTemplate)
        var purchasedProducts = this.productClient.purchaseProducts(orderRequest.products());

        // 3. persist order: After purchasing the product, save the order object
        var order = this.orderRepository.save(orderMapper.toOrder(orderRequest));

        // 4. persist order lines: Persist (save) the order lines (like purchasing or saving order lines)
        for (PurchaseRequest purchaseRequest: orderRequest.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null, // id of the orderline
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        return order.getId();

        // 5. start payment process: After persisting the order lines, start payment process

        // 6. send the order confirmation: Send the order confirmation to the notification microservice (use kafka)
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
                                OrderConstants.ORDER_NOT_FOUND_ERROR_MESSAGE + orderId
                        )));
    }
}
