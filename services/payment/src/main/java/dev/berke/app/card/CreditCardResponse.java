package dev.berke.app.card;

public record CreditCardResponse(
        Integer id,
        String customerId,
        String cardHolderName,
        String cardNumber,
        String expireMonth,
        String expireYear,
        String cvc
) {
}
