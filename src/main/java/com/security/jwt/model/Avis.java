package com.security.jwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "avis")
@NoArgsConstructor
@AllArgsConstructor
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user who gives the review

    @ManyToOne
    @JoinColumn(name = "delivery_man_id", nullable = false)
    private User deliveryMan; // The delivery man being reviewed

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // The order related to the review

    @Column(nullable = false)
    private String comment; // The comment about the delivery

    @Column(nullable = false)
    private Integer rate; // The rating (number) for the delivery

}
