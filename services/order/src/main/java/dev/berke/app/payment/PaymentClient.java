package dev.berke.app.payment;

import dev.berke.app.basket.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service", url = "${application.config.payment-url}")
public interface PaymentClient {

    @PostMapping("/iyzi-payment")
    PaymentResponse createPayment();
}