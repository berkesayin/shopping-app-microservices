package dev.berke.app.payment;

import dev.berke.app.card.CreditCardRequest;
import dev.berke.app.card.CreditCardResponse;
import dev.berke.app.kafka.PaymentNotificationProducer;
import dev.berke.app.kafka.PaymentNotificationRequest;
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
    private final PaymentNotificationProducer paymentNotificationProducer;

    public Integer createCreditCard(CreditCardRequest creditCardRequest) {
        var creditCard = paymentMapper.toCreditCard(creditCardRequest);
        return paymentRepository.save(creditCard).getId();
    }

    public List<CreditCardResponse> getCreditCardsByCustomerId(String customerId) {
        log.info("Fetching credit cards for customer ID: {}", customerId);

        List<Payment> creditCards = paymentRepository.findByCustomerId(customerId);
        log.info("Found {} credit cards for customer ID: {}", creditCards.size(), customerId);

        return paymentMapper.toCreditCardResponseList(creditCards);
    }
}
