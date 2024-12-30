package dev.berke.app.customer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class Address {

    // @Id
    // private String id;
    private String contactName;
    private String city;
    private String country;
    private String address;
    private String zipCode;
}
