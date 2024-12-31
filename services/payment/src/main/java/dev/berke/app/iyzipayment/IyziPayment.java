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
import dev.berke.app.customer.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IyziPayment {

    private final Options iyzipayOptions; // Correctly inject Options
    private final CustomerClient customerClient;

    @Bean
    public CreatePaymentRequest createPaymentRequestWithCard(
            String customerId
    ) {
        CreatePaymentRequest request = new CreatePaymentRequest();

        // Create and set paymentCard
        PaymentCard paymentCard = createPaymentCard();
        request.setPaymentCard(paymentCard);

        // Create and set buyer
        // Buyer buyer = createBuyer(customerRequest);
        // request.setBuyer(buyer);

        // Retrieve Customer data from customer service
        CustomerResponse customerResponse = getCustomerDetails(customerId);
        Buyer iyziBuyer = createIyziBuyer(customerResponse);
        request.setBuyer(iyziBuyer);

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

    private CustomerResponse getCustomerDetails(String customerId) {
        return customerClient.getCustomerById(customerId);
    }

    private Buyer createIyziBuyer(CustomerResponse customerResponse) {
        Buyer buyer = new Buyer();

        buyer.setId(customerResponse.id());
        buyer.setName(customerResponse.name());
        buyer.setSurname(customerResponse.surname());
        buyer.setGsmNumber(customerResponse.gsmNumber());
        buyer.setEmail(customerResponse.email());
        buyer.setIdentityNumber(customerResponse.identityNumber());
        buyer.setRegistrationAddress(customerResponse.registrationAddress());
        buyer.setCity(customerResponse.city());
        buyer.setCountry(customerResponse.country());
        buyer.setZipCode(customerResponse.zipCode());

        return buyer;
    }

    private Address createBillingAddress() {
        Address billingAddress = new Address();

        billingAddress.setContactName("Berke Sayin");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");

        return billingAddress;
    }

    private Address createShippingAddress() {
        Address shippingAddress = new Address();

        shippingAddress.setContactName("Berke Sayin");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        shippingAddress.setZipCode("34742");

        return shippingAddress;
    }

    private PaymentCard createPaymentCard(){
        PaymentCard paymentCard = new PaymentCard();

        paymentCard.setCardHolderName("Berke Sayin");
        paymentCard.setCardNumber("5890040000000016");
        paymentCard.setExpireMonth("12");
        paymentCard.setExpireYear("2030");
        paymentCard.setCvc("123");

        return paymentCard;
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