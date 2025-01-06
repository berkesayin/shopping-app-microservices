package dev.berke.app.basket;

import dev.berke.app.customer.CustomerClient;
import dev.berke.app.product.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public BasketResponse createBasket(BasketRequest basketRequest) {

        // 1. Validate if customer exists
        CustomerResponse customerResponse = customerClient.getCustomerById(basketRequest.customerId());

        // 2. Transform items
        List<BasketItem> basketItems = basketRequest.items().stream()
                .map(basketItemRequest -> {
                    ProductResponse productResponse = productClient.getProductById(basketItemRequest.productId());

                    return new BasketItem(
                            productResponse.id(),
                            productResponse.name(),
                            productResponse.categoryId(),
                            ItemType.PHYSICAL,
                            productResponse.price(),
                            basketItemRequest.quantity()
                    );
                }).collect(Collectors.toList());

        // 3. Create basket
        Basket basket = new Basket(customerResponse.id(), basketItems);
        Basket savedBasket = basketRepository.save(basket);
        return new BasketResponse(savedBasket.getCustomerId(), savedBasket.getItems());
    }
}
