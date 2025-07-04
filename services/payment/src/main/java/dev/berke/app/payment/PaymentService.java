package dev.berke.app.payment;

import dev.berke.app.card.CreditCardRequest;
import dev.berke.app.card.CreditCardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public Integer createCreditCard(CreditCardRequest creditCardRequest, String customerId) {
        var creditCard = paymentMapper.toCreditCard(creditCardRequest, customerId);
        return paymentRepository.save(creditCard).getId();
    }

    public List<CreditCardResponse> getCreditCards(String customerId) {
        log.info("Fetching credit cards for customer ID: {}", customerId);

        List<Payment> creditCards = paymentRepository.findByCustomerId(customerId);
        log.info("Found {} credit cards for customer ID: {}", creditCards.size(), customerId);

        return paymentMapper.toCreditCardResponseList(creditCards);
    }
}
