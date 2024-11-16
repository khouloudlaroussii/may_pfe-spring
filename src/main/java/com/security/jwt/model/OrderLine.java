package com.security.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_line")
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    private BigDecimal totalPriceHT;
    private BigDecimal totalVatAmount;
    private BigDecimal totalPriceTTC;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    public void calculateTotals() {
        // Default to zero if price or tva is null to avoid null pointer exceptions
        BigDecimal unitPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        BigDecimal vatRate = product.getTva() != null ? product.getTva() : BigDecimal.ZERO;

        // Default quantity to 1 if it's null
        int validQuantity = quantity != null ? quantity : 1;

        // Calculate total price without VAT (Net)
        this.totalPriceHT = unitPrice.multiply(new BigDecimal(validQuantity));

        // Calculate VAT amount
        this.totalVatAmount = this.totalPriceHT.multiply(vatRate);

        // Calculate total price with VAT (Gross)
        this.totalPriceTTC = this.totalPriceHT.add(this.totalVatAmount);
    }
}
