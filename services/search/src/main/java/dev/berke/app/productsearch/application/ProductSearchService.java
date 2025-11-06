package dev.berke.app.productsearch.application;

import dev.berke.app.consumer.event.ProductPublishedEvent;
import dev.berke.app.productsearch.api.dto.SearchAggregation;
import dev.berke.app.productsearch.domain.document.ProductDocument;
import dev.berke.app.productsearch.api.dto.AutocompleteSuggestionResponse;
import dev.berke.app.productsearch.api.dto.ProductSearchRequest;
import dev.berke.app.productsearch.api.dto.ProductSearchResponse;
import dev.berke.app.productsearch.api.dto.ProductSearchResult;
import dev.berke.app.productsearch.domain.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.support.PageableExecutionUtils;
import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public void indexProduct(ProductPublishedEvent event) {
        log.info("INDEX event received for product ID: {}", event.productId());

        ProductDocument.CategoryDocument categoryDocument = ProductDocument.CategoryDocument.builder()
                .id(event.categoryId().toString())
                .name(event.categoryName())
                .build();

        ProductDocument document = ProductDocument.builder()
                .productId(event.productId())
                .productName(event.productName())
                .category(categoryDocument)
                .basePrice(event.basePrice())
                .minPrice(event.minPrice())
                .manufacturer(event.manufacturer())
                .sku(event.sku())
                .status(event.status())
                .createdOn(event.createdOn())
                .build();

        log.info("Document to be indexed: {}", document);

        productSearchRepository.save(document);
        log.info("Successfully indexed product with ID: {}", document.getProductId());
    }

    public void deleteProduct(Integer productId) {
        log.info("DELETE event received for product ID: {}", productId);
        productSearchRepository.deleteById(productId);
        log.info("Successfully deleted product with ID: {}", productId);
    }

    public ProductSearchResponse searchProducts(ProductSearchRequest request) {
        // business logic
        // 1. perform search
        // 2. extract aggregations from search response
        // 3. convert SearchHit<ProductDocument> to a Page<ProductSearchResult>
        // 4. create and return final response object

        log.info("Searching for products with request: {}", request);

        SearchHits<ProductDocument> searchHits = productSearchRepository.search(request);
        log.debug("Search hits retrieved: {}", searchHits.getTotalHits());

        List<SearchAggregation> aggregations = extractAggregations(searchHits);
        log.debug("Aggregations extracted: {}", aggregations);

        Page<ProductSearchResult> page = toProductSearchResultPage(request, searchHits);
        log.debug("Page created with {} elements", page.getNumberOfElements());

        return ProductSearchResponse.from(page, aggregations);
    }

    private Page<ProductSearchResult> toProductSearchResultPage(
            ProductSearchRequest request,
            SearchHits<ProductDocument> searchHits
    ) {
        List<ProductSearchResult> results = searchHits
                .getSearchHits()
                .stream()
                .map(hit -> ProductSearchResult.from(hit.getContent()))
                .toList();

        PageRequest pageable = PageRequest.of(request.page(), request.size());

        return PageableExecutionUtils.getPage(results, pageable, searchHits::getTotalHits);
    }

    private List<SearchAggregation> extractAggregations(
            SearchHits<ProductDocument> searchHits
    ) {
        AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();

        if (aggregationsContainer == null) {
            return List.of();
        }

        ElasticsearchAggregations elasticsearchAggregations =
                (ElasticsearchAggregations) aggregationsContainer;

        List<SearchAggregation> resultAggregations = new ArrayList<>();

        for (ElasticsearchAggregation aggregation : elasticsearchAggregations.aggregations()) {
            Aggregate aggregate = aggregation.aggregation().getAggregate();

            if (aggregate.isSterms()) {
                StringTermsAggregate termsAgg = aggregate.sterms();
                List<SearchAggregation.Bucket> buckets = termsAgg
                        .buckets()
                        .array()
                        .stream()
                        .map(bucket -> new SearchAggregation.Bucket(
                                bucket.key().stringValue(),
                                bucket.docCount())
                        )
                        .toList();

                if (!buckets.isEmpty()) {
                    resultAggregations.add(new SearchAggregation(
                            aggregation.aggregation().getName(),
                            buckets
                            )
                    );
                }
            }
        }
        return resultAggregations;
    }
    
    public AutocompleteSuggestionResponse getAutocompleteSuggestions(String query) {
        List<ProductDocument> results = productSearchRepository
                .findByProductNameAutocomplete(query);

        List<String> suggestions = results.stream()
                .map(ProductDocument::getProductName)
                .distinct()
                .limit(10)
                .toList();
        return new AutocompleteSuggestionResponse(suggestions);
    }
}
