package com.security.jwt.repository;

import com.security.jwt.model.Order;
import com.security.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user);
    List<Order> findByDeliveryMan(User deliveryMan);
    long countByStatus(int status);
    long countByStatusAndUserId(int status, int userId);
    // and o.deliveryMan.id = :delivery_man_id GROUP BY o.status
    @Query("SELECT o.status, COUNT(o.id) AS count FROM Order o JOIN User u ON o.deliveryMan.id = u.id WHERE u.role = 2 AND o.deliveryMan.id = :delivery_man_id GROUP BY o.status")
    long countByDeliveryManAndStatus(@Param("delivery_man_id") int delivery_man_id);

    @Query("SELECT COUNT(o) FROM Order o WHERE MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year")
    long countByMonthAndYear(@Param("month") int month, @Param("year") int year);

    // Count by month, year, and status
    @Query("SELECT COUNT(o) FROM Order o WHERE MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year AND o.status = :status")
    long countByMonthYearAndStatus(@Param("month") int month, @Param("year") int year, @Param("status") int status);

}
