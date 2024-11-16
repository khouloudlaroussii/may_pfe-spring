package com.security.jwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Set default values to avoid null issues
    private BigDecimal price = BigDecimal.ZERO;  // Default to 0 to avoid null price
    private BigDecimal tva = BigDecimal.ZERO;    // Default to 0 to avoid null tva

    // New attributes for imagePath and description
    private String imagePath;  // Path to the product image
    private String description;  // Description of the product

    // Method to get price with TVA
    public BigDecimal getPriceWithTva() {
        // Ensure price and tva are not null before calculating
        return price.add(price.multiply(tva));
    }
}
