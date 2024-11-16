package com.security.jwt.service.impl;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Category;
import com.security.jwt.model.Product;
import com.security.jwt.repository.ICategoryRepository;
import com.security.jwt.repository.IProductRepository;
import com.security.jwt.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    @Override
    public ApiResponse<Product> createProduct(Product product) {
        try {
            Product createdProduct = productRepository.save(product);
            return new ApiResponse<>(false, "Product created successfully!", createdProduct);
        } catch (DataIntegrityViolationException ex) {
            return new ApiResponse<>(true, "Invalid product data: " + ex.getMostSpecificCause().getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<List<Product>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return new ApiResponse<>(false, "Products retrieved successfully!", products);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Product> getProductById(Integer id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
            return new ApiResponse<>(false, "Product retrieved successfully!", product);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Product> updateProduct(Integer id, Product productDetails) {
        try {
            Product product = getProductById(id).getRows(); // Extract product from the response
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setTva(productDetails.getTva());
            product.setCategory(productDetails.getCategory());
            product.setImagePath(productDetails.getImagePath());
            product.setDescription(productDetails.getDescription());

            Product updatedProduct = productRepository.save(product);
            return new ApiResponse<>(false, "Product updated successfully!", updatedProduct);
        } catch (DataIntegrityViolationException ex) {
            return new ApiResponse<>(true, "Invalid product data: " + ex.getMostSpecificCause().getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Void> deleteProduct(Integer id) {
        try {
            Product product = getProductById(id).getRows(); // Extract product from the response
            productRepository.delete(product);
            return new ApiResponse<>(false, "Product deleted successfully!", null);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<List<Product>> getProductsByCategory(Integer categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

            List<Product> products = productRepository.findByCategory(category);
            return new ApiResponse<>(false, "Products retrieved successfully!", products);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Long> countProducts() {
        try {
            long productCount = productRepository.count();  // Use the repository's count method
            return new ApiResponse<>(false, "Product count retrieved successfully!", productCount);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving product count.", null);
        }
    }
}
