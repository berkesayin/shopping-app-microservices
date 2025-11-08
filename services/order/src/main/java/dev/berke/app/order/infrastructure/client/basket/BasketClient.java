package dev.berke.app.order.infrastructure.client.basket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@FeignClient(name = "basket-service", url = "${application.config.basket-url}")
public interface BasketClient {

    @GetMapping("/me")
    Optional<BasketResponse> getBasket();

    @GetMapping("/total-price")
    ResponseEntity<BasketTotalPriceResponse> getTotalBasketPrice();
}