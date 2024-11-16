package com.security.jwt.service.impl;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.dto.OrderStatisticsResponse;
import com.security.jwt.dto.StatusCountDto;
import com.security.jwt.model.*;
import com.security.jwt.repository.IOrderLineRepository;
import com.security.jwt.repository.IOrderRepository;
import com.security.jwt.repository.IProductRepository;
import com.security.jwt.repository.IUserRepository;
import com.security.jwt.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderLineRepository orderLineRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    @Override
    public ApiResponse<Order> createOrder(Order order) {
        try {
            // Check if the user is provided and exists in the database
            if (order.getUser() == null || order.getUser().getId() == null) {
                throw new IllegalArgumentException("User must be provided for the order.");
            }
            System.out.println("User provided: " + order.getUser().getId());


            // Fetch the user from the database using the ID from the request
            User user = userRepository.findById(order.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + order.getUser().getId()));
            order.setUser(user);  // Set the user in the order object

            // Check if a deliveryMan is provided, and ensure it exists
            if (order.getDeliveryMan() != null && order.getDeliveryMan().getId() != null) {
                User deliveryMan = userRepository.findById(order.getDeliveryMan().getId())
                        .orElseThrow(() -> new IllegalArgumentException("DeliveryMan not found with ID: " + order.getDeliveryMan().getId()));
                order.setDeliveryMan(deliveryMan);  // Set the delivery man in the order object
            }

            // Process order lines and calculate totals
            for (OrderLine orderLine : order.getOrderLines()) {
                Product product = productRepository.findById(orderLine.getProduct().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + orderLine.getProduct().getId()));

                orderLine.setProduct(product);
                orderLine.setOrder(order);
                orderLine.calculateTotals();
            }

            // Calculate total prices for the order
            order.calculateTotalPrices();

            // Save the order in the database
            Order savedOrder = orderRepository.save(order);

            // Return a success response
            return new ApiResponse<>(false, "Order created successfully!", savedOrder);

        } catch (IllegalArgumentException ex) {
            // Return specific error message when validation fails
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            // Log the error and return a general error message
            ex.printStackTrace();
            return new ApiResponse<>(true, "An unexpected error occurred: " + ex.getMessage(), null);
        }
    }



    @Override
    public ApiResponse<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return new ApiResponse<>(false, "Orders retrieved successfully!", orders);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving orders.", null);
        }
    }

    @Override
    public ApiResponse<Order> getOrderById(Integer id) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
            return new ApiResponse<>(false, "Order retrieved successfully!", order);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving the order.", null);
        }
    }

    @Override
    public ApiResponse<Order> updateOrder(Integer id, Order orderDetails) {
        try {
            Order existingOrder = getOrderById(id).getRows();
            for (OrderLine orderLine : orderDetails.getOrderLines()) {
                if (orderLine.getId() == null) {
                    orderLine.setOrder(existingOrder);
                    orderLine.calculateTotals();
                    orderLineRepository.save(orderLine);
                } else {
                    OrderLine existingOrderLine = orderLineRepository.findById(orderLine.getId())
                            .orElseThrow(() -> new IllegalArgumentException("OrderLine not found"));
                    existingOrderLine.setQuantity(orderLine.getQuantity());
                    existingOrderLine.setProduct(orderLine.getProduct());
                    existingOrderLine.calculateTotals();
                    orderLineRepository.save(existingOrderLine);
                }
            }
            existingOrder.calculateTotalPrices();
            Order updatedOrder = orderRepository.save(existingOrder);
            return new ApiResponse<>(false, "Order updated successfully!", updatedOrder);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while updating the order.", null);
        }
    }

    @Override
    public ApiResponse<Void> deleteOrder(Integer id) {
        try {
            Order existingOrder = getOrderById(id).getRows();
            orderRepository.delete(existingOrder);
            return new ApiResponse<>(false, "Order deleted successfully!", null);
        } catch (IllegalArgumentException ex) {
            return new ApiResponse<>(true, ex.getMessage(), null);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while deleting the order.", null);
        }
    }

    @Override
    public ApiResponse<List<Order>> getOrdersByUserId(Integer userId) {
        try {
            // Fetch the user first to ensure they exist
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            // Retrieve the orders for the specific user
            List<Order> orders = orderRepository.findByUser(user);

            if (orders.isEmpty()) {
                return new ApiResponse<>(false, "No orders found for this user.", orders);
            }

            return new ApiResponse<>(false, "Orders retrieved successfully!", orders);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving orders for the user.", null);
        }
    }

    @Override
    public ApiResponse<Order> updateOrderStatus(Integer id, Integer status) {
        try {
            Order order = getOrderById(id).getRows();  // Get the order by its ID
            order.setStatus(status);  // Update the status
            Order updatedOrder = orderRepository.save(order);  // Save the updated order
            return new ApiResponse<>(false, "Order status updated successfully!", updatedOrder);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while updating the order status.", null);
        }
    }


    @Override
    public ApiResponse<Order> assignDeliveryMan(Integer id, Integer deliveryManId, BigDecimal deliveryFee) {
        try {
            Order order = getOrderById(id).getRows();  // Get the order by its ID
            User deliveryMan = userRepository.findById(deliveryManId)
                    .orElseThrow(() -> new IllegalArgumentException("Delivery man not found"));

            if (!deliveryMan.getRole().equals(Role.DELIVERYMAN)) {
                return new ApiResponse<>(true, "The user is not a delivery man.", null);
            }

            order.setDeliveryMan(deliveryMan);

            order.setDeliveryFee(deliveryFee);
            order.setStatus(1);
            order.calculateTotalPrices();

            // Save the updated order
            Order updatedOrder = orderRepository.save(order);
            return new ApiResponse<>(false, "Delivery man and delivery fee updated successfully!", updatedOrder);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while updating the delivery man.", null);
        }
    }


    @Override
    public ApiResponse<List<Order>> getOrdersByDeliveryMan(Integer deliveryManId) {
        try {
            User deliveryMan = userRepository.findById(deliveryManId)
                    .orElseThrow(() -> new IllegalArgumentException("Delivery Man not found"));

            List<Order> orders = orderRepository.findByDeliveryMan(deliveryMan);
            return new ApiResponse<>(false, "Orders retrieved successfully", orders);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "Error retrieving orders", null);
        }
    }

    @Override
    public ApiResponse<List<StatusCountDto>> getOrderCountsByStatus() {
        try {
            List<StatusCountDto> statusCounts = List.of(
                    new StatusCountDto("Pending", orderRepository.countByStatus(0)),
                    new StatusCountDto("Shipped", orderRepository.countByStatus(1)),
                    new StatusCountDto("Delivered", orderRepository.countByStatus(2)),
                    new StatusCountDto("Canceled", orderRepository.countByStatus(3)),
                    new StatusCountDto("Received", orderRepository.countByStatus(4))
            );

            return new ApiResponse<>(false, "Order counts by status retrieved successfully!", statusCounts);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving status counts.", null);
        }
    }

    @Override
    public ApiResponse<List<StatusCountDto>> getCountForUserOrderes(Integer userId) {
        try {
            List<StatusCountDto> statusCounts = List.of(
                    new StatusCountDto("Pending", orderRepository.countByStatusAndUserId(0, userId)),
                    new StatusCountDto("Shipped", orderRepository.countByStatusAndUserId(1, userId)),
                    new StatusCountDto("Delivered", orderRepository.countByStatusAndUserId(2, userId)),
                    new StatusCountDto("Canceled", orderRepository.countByStatusAndUserId(3, userId)),
                    new StatusCountDto("Received", orderRepository.countByStatusAndUserId(4, userId))
            );

            return new ApiResponse<>(false, "Order counts by status and Client Id retrieved successfully!", statusCounts);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving status and Client Id counts.", null);
        }
    }

    @Override
    public ApiResponse<List<StatusCountDto>> getCountForlivreurOrderes(Integer delivery_man_id) {
        try {
            List<StatusCountDto> statusCounts = List.of(
                    new StatusCountDto("Pending", orderRepository.countByDeliveryManAndStatus( delivery_man_id)),
                    new StatusCountDto("Shipped", orderRepository.countByDeliveryManAndStatus(delivery_man_id)),
                    new StatusCountDto("Delivered", orderRepository.countByDeliveryManAndStatus( delivery_man_id)),
                    new StatusCountDto("Canceled", orderRepository.countByDeliveryManAndStatus( delivery_man_id)),
                    new StatusCountDto("Received", orderRepository.countByDeliveryManAndStatus(delivery_man_id))
            );

            return new ApiResponse<>(false, "Order counts by status and delivery man id retrieved successfully!", statusCounts);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving status and delivery man id counts.", null);
        }
    }

    @Override
    public ApiResponse<List<OrderStatisticsResponse>> getOrderStatisticsByYearAndStatus(int year, Integer status) {
        try {
            List<OrderStatisticsResponse> statistics = new ArrayList<>();
            for (int month = 1; month <= 12; month++) {
                long orderCount;
                if (status != null) {
                    // Fetch order count by month, year, and status
                    orderCount = orderRepository.countByMonthYearAndStatus(month, year, status);
                } else {
                    // Fetch order count by month and year (all statuses)
                    orderCount = orderRepository.countByMonthAndYear(month, year);
                }
                String monthName = getMonthName(month);  // Helper method to get the month name
                statistics.add(new OrderStatisticsResponse(monthName, orderCount));
            }
            return new ApiResponse<>(false, "Order statistics retrieved successfully!", statistics);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving order statistics.", null);
        }
    }

    private String getMonthName(int month) {
        switch (month) {
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return "";
        }
    }

}
