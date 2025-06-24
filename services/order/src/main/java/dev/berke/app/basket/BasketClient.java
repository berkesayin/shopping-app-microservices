package dev.berke.app.basket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "basket-service", url = "${application.config.basket-url}")
public interface BasketClient {

    @GetMapping("/{customerId}")
    Optional<BasketResponse> getBasketByCustomerId(@PathVariable("customerId") String customerId);

    @GetMapping("/{customerId}/total-price")
    ResponseEntity<BasketTotalPriceResponse> calculateTotalBasketPrice(@PathVariable("customerId") String customerId);
}