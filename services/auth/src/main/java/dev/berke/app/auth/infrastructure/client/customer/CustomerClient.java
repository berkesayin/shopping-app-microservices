package dev.berke.app.auth.infrastructure.client.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "customer-service", path = "/api/v1/customers")
public interface CustomerClient {

    @PostMapping()
    ResponseEntity<CustomerCreateResponse> createCustomer(@RequestBody CustomerDataRequest customerDataRequest);
}