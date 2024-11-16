package com.security.jwt.service.impl;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Avis;
import com.security.jwt.model.Order;
import com.security.jwt.model.User;
import com.security.jwt.repository.IAvisRepository;
import com.security.jwt.repository.IOrderRepository;
import com.security.jwt.repository.IUserRepository;
import com.security.jwt.service.IAvisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvisServiceImpl implements IAvisService {

    private final IAvisRepository avisRepository;
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;

    @Override
    public ApiResponse<Avis> createAvis(Avis avis) {
        try {
            // Ensure user exists
            User user = userRepository.findById(avis.getUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + avis.getUser().getId()));

            // Ensure delivery man exists
            User deliveryMan = userRepository.findById(avis.getDeliveryMan().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Delivery Man not found with ID: " + avis.getDeliveryMan().getId()));

            // Ensure order exists
            Order order = orderRepository.findById(avis.getOrder().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + avis.getOrder().getId()));

            // Save Avis
            avis.setUser(user);
            avis.setDeliveryMan(deliveryMan);
            avis.setOrder(order);
            Avis savedAvis = avisRepository.save(avis);

            return new ApiResponse<>(false, "Avis created successfully!", savedAvis);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while creating the avis: " + ex.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<Avis>> getAvisByOrder(Integer orderId) {
        try {
            // Ensure order exists
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

            // Get Avis for the order
            List<Avis> avisList = avisRepository.findByOrder(order);
            return new ApiResponse<>(false, "Avis for order retrieved successfully!", avisList);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving avis for the order: " + ex.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<Avis>> getAvisByDeliveryMan(Integer deliveryManId) {
        try {
            // Ensure delivery man exists
            User deliveryMan = userRepository.findById(deliveryManId)
                    .orElseThrow(() -> new IllegalArgumentException("Delivery Man not found with ID: " + deliveryManId));

            // Get Avis for the delivery man
            List<Avis> avisList = avisRepository.findByDeliveryMan(deliveryMan);
            return new ApiResponse<>(false, "Avis for delivery man retrieved successfully!", avisList);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving avis for the delivery man: " + ex.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<Avis>> getAvisByUser(Integer userId) {  // New method for getting avis by user
        try {
            // Ensure user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            // Get Avis for the user
            List<Avis> avisList = avisRepository.findByUser(user);
            return new ApiResponse<>(false, "Avis for user retrieved successfully!", avisList);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving avis for the user: " + ex.getMessage(), null);
        }
    }


    @Override
    public ApiResponse<List<Avis>> getAllAvis() {
        try {
            List<Avis> allAvis = avisRepository.findAll();
            return new ApiResponse<>(false, "All avis retrieved successfully!", allAvis);
        } catch (Exception ex) {
            return new ApiResponse<>(true, "An error occurred while retrieving all avis.", null);
        }
    }
}
