package dev.berke.app.payment;

import dev.berke.app.card.CreditCardRequest;
import dev.berke.app.card.CreditCardResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMapper {

    public Payment toCreditCard(CreditCardRequest creditCardRequest, String customerId) {
        return Payment.builder()
                .customerId(customerId)
                .cardHolderName(creditCardRequest.cardHolderName())
                .cardNumber(creditCardRequest.cardNumber())
                .expireMonth(creditCardRequest.expireMonth())
                .expireYear(creditCardRequest.expireYear())
                .cvc(creditCardRequest.cvc())
                .build();
    }

    public List<CreditCardResponse> toCreditCardResponseList(List<Payment> creditCards) {
        return creditCards.stream()
                .map(creditCard -> new CreditCardResponse(
                        creditCard.getId(),
                        creditCard.getCustomerId(),
                        creditCard.getCardHolderName(),
                        creditCard.getCardNumber(),
                        creditCard.getExpireMonth(),
                        creditCard.getExpireYear(),
                        creditCard.getCvc()
                ))
                .collect(Collectors.toList());
    }
}
