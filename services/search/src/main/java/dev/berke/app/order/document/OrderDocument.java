package dev.berke.app.order.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "order", writeTypeHint = WriteTypeHint.FALSE)
public class OrderDocument {

    @Id
    @Field(type = FieldType.Keyword, name = "order_id")
    @JsonProperty("order_id")
    private String orderId;

    @Field(type = FieldType.Keyword)
    private String reference;

    @Field(
            type = FieldType.Date,
            name = "order_date",
            format = {},
            pattern = {
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    "yyyy-MM-dd'T'HH:mm:ssXXX",
                    "yyyy-MM-dd'T'HH:mm:ss'Z'"
            }
    )
    @JsonProperty("order_date")
    private Instant orderDate;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100, name = "total_amount")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @Field(type = FieldType.Keyword, name = "payment_method")
    @JsonProperty("payment_method")
    private String paymentMethod;

    @Field(type = FieldType.Object)
    private CustomerDocument customer;

    @Field(type = FieldType.Object, name = "shipping_address")
    @JsonProperty("shipping_address")
    private AddressDocument shippingAddress;

    @Field(type = FieldType.Object, name = "billing_address")
    @JsonProperty("billing_address")
    private AddressDocument billingAddress;

    @Field(type = FieldType.Nested)
    private List<ItemDocument> items;

    // inner classes for nested object structure: CustomerDocument, AddressDocument, ItemDocument
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerDocument {

        @Field(type = FieldType.Keyword)
        private String id;

        @MultiField(
                mainField = @Field(type = FieldType.Text, name = "full_name"),
                otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)}
        )
        @JsonProperty("full_name")
        private String fullName;

        @MultiField(
                mainField = @Field(type = FieldType.Text, analyzer = "email_analyzer"),
                otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)}
        )
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDocument {

        @Field(type = FieldType.Text, name = "contact_name")
        @JsonProperty("contact_name")
        private String contactName;

        @Field(type = FieldType.Keyword)
        private String city;

        @Field(type = FieldType.Keyword)
        private String country;

        @Field(type = FieldType.Text)
        private String address;

        @Field(type = FieldType.Keyword, name = "zip_code")
        @JsonProperty("zip_code")
        private String zipCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDocument {

        @Field(type = FieldType.Integer, name = "product_id")
        @JsonProperty("product_id")
        private Integer productId;

        @MultiField(
                mainField = @Field(type = FieldType.Text, name = "product_name"),
                otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)}
        )
        @JsonProperty("product_name")
        private String productName;

        @Field(type = FieldType.Keyword)
        private String manufacturer;

        @Field(type = FieldType.Integer)
        private Integer quantity;

        @Field(type = FieldType.Scaled_Float, scalingFactor = 100, name = "price_per_unit")
        @JsonProperty("price_per_unit")
        private BigDecimal pricePerUnit;
    }
}