package com.security.jwt.service;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.OrderStatisticsResponse;
import com.security.jwt.dto.StatusCountDto;
import com.security.jwt.model.Order;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {
    ApiResponse<Order> createOrder(Order order);
    ApiResponse<List<Order>> getAllOrders();
    ApiResponse<Order> getOrderById(Integer id);
    ApiResponse<Order> updateOrder(Integer id, Order order);
    ApiResponse<Void> deleteOrder(Integer id);
    ApiResponse<List<Order>> getOrdersByUserId(Integer userId);
    ApiResponse<Order> updateOrderStatus(Integer id, Integer status);
    ApiResponse<Order> assignDeliveryMan(Integer orderId, Integer deliveryManId, BigDecimal deliveryFee);
    ApiResponse<List<Order>> getOrdersByDeliveryMan(Integer deliveryManId);
    ApiResponse<List<StatusCountDto>> getOrderCountsByStatus();
    ApiResponse<List<OrderStatisticsResponse>> getOrderStatisticsByYearAndStatus(int year, Integer status);
    ApiResponse<List<StatusCountDto>> getCountForUserOrderes(Integer id);
    ApiResponse<List<StatusCountDto>> getCountForlivreurOrderes(Integer id);

}
