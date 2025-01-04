package dev.berke.app.payment;
import dev.berke.app.card.CreditCardResponse;
import lombok.extern.slf4j.Slf4j;
import dev.berke.app.kafka.PaymentNotificationProducer;
import lombok.RequiredArgsConstructor;
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

    /*
    public List<CreditCardResponse> getCreditCardsByCustomerId(String customerId) {
        List<CreditCard> creditCards = paymentRepository.findByCustomerId(customerId);
        return paymentMapper.toCreditCardResponseList(creditCards);
    }*/

    public List<CreditCardResponse> getCreditCardsByCustomerId(String customerId) {
        log.info("Fetching credit cards for customer ID: {}", customerId);
        List<Payment> creditCards = paymentRepository.findByCustomerId(customerId);
        log.info("Found {} credit cards for customer ID: {}", creditCards.size(), customerId);
        return paymentMapper.toCreditCardResponseList(creditCards);
    }

    /*
    public Integer createPayment(PaymentRequest paymentRequest) {
        var payment = paymentRepository.save(paymentMapper.toPayment(paymentRequest));

        paymentNotificationProducer.sendPaymentNotification(
                new PaymentNotificationRequest(
                        paymentRequest.orderReference(),
                        paymentRequest.amount(),
                        paymentRequest.paymentMethod(),
                        paymentRequest.customer().firstname(),
                        paymentRequest.customer().lastname(),
                        paymentRequest.customer().email()
                )
        );

        return payment.getId();
    }

     */
}
