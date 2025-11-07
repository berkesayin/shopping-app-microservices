package dev.berke.app.order.api;

import dev.berke.app.order.api.dto.OrderRequest;
import dev.berke.app.order.api.dto.OrderResponse;
import dev.berke.app.order.application.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;

        OrderResponse orderResponse = orderService.createOrder(orderRequest, customerId);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable("orderId") Integer orderId
    ) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

}
