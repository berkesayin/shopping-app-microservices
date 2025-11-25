package dev.berke.app.payment.infrastructure.client.basket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "basket-service", path = "/api/v1/baskets")
public interface BasketClient {

    @GetMapping("/me")
    BasketResponse getBasket();

    @GetMapping("/total-price")
    BasketTotalPriceResponse getTotalBasketPrice();
}