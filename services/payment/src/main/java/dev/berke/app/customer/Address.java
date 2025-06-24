package dev.berke.app.customer;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class Address {

    private String id;
    private String contactName;
    private String city;
    private String country;
    private String address;
    private String zipCode;
    private Boolean isActive;
}