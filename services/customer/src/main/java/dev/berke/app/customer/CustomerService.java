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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    @Transactional
    public CustomerCreateResponse createCustomer(CustomerDataRequest customerDataRequest) {
        var customer = customerMapper.toCustomer(customerDataRequest);
        Customer savedCustomer = customerRepository.save(customer);

        System.out.println("Customer profile created with ID: "
                + savedCustomer.getId() + " for email: "
                + savedCustomer.getEmail());

        return new CustomerCreateResponse(savedCustomer.getId());
    }

    public CustomerResponse updateCustomer(CustomerUpdateRequest customerUpdateRequest) {
        var customer = customerRepository.findById(customerUpdateRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format(CustomerConstants
                                .CUSTOMER_NOT_FOUND_MESSAGE, customerUpdateRequest.id())
                ));
        if (StringUtils.isNotBlank(customerUpdateRequest.name())) {
            customer.setName(customerUpdateRequest.name());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.surname())) {
            customer.setSurname(customerUpdateRequest.surname());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.gsmNumber())) {
            customer.setGsmNumber(customerUpdateRequest.gsmNumber());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.email())) {
            customer.setEmail(customerUpdateRequest.email());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.password())) {
            customer.setPassword(customerUpdateRequest.password());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.identityNumber())) {
            customer.setIdentityNumber(customerUpdateRequest.identityNumber());
        }
        if (StringUtils.isNotBlank(customerUpdateRequest.registrationAddress())) {
            customer.setRegistrationAddress(customerUpdateRequest.registrationAddress());
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

    public CustomerResponse getCustomerById(String customerId) {
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

        if(customer.getBillingAddresses() == null) {
            customer.setBillingAddresses(new ArrayList<>());
        }
        customer.getBillingAddresses().add(newAddress);

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

        if(customer.getShippingAddresses() == null) {
            customer.setShippingAddresses(new ArrayList<>());
        }
        customer.getShippingAddresses().add(newAddress);

        if(Boolean.TRUE.equals(addressRequest.isActive())) {
            customer.setActiveShippingAddressId(newAddress.getId());
        }

        customerRepository.save(customer);
        return addressMapper.toAddressResponse(newAddress);
    }

    public List<AddressResponse> getBillingAddressesByCustomerId(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        var billingAddresses = customer.getBillingAddresses()
                != null ? customer.getBillingAddresses() : List.of();

        return billingAddresses.stream()
                .map(address -> addressMapper.toAddressResponse((Address) address))
                .collect(Collectors.toList());
    }

    public List<AddressResponse> getShippingAddressesByCustomerId(String customerId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        var shippingAddresses = customer.getShippingAddresses()
                != null ? customer.getShippingAddresses() : List.of();

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