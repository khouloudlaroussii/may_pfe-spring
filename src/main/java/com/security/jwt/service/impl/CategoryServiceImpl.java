package com.security.jwt.service.impl;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Category;
import com.security.jwt.repository.ICategoryRepository;
import com.security.jwt.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    @Override
    public ApiResponse<Category> createCategory(Category category) {
        try {
            Category createdCategory = categoryRepository.save(category);
            return new ApiResponse<>(false, "Category created successfully!", createdCategory);
        } catch (DataIntegrityViolationException ex) {
            return new ApiResponse<>(true, "Category name must not be null.", null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            return new ApiResponse<>(false, "Categories retrieved successfully!", categories);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Category> getCategoryById(Integer id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
            return new ApiResponse<>(false, "Category retrieved successfully!", category);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Category> updateCategory(Integer id, Category categoryDetails) {
        try {
            Category category = getCategoryById(id).getRows();  // Extract the category object from the response
            category.setName(categoryDetails.getName());
            Category updatedCategory = categoryRepository.save(category);
            return new ApiResponse<>(false, "Category updated successfully!", updatedCategory);
        } catch (DataIntegrityViolationException ex) {
            return new ApiResponse<>(true, "Category name must not be null.", null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Void> deleteCategory(Integer id) {
        try {
            Category category = getCategoryById(id).getRows();
            categoryRepository.delete(category);
            return new ApiResponse<>(false, "Category deleted successfully!", null);
        } catch (DataIntegrityViolationException ex) {
            return new ApiResponse<>(true, "Cannot delete category. It is referenced by one or more products.", null);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An unexpected error occurred.", null);
        }
    }

    @Override
    public ApiResponse<Long> countCategories() {
        try {
            long categoryCount = categoryRepository.count();  // Use the repository's count method
            return new ApiResponse<>(false, "Category count retrieved successfully!", categoryCount);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving category count.", null);
        }
    }
}

