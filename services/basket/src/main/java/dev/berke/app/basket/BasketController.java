package dev.berke.app.basket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/baskets")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping
    public ResponseEntity<BasketResponse> createBasket(
            @RequestBody BasketRequest basketRequest
    ) {
        BasketResponse basketResponse = basketService.createBasket(basketRequest);
        return new ResponseEntity<>(basketResponse, HttpStatus.CREATED);
    }
}
