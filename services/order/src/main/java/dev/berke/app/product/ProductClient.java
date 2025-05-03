package dev.berke.app.product;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-service", url = "${application.config.product-url}")
public interface ProductClient {

}