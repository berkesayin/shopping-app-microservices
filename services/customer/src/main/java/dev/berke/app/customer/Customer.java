package dev.berke.app.customer;

// import dev.berke.app.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document
public class Customer {

    @Id
    private String id;
    private String name;
    private String surname;
    private String gsmNumber;
    private String email;
    private String identityNumber;
    private String registrationAddress;
    private String city;
    private String country;
    private String zipCode;
    // private Address address;
}

