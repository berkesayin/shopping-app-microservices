package dev.berke.app.payment;

import dev.berke.app.basket.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "payment-service", url = "${application.config.payment-url}")
public interface PaymentClient {

    @PostMapping("/create-iyzipayment")
    PaymentResponse createPayment(
            @RequestParam("customerId") String customerId);
}
