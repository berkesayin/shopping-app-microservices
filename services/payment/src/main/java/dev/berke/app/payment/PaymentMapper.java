package dev.berke.app.payment;

import dev.berke.app.card.CreditCard;
import dev.berke.app.card.CreditCardRequest;
import dev.berke.app.card.CreditCardResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMapper {

    public Payment toCreditCard(CreditCardRequest creditCardRequest) {
        return Payment.builder()
                .id(creditCardRequest.id())
                .customerId(creditCardRequest.customerId())
                .cardHolderName(creditCardRequest.cardHolderName())
                .cardNumber(creditCardRequest.cardNumber())
                .expireMonth(creditCardRequest.expireMonth())
                .expireYear(creditCardRequest.expireYear())
                .cvc(creditCardRequest.cvc())
                .build();
    }

    public List<CreditCardResponse> toCreditCardResponseList(List<CreditCard> creditCards) {
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

    public Payment toPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .id(paymentRequest.id())
                .orderId(paymentRequest.orderId())
                .paymentMethod(paymentRequest.paymentMethod())
                .amount(paymentRequest.amount())
                .build();
    }
}
