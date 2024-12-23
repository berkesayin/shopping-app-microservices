package dev.berke.app.product;

import dev.berke.app.constants.OrderConstants;
import dev.berke.app.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {

    @Value("${application.config.product-url}")
    private String productUrl;

    private final RestTemplate restTemplate;

    /**
     * Purchases a list of products by sending a POST request to the product service
     * @param requestBody A list of PurchaseRequest objects representing the products to purchase
     * @return A list of PurchaseResponse objects representing the result of the purchase
     * @throws BusinessException If an error occurs during the purchase process
     */
    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody) {
        // 1. Set up HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // 2. Create an HttpEntity with the request body and headers
        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 3. Define the response type as a list of PurchaseResponse
        ParameterizedTypeReference<List<PurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() {};

        // 4. Send the POST request to the product service
        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                HttpMethod.POST,
                requestEntity, // The request entity containing the body and headers
                responseType
        );

        // 5. Check if the response indicates an error
        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException(
                    OrderConstants.PRODUCT_PURCHASE_ERROR_MESSAGE +
                            responseEntity.getStatusCode()
            );
        }

        // 6. Return the list of PurchaseResponse objects from the successful response
        return responseEntity.getBody();
    }
}