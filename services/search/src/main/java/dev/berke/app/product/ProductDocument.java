package dev.berke.app.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "product", createIndex = true)
public class ProductDocument {

    @Id
    @Field(type = FieldType.Integer, name = "product_id")
    private Integer productId;

    @Field(type = FieldType.Text, name = "product_name")
    private String productName;

    @Field(type = FieldType.Integer, name = "category_id")
    private Integer categoryId;

    @Field(type = FieldType.Keyword, name = "category_name")
    private String categoryName;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100, name = "base_price")
    private BigDecimal basePrice;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100, name = "min_price")
    private BigDecimal minPrice;

    @Field(type = FieldType.Keyword, name = "manufacturer")
    private String manufacturer;

    @Field(type = FieldType.Keyword, name = "sku")
    private String sku;

    @Field(type = FieldType.Integer, name = "status")
    private Integer status;

    @Field(type = FieldType.Date, name = "created_on")
    private Instant createdOn;
}