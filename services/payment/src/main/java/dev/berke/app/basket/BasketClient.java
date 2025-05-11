package dev.berke.app.basket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "basket-service", url = "${application.config.basket-url}")
public interface BasketClient {

    @GetMapping("/{customer-id}")
    BasketResponse getBasketByCustomerId(@PathVariable("customer-id") String customerId);

    @GetMapping("/{customer-id}/total-price")
    BasketTotalPriceResponse getTotalBasketPrice(@PathVariable("customer-id") String customerId);
}