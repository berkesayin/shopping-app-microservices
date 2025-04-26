package dev.berke.app.customer;

import dev.berke.app.address.Address;
import dev.berke.app.address.AddressMapper;
import dev.berke.app.address.AddressRequest;
import dev.berke.app.address.AddressResponse;
import dev.berke.app.constants.CustomerConstants;
import dev.berke.app.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    public String createCustomer(CustomerCreateRequest customerCreateRequest) {
        // Check for existing email
        if(customerRepository.existsByEmail(customerCreateRequest.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        var customer = customerRepository.save(customerMapper.toCustomer(customerCreateRequest));
        return customer.getId();
    }

    public CustomerResponse updateCustomer(CustomerUpdateRequest updateRequest) {
        var customer = customerRepository.findById(updateRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format(CustomerConstants.CUSTOMER_NOT_FOUND_MESSAGE, updateRequest.id())
                ));
        if (StringUtils.isNotBlank(updateRequest.name())) {
            customer.setName(updateRequest.name());
        }
        if (StringUtils.isNotBlank(updateRequest.surname())) {
            customer.setSurname(updateRequest.surname());
        }
        if (StringUtils.isNotBlank(updateRequest.gsmNumber())) {
            customer.setGsmNumber(updateRequest.gsmNumber());
        }
        if (StringUtils.isNotBlank(updateRequest.email())) {
            customer.setEmail(updateRequest.email());
        }
        if (StringUtils.isNotBlank(updateRequest.password())) {
            customer.setPassword(updateRequest.password());
        }
        if (StringUtils.isNotBlank(updateRequest.identityNumber())) {
            customer.setIdentityNumber(updateRequest.identityNumber());
        }
        if (StringUtils.isNotBlank(updateRequest.registrationAddress())) {
            customer.setRegistrationAddress(updateRequest.registrationAddress());
        }
        customerRepository.save(customer);
        return customerMapper.fromCustomer(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean checkCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .isPresent();
    }

    public CustomerResponse findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format(CustomerConstants.CUSTOMER_NOT_FOUND_BY_ID, customerId)));
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    public AddressResponse addBillingAddress(String customerId, AddressRequest addressRequest) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        var newAddress = addressMapper.toAddress(addressRequest);
        // Append new billing address
        if(customer.getBillingAddresses() == null) {
            customer.setBillingAddresses(new ArrayList<>());
        }
        customer.getBillingAddresses().add(newAddress);
        // If marked active, update customer's active billing address id
        if(Boolean.TRUE.equals(addressRequest.isActive())) {
            customer.setActiveBillingAddressId(newAddress.getId());
        }
        customerRepository.save(customer);
        return addressMapper.toAddressResponse(newAddress);
    }

    public AddressResponse addShippingAddress(String customerId, AddressRequest addressRequest) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        var newAddress = addressMapper.toAddress(addressRequest);

        // Append new shipping address
        if(customer.getShippingAddresses() == null) {
            customer.setShippingAddresses(new ArrayList<>());
        }
        customer.getShippingAddresses().add(newAddress);

        // If marked active, update customer's active shipping address id
        if(Boolean.TRUE.equals(addressRequest.isActive())) {
            customer.setActiveShippingAddressId(newAddress.getId());
        }

        customerRepository.save(customer);
        return addressMapper.toAddressResponse(newAddress);
    }

    public List<AddressResponse> getBillingAddressesByCustomerId(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        var billingAddresses = customer.getBillingAddresses() != null ? customer.getBillingAddresses() : List.of();
        return billingAddresses.stream()
                .map(address -> addressMapper.toAddressResponse((Address) address))
                .collect(Collectors.toList());
    }

    public List<AddressResponse> getShippingAddressesByCustomerId(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        var shippingAddresses = customer.getShippingAddresses() != null ? customer.getShippingAddresses() : List.of();

        return shippingAddresses.stream()
                .map(address -> addressMapper.toAddressResponse((Address) address))
                .collect(Collectors.toList());
    }

    public void chooseActiveBillingAddress(String customerId, String billingAddressId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        var billingAddresses = customer.getBillingAddresses();

        if (billingAddresses == null || billingAddresses.isEmpty()) {
            throw new IllegalArgumentException("No billing addresses found for customer: " + customerId);
        }
        boolean found = false;

        for (Address address : billingAddresses) {
            if (address.getId().equals(billingAddressId)) {
                address.setIsActive(true);
                found = true;
            } else {
                address.setIsActive(false);
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Billing address not found for customer: " + customerId);
        }
        customer.setActiveBillingAddressId(billingAddressId);
        customerRepository.save(customer);
    }

    public void chooseActiveShippingAddress(String customerId, String shippingAddressId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        var shippingAddresses = customer.getShippingAddresses();
        if (shippingAddresses == null || shippingAddresses.isEmpty()) {
            throw new IllegalArgumentException("No shipping addresses found for customer: " + customerId);
        }
        boolean found = false;
        for (Address address : shippingAddresses) {
            if (address.getId().equals(shippingAddressId)) {
                address.setIsActive(true);
                found = true;
            } else {
                address.setIsActive(false);
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Shipping address not found for customer: " + customerId);
        }
        customer.setActiveShippingAddressId(shippingAddressId);
        customerRepository.save(customer);
    }

    public AddressResponse getActiveBillingAddress(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        String activeId = customer.getActiveBillingAddressId();
        if (activeId == null) {
            throw new IllegalArgumentException("No active billing address set for customer: " + customerId);
        }
        var activeAddress = customer.getBillingAddresses().stream()
                .filter(address -> address.getId().equals(activeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Active billing address not found"));
        return addressMapper.toAddressResponse(activeAddress);
    }

    public AddressResponse getActiveShippingAddress(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        String activeId = customer.getActiveShippingAddressId();
        if (activeId == null) {
            throw new IllegalArgumentException("No active shipping address set for customer: " + customerId);
        }
        var activeAddress = customer.getShippingAddresses().stream()
                .filter(address -> address.getId().equals(activeId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Active shipping address not found"));
        return addressMapper.toAddressResponse(activeAddress);
    }

}