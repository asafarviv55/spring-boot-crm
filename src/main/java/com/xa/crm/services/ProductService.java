package com.xa.crm.services;

import com.xa.crm.models.Product;
import com.xa.crm.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String name, String sku, String description,
                                 BigDecimal price, BigDecimal cost, Product.ProductCategory category) {
        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
        product.setDescription(description);
        product.setPrice(price);
        product.setCost(cost);
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product updatePricing(Long productId, BigDecimal price, BigDecimal cost) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setPrice(price);
        product.setCost(cost);

        return productRepository.save(product);
    }

    public Product updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }

    public Product setTaxRate(Long productId, BigDecimal taxRate) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setTaxRate(taxRate);
        return productRepository.save(product);
    }

    public Product activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(true);
        return productRepository.save(product);
    }

    public Product deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false);
        return productRepository.save(product);
    }

    public List<Product> getActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public List<Product> getProductsByCategory(Product.ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public Product getProductBySku(String sku) {
        return productRepository.findBySku(sku).orElse(null);
    }
}
