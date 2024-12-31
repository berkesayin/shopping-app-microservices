package dev.berke.app.card;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "card")
public class CreditCard {

    @Id
    @GeneratedValue
    private Integer id;

    private String customerId;
    private String cardHolderName;
    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String cvc;
    // Use only at the time of creation.// Do not store this, and store only token after creation.
}
