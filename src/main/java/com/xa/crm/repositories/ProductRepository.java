package com.xa.crm.repositories;

import com.xa.crm.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByIsActiveTrue();
    List<Product> findByCategory(Product.ProductCategory category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryAndIsActiveTrue(Product.ProductCategory category);
}
