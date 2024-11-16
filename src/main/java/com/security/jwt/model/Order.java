package com.security.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "delivery_man_id", nullable = true)
    private User deliveryMan;  // The delivery man (linking to User table)

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLine> orderLines;

    private BigDecimal totalPriceHT;
    private BigDecimal totalVatAmount;
    private BigDecimal totalPriceTTC;

    // New delivery fee attribute with default value 0
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    private Integer status;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        this.status = 0;  // Default status is 0 (pending)
    }

    // Update this method to include delivery fee in total calculation
    public void calculateTotalPrices() {
        this.totalPriceHT = orderLines.stream()
                .map(OrderLine::getTotalPriceHT)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalVatAmount = orderLines.stream()
                .map(OrderLine::getTotalVatAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Add deliveryFee to the totalPriceTTC calculation
        this.totalPriceTTC = orderLines.stream()
                .map(OrderLine::getTotalPriceTTC)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(deliveryFee);  // Include delivery fee
    }
}
