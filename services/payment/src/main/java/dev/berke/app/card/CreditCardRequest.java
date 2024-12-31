package dev.berke.app.card;

import jakarta.validation.constraints.NotNull;

public record CreditCardRequest(
        Integer id,

        @NotNull
        String customerId,

        @NotNull
        String cardHolderName,

        @NotNull
        String cardNumber,

        @NotNull
        String expireMonth,

        @NotNull
        String expireYear,

        @NotNull
        String cvc
) {
}