package dev.berke.app.payment.api;

import dev.berke.app.payment.api.dto.CreditCardRequest;
import dev.berke.app.payment.api.dto.CreditCardResponse;
import dev.berke.app.payment.infrastructure.paymentprovider.iyzipay.IyzipayService;
import dev.berke.app.payment.api.dto.PaymentResponse;
import dev.berke.app.payment.application.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final IyzipayService iyzipayService;

    @PostMapping("/me/credit-cards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Integer> createCreditCard(
            @RequestBody @Valid CreditCardRequest creditCardRequest,
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;

        return ResponseEntity.ok(paymentService.createCreditCard(creditCardRequest, customerId));
    }

    @GetMapping("/me/credit-cards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CreditCardResponse>> getCreditCards(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;

        return ResponseEntity.ok(
                paymentService.getCreditCards(customerId)
        );
    }

    @PostMapping("/iyzi-payment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> createPayment(
            @AuthenticationPrincipal String customerIdPrincipal
    ) {
        String customerId = customerIdPrincipal;

        PaymentResponse paymentResponse =
                iyzipayService.createPaymentRequestWithCard(customerId);

        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

}
