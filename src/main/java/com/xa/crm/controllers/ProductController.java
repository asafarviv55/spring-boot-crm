package com.xa.crm.controllers;

import com.xa.crm.models.Product;
import com.xa.crm.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestParam String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String description,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) BigDecimal cost,
            @RequestParam(required = false) Product.ProductCategory category) {
        return ResponseEntity.ok(productService.createProduct(name, sku, description, price, cost, category));
    }

    @PutMapping("/{id}/pricing")
    public ResponseEntity<Product> updatePricing(
            @PathVariable Long id,
            @RequestParam BigDecimal price,
            @RequestParam(required = false) BigDecimal cost) {
        return ResponseEntity.ok(productService.updatePricing(id, price, cost));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(productService.updateStock(id, quantity));
    }

    @PutMapping("/{id}/tax-rate")
    public ResponseEntity<Product> setTaxRate(@PathVariable Long id, @RequestParam BigDecimal taxRate) {
        return ResponseEntity.ok(productService.setTaxRate(id, taxRate));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Product> activate(@PathVariable Long id) {
        return ResponseEntity.ok(productService.activateProduct(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Product> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deactivateProduct(id));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Product.ProductCategory category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> getBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }
}
