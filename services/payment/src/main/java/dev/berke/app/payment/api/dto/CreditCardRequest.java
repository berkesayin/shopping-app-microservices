package dev.berke.app.payment.api.dto;

public record CreditCardRequest(
        String cardHolderName,
        String cardNumber,
        String expireMonth,
        String expireYear,
        String cvc
) {
}