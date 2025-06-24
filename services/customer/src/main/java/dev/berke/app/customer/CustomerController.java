package dev.berke.app.customer;

import dev.berke.app.address.AddressRequest;
import dev.berke.app.address.AddressResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerCreateResponse> createCustomer(
            @RequestBody @Valid CustomerDataRequest customerDataRequest
    ) {
        try {
            CustomerCreateResponse response = customerService.createCustomer(customerDataRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> updateCustomer(
            @RequestBody @Valid CustomerUpdateRequest customerUpdateRequest
    ) {
        CustomerResponse updatedCustomer = customerService.updateCustomer(customerUpdateRequest);
        return ResponseEntity.accepted().body(updatedCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/exists/{customerId}")
    public ResponseEntity<Boolean> checkCustomerById(@PathVariable("customerId") String customerId) {
        return ResponseEntity.ok(customerService.checkCustomerById(customerId));
    }

    @GetMapping("{customerId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        // @SuppressWarnings("unchecked")
        // Map<String, String> details = (Map<String, String>) authentication.getDetails();
        // String customerId = details.get("customerId");

        String customerId = customerIdPrincipal;
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{customerId}/billing-addresses")
    public ResponseEntity<AddressResponse> addBillingAddress(
            @PathVariable("customerId") String customerId,
            @RequestBody @Valid AddressRequest addressRequest
    ) {
        var response = customerService.addBillingAddress(customerId, addressRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{customerId}/shipping-addresses")
    public ResponseEntity<AddressResponse> addShippingAddress(
            @PathVariable("customerId") String customerId,
            @RequestBody @Valid AddressRequest addressRequest
    ) {
        var response = customerService.addShippingAddress(customerId, addressRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}/billing-addresses")
    public ResponseEntity<List<AddressResponse>> getBillingAddressesByCustomerId(
            @PathVariable("customerId") String customerId
    ) {
        var responses = customerService.getBillingAddressesByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{customerId}/shipping-addresses")
    public ResponseEntity<List<AddressResponse>> getShippingAddressesByCustomerId(
            @PathVariable("customerId") String customerId
    ) {
        var responses = customerService.getShippingAddressesByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{customerId}/billing-addresses/active/{billingAddressId}")
    public ResponseEntity<String> chooseActiveBillingAddress(
            @PathVariable("customerId") String customerId,
            @PathVariable("billingAddressId") String billingAddressId
    ) {
        customerService.chooseActiveBillingAddress(customerId, billingAddressId);
        return ResponseEntity.ok("Active billing address changed");
    }

    @PutMapping("/{customerId}/shipping-addresses/active/{shippingAddressId}")
    public ResponseEntity<String> chooseActiveShippingAddress(
            @PathVariable("customerId") String customerId,
            @PathVariable("shippingAddressId") String shippingAddressId
    ) {
        customerService.chooseActiveShippingAddress(customerId, shippingAddressId);
        return ResponseEntity.ok("Active shipping address changed");
    }

    @GetMapping("/{customerId}/billing-addresses/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressResponse> getActiveBillingAddress(
            @PathVariable("customerId") String customerId
    ) {
        var response = customerService.getActiveBillingAddress(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}/shipping-addresses/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressResponse> getActiveShippingAddress(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;
        var response = customerService.getActiveShippingAddress(customerId);
        return ResponseEntity.ok(response);
    }

}