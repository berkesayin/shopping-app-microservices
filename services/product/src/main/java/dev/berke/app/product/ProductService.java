package dev.berke.app.product;

import dev.berke.app.constants.ProductConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse createProduct(ProductRequest productRequest) {
        var product = productMapper.toProduct(productRequest);
        var savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    public ProductResponse getProductById(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        ProductConstants.PRODUCT_NOT_FOUND_MESSAGE + productId
                ));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    public Integer getCategoryIdOfProduct(Integer productId) {
        return productRepository.findById(productId)
                .map(product -> product.getCategory().getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                        ProductConstants.PRODUCT_NOT_FOUND_MESSAGE + productId
                ));
    }
}
