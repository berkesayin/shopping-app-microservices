package dev.berke.app.card;

import jakarta.validation.constraints.NotNull;

public record CreditCardRequest(
        @NotNull
        String customerId,

        String cardHolderName,
        String cardNumber,
        String expireMonth,
        String expireYear,
        String cvc
) {
}