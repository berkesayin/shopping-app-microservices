package dev.berke.app.payment.application;

import dev.berke.app.payment.domain.model.Payment;
import dev.berke.app.payment.application.mapper.PaymentMapper;
import dev.berke.app.payment.domain.repository.PaymentRepository;
import dev.berke.app.payment.api.dto.CreditCardRequest;
import dev.berke.app.payment.api.dto.CreditCardResponse;
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
