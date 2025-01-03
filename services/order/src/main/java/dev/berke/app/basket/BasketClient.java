package dev.berke.app.basket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "basket-service", url = "${application.config.basket-url}")
public interface BasketClient {

    @PostMapping
    ResponseEntity<BasketResponse> createBasket(@RequestBody BasketRequest basketRequest);

    @PostMapping("/items")
    ResponseEntity<BasketResponse> addItemToBasket(@RequestBody AddItemToBasketRequest addItemToBasketRequest);

    @GetMapping("/{customer-id}/items")
    ResponseEntity<BasketTotalPriceResponse> calculateTotalBasketPrice(@PathVariable("customer-id") String customerId);
}
