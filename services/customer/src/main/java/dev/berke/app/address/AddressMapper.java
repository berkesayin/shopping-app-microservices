package dev.berke.app.address;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AddressMapper {

    public Address toAddress(AddressRequest addressRequest) {
        if(addressRequest == null) {
            return null;
        }
        return Address.builder()
                .id(UUID.randomUUID().toString())  // generate new id
                .contactName(addressRequest.contactName())
                .city(addressRequest.city())
                .country(addressRequest.country())
                .address(addressRequest.address())
                .zipCode(addressRequest.zipCode())
                .isActive(addressRequest.isActive())
                .build();
    }

    public AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressResponse(
                address.getId(),
                address.getContactName(),
                address.getCity(),
                address.getCountry(),
                address.getAddress(),
                address.getZipCode()
        );
    }

}