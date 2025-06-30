package dev.berke.app.customer;

import dev.berke.app.address.AddressRequest;
import dev.berke.app.address.AddressResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomerResponse> updateProfile(
            @RequestBody @Valid CustomerUpdateRequest customerUpdateRequest,
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;

        CustomerResponse updatedCustomer =
                customerService.updateProfile(customerUpdateRequest, customerId);

        return ResponseEntity.accepted().body(updatedCustomer);
    }

    @GetMapping
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/exists/{customerId}")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<Boolean> checkCustomerById(
            @PathVariable("customerId") String customerId
    ) {
        return ResponseEntity.ok(customerService.checkCustomerById(customerId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomerResponse> getProfile(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;
        return ResponseEntity.ok(customerService.getProfile(customerId));
    }

    @GetMapping("{customerId}")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(customerService.getProfile(customerId));
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteProfile(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;
        customerService.deleteProfile(customerId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/me/billing-addresses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressResponse> addBillingAddress(
            @AuthenticationPrincipal String customerIdPrincipal,
            @RequestBody @Valid AddressRequest addressRequest
    ) {
        String customerId = customerIdPrincipal;
        var response = customerService.addBillingAddress(customerId, addressRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me/shipping-addresses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressResponse> addShippingAddress(
            @AuthenticationPrincipal String customerIdPrincipal,
            @RequestBody @Valid AddressRequest addressRequest
    ) {
        String customerId = customerIdPrincipal;
        var response = customerService.addShippingAddress(customerId, addressRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/billing-addresses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AddressResponse>> getBillingAddresses(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;
        var responses = customerService.getBillingAddresses(customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me/shipping-addresses")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AddressResponse>> getShippingAddresses(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;
        var responses = customerService.getShippingAddresses(customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{customerId}/billing-addresses")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<List<AddressResponse>> getBillingAddressesByCustomerId(
            @PathVariable String customerId
    ) {
        var responses = customerService.getBillingAddresses(customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{customerId}/shipping-addresses")
    @PreAuthorize("hasRole('BACKOFFICE')")
    public ResponseEntity<List<AddressResponse>> getShippingAddressesByCustomerId(
            @PathVariable String customerId
    ) {
        var responses = customerService.getShippingAddresses(customerId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/me/billing-addresses/{billingAddressId}/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> setActiveBillingAddress(
            @AuthenticationPrincipal String customerIdPrincipal,
            @PathVariable("billingAddressId") String billingAddressId
    ) {
        String customerId = customerIdPrincipal;
        customerService.setActiveBillingAddress(customerId, billingAddressId);
        return ResponseEntity.ok("Active billing address has been set.");
    }

    @PutMapping("/me/shipping-addresses/{shippingAddressId}/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> setActiveShippingAddress(
            @AuthenticationPrincipal String customerIdPrincipal,
            @PathVariable("shippingAddressId") String shippingAddressId
    ) {
        String customerId = customerIdPrincipal;
        customerService.setActiveShippingAddress(customerId, shippingAddressId);
        return ResponseEntity.ok("Active shipping address has been set.");
    }

    @GetMapping("/me/billing-addresses/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressResponse> getActiveBillingAddress(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;
        var response = customerService.getActiveBillingAddress(customerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/shipping-addresses/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressResponse> getActiveShippingAddress(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;
        var response = customerService.getActiveShippingAddress(customerId);
        return ResponseEntity.ok(response);
    }

}