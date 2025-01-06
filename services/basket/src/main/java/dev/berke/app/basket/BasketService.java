package dev.berke.app.basket;

import dev.berke.app.customer.CustomerClient;
import dev.berke.app.product.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
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

    public BasketResponse getBasketByCustomerId(String customerId) {
        Optional<Basket> basket = basketRepository.findByCustomerId(customerId);
        return basket.map(value -> new BasketResponse(
                    value.getCustomerId(),
                    value.getItems()
                ))
                .orElse(null);
    }

    public BasketResponse addItemToBasket(AddItemToBasketRequest addItemToBasketRequest) {
        String customerId = addItemToBasketRequest.customerId();

        // 1. Retrieve existing basket
        Basket basket = basketRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Basket not found for customer id: " + customerId));

        // 2. Transform new items
        List<BasketItem> newBasketItems = addItemToBasketRequest.items().stream()
                .map(basketItemRequest -> {
                    ProductResponse productResponse = productClient
                            .getProductById(basketItemRequest.productId());

                    return new BasketItem(
                            productResponse.id(),
                            productResponse.name(),
                            productResponse.categoryId(),
                            ItemType.PHYSICAL,
                            productResponse.price(),
                            basketItemRequest.quantity()
                    );
                }).collect(Collectors.toList());

        // 3. Add/Update items
        List<BasketItem> existingItems = basket.getItems() != null ? basket.getItems() : new ArrayList<>();
        for(BasketItem newBasketItem : newBasketItems){
            boolean itemExists = false;
            for(BasketItem existingItem : existingItems){
                if(existingItem.getProductId().equals(newBasketItem.getProductId())){
                    existingItem.setQuantity(existingItem.getQuantity() + newBasketItem.getQuantity());
                    itemExists = true;
                    break;
                }
            }
            if(!itemExists){
                existingItems.add(newBasketItem);
            }
        }

        basket.setItems(existingItems);
        Basket updatedBasket = basketRepository.save(basket);

        return new BasketResponse(updatedBasket.getCustomerId(), updatedBasket.getItems());
    }

    public BasketTotalPriceResponse calculateTotalBasketPrice(
            String customerId
    ) {
        Basket basket = basketRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Basket not found for customer id: " + customerId));

        BigDecimal totalPrice = basket.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BasketTotalPriceResponse(customerId, totalPrice);
    }
}
