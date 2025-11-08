package dev.berke.app.basket.infrastructure.client.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${application.config.product-url}")
public interface ProductClient {

    @GetMapping("/{product-id}")
    ProductResponse getProductById(@PathVariable("product-id") Integer productId);
}