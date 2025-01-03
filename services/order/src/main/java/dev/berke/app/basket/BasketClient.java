package dev.berke.app.basket;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "basket-service",
        url = "${application.config.basket-url}"
)
public interface BasketClient {

    @PostMapping
    ResponseEntity<BasketResponse> createBasket(@RequestBody BasketRequest basketRequest);

    @PostMapping("/items")
    ResponseEntity<BasketResponse> addItemToBasket(@RequestBody AddItemToBasketRequest addItemToBasketRequest);
}
