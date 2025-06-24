package dev.berke.app.basket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "basket-service", url = "${application.config.basket-url}")
public interface BasketClient {

    @GetMapping("/{customerId}")
    BasketResponse getBasketByCustomerId(@PathVariable("customerId") String customerId);

    @GetMapping("/{customerId}/total-price")
    BasketTotalPriceResponse getTotalBasketPrice(@PathVariable("customerId") String customerId);
}