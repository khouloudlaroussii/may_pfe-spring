package com.security.jwt.repository;

import com.security.jwt.model.Category;
import com.security.jwt.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(Category category);

}
