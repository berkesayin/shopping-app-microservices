package dev.berke.app.payment;

import dev.berke.app.card.CreditCardRequest;
import dev.berke.app.card.CreditCardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/credit-cards")
    public ResponseEntity<Integer> createCreditCard(
            @RequestBody @Valid CreditCardRequest creditCardRequest
    ) {
        return ResponseEntity.ok(paymentService.createCreditCard(creditCardRequest));
    }

    @GetMapping("/customer/{customer-id}")
    public ResponseEntity<List<CreditCardResponse>> getCreditCardsByCustomerId(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(paymentService.getCreditCardsByCustomerId(customerId));
    }

    @PostMapping
    public ResponseEntity<Integer> createPayment(
            @RequestBody @Valid PaymentRequest paymentRequest
    ) {
        return ResponseEntity.ok(paymentService.createPayment(paymentRequest));
    }

}
