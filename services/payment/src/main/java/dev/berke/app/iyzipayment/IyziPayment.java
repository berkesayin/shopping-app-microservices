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
import dev.berke.app.customer.CustomerRequest;
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
    public CreatePaymentRequest createPaymentRequestWithCard(CustomerRequest customerRequest) {
        CreatePaymentRequest request = new CreatePaymentRequest();

        // Create and set paymentCard
        PaymentCard paymentCard = createPaymentCard();
        request.setPaymentCard(paymentCard);

        // Create and set buyer
        Buyer buyer = createBuyer(customerRequest);
        request.setBuyer(buyer);

        // Create and set billing address
        Address billingAddress = createBillingAddress();
        request.setBillingAddress(billingAddress);

        // Create and set shipping address
        Address shippingAddress = createShippingAddress();
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

    /*private Buyer createBuyer(){
        Buyer buyer = new Buyer();

        buyer.setId("BY789");
        buyer.setName("Berke");
        buyer.setSurname("Sayin");
        buyer.setGsmNumber("+905350000000");
        buyer.setEmail("email@email.com");
        buyer.setIdentityNumber("74300864791");
        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34732");

        return buyer;
    }*/

    private Buyer createBuyer(CustomerRequest customerRequest){
        // 1. Create a customer by calling the createCustomer method from CustomerClient
        String customerId = customerClient.createCustomer(customerRequest);

        Buyer buyer = new Buyer();

        buyer.setId(customerId); // Use the customerId returned from Customer service
        buyer.setName(customerRequest.name());
        buyer.setSurname(customerRequest.surname());
        buyer.setGsmNumber(customerRequest.gsmNumber());
        buyer.setEmail(customerRequest.email());
        buyer.setIdentityNumber(customerRequest.identityNumber());
        buyer.setRegistrationAddress(customerRequest.registrationAddress());
        buyer.setCity(customerRequest.city());
        buyer.setCountry(customerRequest.country());
        buyer.setZipCode(customerRequest.zipCode());

        return buyer;
    }

    private Address createBillingAddress(){
        Address billingAddress = new Address();

        billingAddress.setContactName("Berke Sayin");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");

        return billingAddress;
    }

    private Address createShippingAddress(){
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