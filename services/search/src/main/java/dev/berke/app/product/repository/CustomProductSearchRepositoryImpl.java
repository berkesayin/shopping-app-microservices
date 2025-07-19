package dev.berke.app.product.repository;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import dev.berke.app.product.dto.ProductSearchRequest;
import dev.berke.app.product.document.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class CustomProductSearchRepositoryImpl implements CustomProductSearchRepository {

    private static final String CATEGORY_AGG = "category_agg";
    private static final String MANUFACTURER_AGG = "manufacturer_agg";

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchHits<ProductDocument> search(ProductSearchRequest request) {
        // business logic to search products
        // 1. full-text search
        // 2. filtering: category filter, manufacturer filter, price range filter, filter for active products
        // 3. aggregations
        // 4. building native query
        // 5. sorting

        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 1. full-text search
        if (StringUtils.hasText(request.query())) {
            boolQueryBuilder.must(q -> q
                    .multiMatch(mm -> mm
                            .query(request.query())
                            .fields("product_name", "category.name", "manufacturer")
                            .fuzziness("AUTO")
                    )
            );
        } else {
            boolQueryBuilder.must(q -> q.matchAll(m -> m));
        }

        // 2. filtering
        // category filter
        if (!CollectionUtils.isEmpty(request.categories())) {
            boolQueryBuilder.filter(f -> f
                    .terms(t -> t
                            .field("category.name.keyword")
                            .terms(v -> v.value(
                                    request.categories()
                                            .stream()
                                            .map(FieldValue::of)
                                            .toList()
                                    )
                            )
                    )
            );
        }

        // manufacturer filter
        if (!CollectionUtils.isEmpty(request.manufacturers())) {
            boolQueryBuilder.filter(f -> f
                    .terms(t -> t
                            .field("manufacturer.keyword")
                            .terms(v -> v.value(
                                    request.manufacturers()
                                            .stream()
                                            .map(FieldValue::of)
                                            .toList()
                                    )
                            )
                    )
            );
        }

        // price range filter
        if (request.priceRange() != null) {
            boolQueryBuilder.filter(f -> f
                    .range(r -> {
                        r.field("min_price");

                        if (request.priceRange().min() != null) {
                            r.gte((JsonData) FieldValue.of(
                                    request.priceRange()
                                            .min()
                                            .doubleValue()
                                    )
                            );
                        }

                        if (request.priceRange().max() != null) {
                            r.lte((JsonData) FieldValue.of(
                                    request.priceRange()
                                            .max()
                                            .doubleValue()
                                    )
                            );
                        }

                        return r;
                    })
            );
        }

        // filter for active products
        boolQueryBuilder.filter(f -> f.term(
                t -> t.field("status").value(true)));

        Query finalQuery = new Query(boolQueryBuilder.build());

        // 3. aggregations
        Aggregation categoryAgg = Aggregation.of(a -> a.terms(
                t -> t.field("category.name.keyword").size(50)));

        Aggregation manufacturerAgg = Aggregation.of(a -> a.terms(
                t -> t.field("manufacturer.keyword").size(50)));

        // 4. building native query
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder()
                .withQuery(finalQuery)
                .withPageable(PageRequest.of(request.page(), request.size()))
                .withAggregation(CATEGORY_AGG, categoryAgg)
                .withAggregation(MANUFACTURER_AGG, manufacturerAgg);

        // 5. sorting
        if (request.sortBy() != null) {
            switch (request.sortBy()) {
                case PRICE_ASC -> nativeQueryBuilder.withSort(
                        s -> s.field(f -> f.field("min_price").order(SortOrder.Asc)));

                case PRICE_DESC -> nativeQueryBuilder.withSort(
                        s -> s.field(f -> f.field("min_price").order(SortOrder.Desc)));

                case NEWEST -> nativeQueryBuilder.withSort(
                        s -> s.field(f -> f.field("created_on").order(SortOrder.Desc)));

                case RELEVANCE -> { }
            }
        }

        NativeQuery query = nativeQueryBuilder.build();

        return elasticsearchOperations.search(query, ProductDocument.class);
    }
}
