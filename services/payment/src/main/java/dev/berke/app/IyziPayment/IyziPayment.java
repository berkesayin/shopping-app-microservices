package dev.berke.app.IyziPayment;

import com.iyzipay.model.Address;
import com.iyzipay.model.BasketItem;
import com.iyzipay.model.BasketItemType;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Payment;
import com.iyzipay.model.PaymentCard;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.Options;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IyziPayment {

    private final Options iyzipayOptions; // Correctly inject Options

    @Bean
    public CreatePaymentRequest createPaymentRequestWithCard() {
        CreatePaymentRequest request = new CreatePaymentRequest();

        PaymentCard paymentCard = createPaymentCard();
        request.setPaymentCard(paymentCard);

        // added basket id
        request.setBasketId(UUID.randomUUID().toString());

        // Create and set buyer
        Buyer buyer = createBuyer();
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

        // Print payment request details BEFORE creating payment
        printPaymentRequestDetails(request);

        // Create Payment using the injected Options
        Payment payment = Payment.create(request, iyzipayOptions);

        // You can print the payment response here

        return request;
    }

    private PaymentCard createPaymentCard(){
        PaymentCard paymentCard = new PaymentCard();

        paymentCard.setCardHolderName("Berke Sayin");
        paymentCard.setCardNumber("5528790000000008");
        paymentCard.setExpireMonth("12");
        paymentCard.setExpireYear("2030");
        paymentCard.setCvc("123");
        paymentCard.setRegisterCard(0);

        return paymentCard;
    }

    private Buyer createBuyer(){
        Buyer buyer = new Buyer();

        buyer.setId("BY789");
        buyer.setName("Berke");
        buyer.setSurname("Sayin");
        buyer.setGsmNumber("+905350000000");
        buyer.setEmail("email@email.com");
        buyer.setIdentityNumber("74300864791");
        buyer.setLastLoginDate("2015-10-05 12:43:35");
        buyer.setRegistrationDate("2013-04-21 15:12:09");
        buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        buyer.setIp("85.34.78.112");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34732");

        return buyer;
    }

    private Address createBillingAddress(){
        Address billingAddress = new Address();

        billingAddress.setContactName("Jane Doe");
        billingAddress.setCity("Istanbul");
        billingAddress.setCountry("Turkey");
        billingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        billingAddress.setZipCode("34742");

        return billingAddress;
    }

    private Address createShippingAddress(){
        Address shippingAddress = new Address();

        shippingAddress.setContactName("Jane Doe");
        shippingAddress.setCity("Istanbul");
        shippingAddress.setCountry("Turkey");
        shippingAddress.setAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
        shippingAddress.setZipCode("34742");

        return shippingAddress;
    }


    private List<BasketItem> createBasketItems() {
        List<BasketItem> basketItems = new ArrayList<>();

        BasketItem firstBasketItem = new BasketItem();
        firstBasketItem.setId("BI101");
        firstBasketItem.setName("Binocular");
        firstBasketItem.setCategory1("Collectibles");
        firstBasketItem.setCategory2("Accessories");
        firstBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        firstBasketItem.setPrice(new BigDecimal("0.3"));
        basketItems.add(firstBasketItem);

        BasketItem secondBasketItem = new BasketItem();
        secondBasketItem.setId("BI102");
        secondBasketItem.setName("Camera");
        secondBasketItem.setCategory1("Electronics");
        secondBasketItem.setCategory2("Accessories");
        secondBasketItem.setItemType(BasketItemType.PHYSICAL.name());
        secondBasketItem.setPrice(new BigDecimal("100.7"));
        basketItems.add(secondBasketItem);

        return basketItems;
    }

    private BigDecimal calculateTotalBasketPrice(List<BasketItem> basketItems){
        BigDecimal total = BigDecimal.ZERO;
        for (BasketItem basketItem : basketItems){
            total =  total.add(basketItem.getPrice());
        }
        return total;
    }

    private void printPaymentRequestDetails(CreatePaymentRequest request) {
        System.out.println("---------------------------------------------");
        System.out.println("Payment Request Details:");
        System.out.println("---------------------------------------------");
        System.out.println("Basket ID: " + request.getBasketId());
        System.out.println("Price: " + request.getPrice());
        System.out.println("Paid Price: " + request.getPaidPrice());

        if (request.getPaymentCard() != null) {
            System.out.println("Payment Card Details:");
            System.out.println("  Card Holder Name: " + request.getPaymentCard().getCardHolderName());
            System.out.println("  Card Number: " + request.getPaymentCard().getCardNumber());
            System.out.println("  Expire Month: " + request.getPaymentCard().getExpireMonth());
            System.out.println("  Expire Year: " + request.getPaymentCard().getExpireYear());
            // CVC is sensitive information and should not be logged in real application
            // System.out.println("  CVC: " + request.getPaymentCard().getCvc());
            System.out.println("  Register Card: " + request.getPaymentCard().getRegisterCard());

        }

        if(request.getBuyer() != null){
            System.out.println("Buyer Details:");
            System.out.println("  Buyer ID: " + request.getBuyer().getId());
            System.out.println("  Buyer Name: " + request.getBuyer().getName());
            System.out.println("  Buyer Surname: " + request.getBuyer().getSurname());
            System.out.println("  Buyer Gsm Number: " + request.getBuyer().getGsmNumber());
            System.out.println("  Buyer Email: " + request.getBuyer().getEmail());
            System.out.println("  Buyer Identity Number: " + request.getBuyer().getIdentityNumber());
            System.out.println("  Buyer Last Login Date: " + request.getBuyer().getLastLoginDate());
            System.out.println("  Buyer Registration Date: " + request.getBuyer().getRegistrationDate());
            System.out.println("  Buyer Registration Address: " + request.getBuyer().getRegistrationAddress());
            System.out.println("  Buyer IP: " + request.getBuyer().getIp());
            System.out.println("  Buyer City: " + request.getBuyer().getCity());
            System.out.println("  Buyer Country: " + request.getBuyer().getCountry());
            System.out.println("  Buyer Zip Code: " + request.getBuyer().getZipCode());
        }

        if(request.getBillingAddress() != null){
            System.out.println("Billing Address Details:");
            System.out.println("  Billing Contact Name: " + request.getBillingAddress().getContactName());
            System.out.println("  Billing City: " + request.getBillingAddress().getCity());
            System.out.println("  Billing Country: " + request.getBillingAddress().getCountry());
            System.out.println("  Billing Address: " + request.getBillingAddress().getAddress());
            System.out.println("  Billing Zip Code: " + request.getBillingAddress().getZipCode());
        }

        if(request.getShippingAddress() != null){
            System.out.println("Shipping Address Details:");
            System.out.println("  Shipping Contact Name: " + request.getShippingAddress().getContactName());
            System.out.println("  Shipping City: " + request.getShippingAddress().getCity());
            System.out.println("  Shipping Country: " + request.getShippingAddress().getCountry());
            System.out.println("  Shipping Address: " + request.getShippingAddress().getAddress());
            System.out.println("  Shipping Zip Code: " + request.getShippingAddress().getZipCode());
        }

        if (request.getBasketItems() != null && !request.getBasketItems().isEmpty()) {
            System.out.println("Basket Items Details:");
            for(BasketItem basketItem : request.getBasketItems()){
                System.out.println("  Basket Item Id: " + basketItem.getId());
                System.out.println("  Basket Item Name: " + basketItem.getName());
                System.out.println("  Basket Item Category1: " + basketItem.getCategory1());
                System.out.println("  Basket Item Category2: " + basketItem.getCategory2());
                System.out.println("  Basket Item Type: " + basketItem.getItemType());
                System.out.println("  Basket Item Price: " + basketItem.getPrice());
            }
        }

        System.out.println("---------------------------------------------");
    }
}