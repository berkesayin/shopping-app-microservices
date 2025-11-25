package dev.berke.app.order.infrastructure.client.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service", path = "/api/v1/payments")
public interface PaymentClient {

    @PostMapping("/iyzi-payment")
    PaymentResponse createPayment();
}