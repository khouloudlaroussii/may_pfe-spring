package com.security.jwt.repository;

import com.security.jwt.model.Avis;
import com.security.jwt.model.Order;
import com.security.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAvisRepository extends JpaRepository<Avis, Integer> {
    List<Avis> findByOrder(Order order);
    List<Avis> findByDeliveryMan(User deliveryMan);
    List<Avis> findByUser(User user);  // New method to find avis by user
}
