package com.security.jwt.repository;

import com.security.jwt.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderLineRepository extends JpaRepository<OrderLine, Integer> {
}
