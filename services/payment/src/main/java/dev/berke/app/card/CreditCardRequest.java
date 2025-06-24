package dev.berke.app.card;

public record CreditCardRequest(
        String cardHolderName,
        String cardNumber,
        String expireMonth,
        String expireYear,
        String cvc
) {
}