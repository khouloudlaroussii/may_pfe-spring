package com.security.jwt.service;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Product;
import java.util.List;

public interface IProductService {
    ApiResponse<Product> createProduct(Product product);
    ApiResponse<List<Product>> getAllProducts();
    ApiResponse<Product> getProductById(Integer id);
    ApiResponse<Product> updateProduct(Integer id, Product product);
    ApiResponse<Void> deleteProduct(Integer id);
    ApiResponse<List<Product>> getProductsByCategory(Integer categoryId);
    ApiResponse<Long> countProducts();

}
