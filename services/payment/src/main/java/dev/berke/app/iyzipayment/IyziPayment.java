package dev.berke.app.iyzipayment;

import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Payment;
import com.iyzipay.model.PaymentCard;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.Options;
import dev.berke.app.customer.CustomerClient;
import dev.berke.app.card.CreditCardResponse;
import dev.berke.app.payment.PaymentController;
import dev.berke.app.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IyziPayment {

    private final Options iyzipayOptions; // Correctly inject Options
    private final CustomerClient customerClient;
    private final PaymentService paymentService;
    private final PaymentController paymentController;

    // @Bean
    public CreatePaymentRequest createPaymentRequestWithCard(
            String customerId
    ) {
        CreatePaymentRequest request = new CreatePaymentRequest();

        // Create and set paymentCard
        PaymentCard paymentCard = createPaymentCard(customerId);
        request.setPaymentCard(paymentCard);

        // Create and set buyer
        Buyer buyer = createBuyer(customerId); // Pass the dynamic ID
        request.setBuyer(buyer);

        // Create and set billing address
        // here is where we will implement feign request to customer service
        Address billingAddress = createBillingAddress(customerId);
        request.setBillingAddress(billingAddress);

        // Create and set shipping address
        // here is where we will implement feign request to customer service
        Address shippingAddress = createShippingAddress(customerId);
        request.setShippingAddress(shippingAddress);

        // Create and set basket Items
        List<BasketItem> basketItems = createBasketItems();
        request.setBasketItems(basketItems);

        // Calculate total basket price
        BigDecimal totalBasketPrice = calculateTotalBasketPrice(basketItems);

        // added price
        request.setPrice(totalBasketPrice);

        // added paid price
        request.setPaidPrice(totalBasketPrice);

        // Create Payment using the injected Options
        Payment payment = Payment.create(request, iyzipayOptions);

        // You can print the payment response here

        return request;
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
        List<CreditCardResponse> creditCards = paymentService.getCreditCardsByCustomerId(customerId);

        if (creditCards != null && !creditCards.isEmpty()) {
            // Assuming you want to use the first credit card in the list
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

    private List<BasketItem> createBasketItems() {
        List<BasketItem> basketItems = new ArrayList<>();

        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("BI101");
        firstBasketItem.setName("Binocular");
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        firstBasketItem.setPrice(new BigDecimal("0.3"));

        basketItems.add(firstBasketItem);

        return basketItems;
    }

    private BigDecimal calculateTotalBasketPrice(List<BasketItem> basketItems){
        BigDecimal total = BigDecimal.ZERO;
        for (BasketItem basketItem : basketItems){
            total =  total.add(basketItem.getPrice());
        }
        return total;
    }
}