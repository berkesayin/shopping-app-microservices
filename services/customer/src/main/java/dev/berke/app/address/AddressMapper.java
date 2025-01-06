package dev.berke.app.address;

import dev.berke.app.customer.Customer;
import dev.berke.app.customer.CustomerRequest;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {

    public Address toAddress(AddressRequest addressRequest) {
        if(addressRequest == null) {
            return null;
        }
        return Address.builder()
                .contactName(addressRequest.contactName())
                .city(addressRequest.city())
                .country(addressRequest.country())
                .address(addressRequest.address())
                .zipCode(addressRequest.zipCode())
                .build();
    }

    public AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressResponse(
                address.getContactName(),
                address.getCity(),
                address.getCountry(),
                address.getAddress(),
                address.getZipCode()
        );
    }

}