package dev.berke.app.iyzipay;

import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Payment;
import com.iyzipay.model.PaymentCard;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.Options;
import dev.berke.app.basket.BasketClient;
import dev.berke.app.basket.BasketResponse;
import dev.berke.app.basket.BasketTotalPriceResponse;
import dev.berke.app.customer.CustomerClient;
import dev.berke.app.card.CreditCardResponse;
import dev.berke.app.payment.PaymentResponse;
import dev.berke.app.payment.PaymentService;
import lombok.RequiredArgsConstructor;
// import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class IyzipayService {

    private final Options iyzipayOptions; // Correctly inject Options
    private final PaymentService paymentService;
    private final CustomerClient customerClient;
    private final BasketClient basketClient;

    // @Bean
    // public CreatePaymentRequest createPaymentRequestWithCard(){}
    public PaymentResponse createPaymentRequestWithCard(
            String customerId
    ) {
        CreatePaymentRequest request = new CreatePaymentRequest();

        // Create and set paymentCard: already in payment service
        PaymentCard paymentCard = createPaymentCard(customerId);
        request.setPaymentCard(paymentCard);

        // Create and set buyer: feign request to customer service
        Buyer buyer = createBuyer(customerId); // Pass the dynamic ID
        request.setBuyer(buyer);

        // Create and set billing address: feign request to customer service
        Address billingAddress = createBillingAddress(customerId);
        request.setBillingAddress(billingAddress);

        // Create and set shipping address: feign request to customer service
        Address shippingAddress = createShippingAddress(customerId);
        request.setShippingAddress(shippingAddress);

        // Create and set basket items: feign request to basket service
        List<com.iyzipay.model.BasketItem> basketItems = createBasketItems(customerId);
        request.setBasketItems(basketItems);

        // Calculate total basket price: feign request to basket service
        BigDecimal totalBasketPrice = calculateTotalBasketPrice(customerId);
        request.setPrice(totalBasketPrice);
        request.setPaidPrice(totalBasketPrice);

        // Create payment using the injected options
        Payment payment = Payment.create(request, iyzipayOptions);

        // old version with CreatePaymentRequest
        // return request;

        return new PaymentResponse(payment.getPaymentStatus(), payment.getPaymentId());

    }

    private Buyer createBuyer(String customerId) {
        Map<String, Object> customer = customerClient.getCustomerById(customerId);

        Buyer buyer = new Buyer();

        buyer.setId(customer.get("id").toString());
        buyer.setName(customer.get("name").toString());
        buyer.setSurname(customer.get("surname").toString());
        buyer.setGsmNumber(customer.get("gsmNumber").toString());
        buyer.setEmail(customer.get("email").toString());
        buyer.setIdentityNumber(customer.get("identityNumber").toString());
        buyer.setRegistrationAddress(customer.get("registrationAddress").toString());
        buyer.setCity(customer.get("city").toString());
        buyer.setCountry(customer.get("country").toString());
        buyer.setZipCode(customer.get("zipCode").toString());

        return buyer;
    }

    private Address createBillingAddress(String customerId) {
        Map<String, Object> customer = customerClient.getCustomerById(customerId);
        Map<String, String> billingAddressData = (Map<String, String>) customer.get("billingAddress");

        Address billingAddress = new Address();

        billingAddress.setContactName(billingAddressData.get("contactName"));
        billingAddress.setCity(billingAddressData.get("city"));
        billingAddress.setCountry(billingAddressData.get("country"));
        billingAddress.setAddress(billingAddressData.get("address"));
        billingAddress.setZipCode(billingAddressData.get("zipCode"));

        return billingAddress;
    }

    private Address createShippingAddress(String customerId) {
        Map<String, Object> customer = customerClient.getCustomerById(customerId);
        Map<String, String> shippingAddressData = (Map<String, String>) customer.get("shippingAddress");

        Address shippingAddress = new Address();

        shippingAddress.setContactName(shippingAddressData.get("contactName"));
        shippingAddress.setCity(shippingAddressData.get("city"));
        shippingAddress.setCountry(shippingAddressData.get("country"));
        shippingAddress.setAddress(shippingAddressData.get("address"));
        shippingAddress.setZipCode(shippingAddressData.get("zipCode"));

        return shippingAddress;
    }

    private PaymentCard createPaymentCard(String customerId) {
        List<CreditCardResponse> creditCards = paymentService
                .getCreditCardsByCustomerId(customerId);

        if (creditCards != null && !creditCards.isEmpty()) {
            // Use the first credit card in the list
            CreditCardResponse creditCardResponse = creditCards.get(0);

            PaymentCard paymentCard = new PaymentCard();

            // Set the attributes from CreditCardResponse to PaymentCard
            paymentCard.setCardHolderName(creditCardResponse.cardHolderName());
            paymentCard.setCardNumber(creditCardResponse.cardNumber());
            paymentCard.setExpireMonth(creditCardResponse.expireMonth());
            paymentCard.setExpireYear(creditCardResponse.expireYear());
            paymentCard.setCvc(creditCardResponse.cvc());

            return paymentCard;
        } else {
            // Handle the case where no credit cards are found for the customer
            System.out.println("No credit cards found for customer: " + customerId);
            return new PaymentCard(); // Or throw a specific exception
        }
    }

    private List<com.iyzipay.model.BasketItem> createBasketItems(String customerId) {
        BasketResponse basketResponse = basketClient.getBasketByCustomerId(customerId);
        List<dev.berke.app.basket.BasketItem> fetchedBasketItems = basketResponse.items();

        List<com.iyzipay.model.BasketItem> basketItems = fetchedBasketItems.stream()
                .map(item -> {
                    com.iyzipay.model.BasketItem iyziBasketItem = new com.iyzipay.model.BasketItem();
                    iyziBasketItem.setId(String.valueOf(item.getProductId()));
                    iyziBasketItem.setName(item.getProductName());
                    iyziBasketItem.setCategory1(String.valueOf(item.getCategoryId()));
                    iyziBasketItem.setItemType(BasketItemType.PHYSICAL.name());
                    iyziBasketItem.setPrice(BigDecimal.valueOf(item.getPrice()));
                    return iyziBasketItem;
                })
                .collect(Collectors.toList());
        return basketItems;
    }

    private BigDecimal calculateTotalBasketPrice(String customerId) {
        BasketTotalPriceResponse totalPriceResponse =
                basketClient.getTotalBasketPrice(customerId);

        return BigDecimal.valueOf(totalPriceResponse.totalPrice());
    }
}