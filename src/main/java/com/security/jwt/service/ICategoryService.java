package com.security.jwt.service;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Category;

import java.util.List;

public interface ICategoryService {
    ApiResponse<Category> createCategory(Category category);
    ApiResponse<List<Category>> getAllCategories();
    ApiResponse<Category> getCategoryById(Integer id);
    ApiResponse<Category> updateCategory(Integer id, Category category);
    ApiResponse<Void> deleteCategory(Integer id);
    ApiResponse<Long> countCategories();

}
