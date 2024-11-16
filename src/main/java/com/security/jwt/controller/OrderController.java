package com.security.jwt.controller;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.OrderStatisticsResponse;
import com.security.jwt.dto.StatusCountDto;
import com.security.jwt.model.Order;
import com.security.jwt.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody Order order) {
        ApiResponse<Order> response = orderService.createOrder(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUserId(@PathVariable Integer userId) {
        ApiResponse<List<Order>> response = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        ApiResponse<List<Order>> response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Integer id) {
        ApiResponse<Order> response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> updateOrder(
            @PathVariable Integer id, @RequestBody Order orderDetails) {
        ApiResponse<Order> response = orderService.updateOrder(id, orderDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Integer id) {
        ApiResponse<Void> response = orderService.deleteOrder(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(@PathVariable Integer id, @PathVariable Integer status) {
        ApiResponse<Order> response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deliveryman/{deliveryManId}/{deliveryFee}")
    public ResponseEntity<ApiResponse<Order>> updateOrderDeliveryMan(
            @PathVariable Integer id,
            @PathVariable Integer deliveryManId,
            @PathVariable BigDecimal deliveryFee) {

        ApiResponse<Order> response = orderService.assignDeliveryMan(id, deliveryManId, deliveryFee);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deliveryman/{deliveryManId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByDeliveryMan(@PathVariable Integer deliveryManId) {
        ApiResponse<List<Order>> response = orderService.getOrdersByDeliveryMan(deliveryManId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/counts-by-status")
    public ResponseEntity<ApiResponse<List<StatusCountDto>>> getOrderCountsByStatus() {
        ApiResponse<List<StatusCountDto>> response = orderService.getOrderCountsByStatus();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/{year}")
    public ResponseEntity<ApiResponse<List<OrderStatisticsResponse>>> getOrderStatistics(
            @PathVariable int year, @RequestParam(value = "status", required = false) Integer status) {
        ApiResponse<List<OrderStatisticsResponse>> response = orderService.getOrderStatisticsByYearAndStatus(year, status);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/userCount/{userId}")
    public ResponseEntity<ApiResponse<List<StatusCountDto>>> getOrdersByUserIdAndStatus(@PathVariable Integer userId) {
        ApiResponse<List<StatusCountDto>> response = orderService.getCountForUserOrderes(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deliveryByStatusCount/{delivery_man_id}")
    public ResponseEntity<ApiResponse<List<StatusCountDto>>> getCountForlivreurOrderes(@PathVariable Integer delivery_man_id) {
        ApiResponse<List<StatusCountDto>> response = orderService.getCountForlivreurOrderes(delivery_man_id);
        return ResponseEntity.ok(response);
    }
}
