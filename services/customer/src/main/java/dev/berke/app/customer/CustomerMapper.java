package dev.berke.app.customer;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public Customer toCustomer(CustomerRequest customerRequest) {
        if (customerRequest == null) {
            return null;
        }

        return Customer.builder()
                .id(customerRequest.id())
                .name(customerRequest.name())
                .surname(customerRequest.surname())
                .gsmNumber(customerRequest.gsmNumber())
                .email(customerRequest.email())
                .password(customerRequest.password()) // added mapping
                .identityNumber(customerRequest.identityNumber())
                .registrationAddress(customerRequest.registrationAddress())
                .city(customerRequest.city())
                .country(customerRequest.country())
                .zipCode(customerRequest.zipCode())
                .billingAddresses(customerRequest.billingAddresses())
                .shippingAddresses(customerRequest.shippingAddresses())
                .activeBillingAddressId(customerRequest.activeBillingAddressId()) // updated mapping
                .activeShippingAddressId(customerRequest.activeShippingAddressId()) // updated mapping
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customer.getGsmNumber(),
                customer.getEmail(),
                customer.getIdentityNumber(),
                customer.getRegistrationAddress(),
                customer.getCity(),
                customer.getCountry(),
                customer.getZipCode(),
                customer.getBillingAddresses(),
                customer.getShippingAddresses(),
                customer.getActiveBillingAddressId(), // updated mapping
                customer.getActiveShippingAddressId()  // updated mapping
        );
    }
}