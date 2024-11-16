package com.security.jwt.controller;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Product;
import com.security.jwt.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createPUserroduct(@Valid @RequestBody Product product) {
        ApiResponse<Product> response = productService.createProduct(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        ApiResponse<List<Product>> response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Integer id) {
        ApiResponse<Product> response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Integer id, @Valid @RequestBody Product product) {
        ApiResponse<Product> response = productService.updateProduct(id, product);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        ApiResponse<Void> response = productService.deleteProduct(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<Product>>> getProductsByCategory(@PathVariable Integer categoryId) {
        ApiResponse<List<Product>> response = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countProducts() {
        ApiResponse<Long> response = productService.countProducts();
        return ResponseEntity.ok(response);
    }
}
