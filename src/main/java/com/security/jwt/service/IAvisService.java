package com.security.jwt.service;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Avis;
import java.util.List;

public interface IAvisService {
    ApiResponse<Avis> createAvis(Avis avis);
    ApiResponse<List<Avis>> getAvisByOrder(Integer orderId);
    ApiResponse<List<Avis>> getAvisByDeliveryMan(Integer deliveryManId);
    ApiResponse<List<Avis>> getAvisByUser(Integer userId);  // New method for getting avis by user
    ApiResponse<List<Avis>> getAllAvis();

}
