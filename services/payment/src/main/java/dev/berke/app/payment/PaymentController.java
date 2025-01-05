package dev.berke.app.payment;

import dev.berke.app.card.CreditCardResponse;
import dev.berke.app.iyzipay.IyzipayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final IyzipayService iyzipayService;

    @PostMapping("/credit-cards")
    public ResponseEntity<Integer> createCreditCard(
            @RequestBody @Valid CreditCardRequest creditCardRequest) {
        return ResponseEntity.ok(paymentService.createCreditCard(creditCardRequest));
    }

    @GetMapping("/customer/{customer-id}")
    public ResponseEntity<List<CreditCardResponse>> getCreditCardsByCustomerId(
            @PathVariable("customer-id") String customerId) {
        return ResponseEntity.ok(paymentService.getCreditCardsByCustomerId(customerId));
    }

    /*
     * @PostMapping
     * public ResponseEntity<Integer> createPayment(
     * 
     * @RequestBody @Valid PaymentRequest paymentRequest
     * ) {
     * return ResponseEntity.ok(paymentService.createPayment(paymentRequest));
     * }
     */

    /*
     * old with CreatePaymentRequest
     * 
     * @PostMapping("/create-iyzipayment")
     * public ResponseEntity<CreatePaymentRequest> createPayment(
     * 
     * @RequestParam String customerId
     * ) {
     * try {
     * CreatePaymentRequest paymentResponse =
     * iyzipayService.createPaymentRequestWithCard(customerId);
     * 
     * return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
     * 
     * } catch (Exception e) {
     * // Log the exception for debugging purposes
     * e.printStackTrace();
     * // Return a user-friendly error message
     * return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }
     */

    @PostMapping("/create-iyzipayment")
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestParam String customerId) {
        PaymentResponse paymentResponse = iyzipayService.createPaymentRequestWithCard(customerId);

        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }
}
