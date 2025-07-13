package dev.berke.app.product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "product", writeTypeHint = WriteTypeHint.FALSE)
public class ProductDocument {

    @Id
    @Field(type = FieldType.Integer, name = "product_id")
    @JsonProperty("product_id")
    private Integer productId;

    // multi-field for flexible querying
    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "product_name"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword),
                    @InnerField(
                            suffix = "autocomplete",
                            type = FieldType.Text,
                            analyzer = "autocomplete_analyzer",
                            searchAnalyzer = "standard"
                    )
            }
    )
    @JsonProperty("product_name")
    private String productName;

    // nested category object
    @Field(type = FieldType.Object)
    private CategoryDocument category;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100, name = "base_price")
    @JsonProperty("base_price")
    private BigDecimal basePrice;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100, name = "min_price")
    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "manufacturer"),
            otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword) }
    )
    private String manufacturer;

    @Field(type = FieldType.Keyword, name = "sku")
    private String sku;

    @Field(type = FieldType.Boolean, name = "status")
    private Boolean status;

    @Field(type = FieldType.Date, name = "created_on", format = DateFormat.date_time)
    @JsonProperty("created_on")
    private Instant createdOn;

    // nested category
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDocument {
        @Field(type = FieldType.Keyword)
        private String id;

        @MultiField(
                mainField = @Field(type = FieldType.Text),
                otherFields = { @InnerField(suffix = "keyword", type = FieldType.Keyword) }
        )
        private String name;
    }
}