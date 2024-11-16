package com.security.jwt.controller;

import com.security.jwt.dto.ApiResponse;
import com.security.jwt.model.Avis;
import com.security.jwt.service.IAvisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/avis")
@RequiredArgsConstructor
public class AvisController {

    private final IAvisService avisService;

    @PostMapping
    public ResponseEntity<ApiResponse<Avis>> createAvis(@Valid @RequestBody Avis avis) {
        ApiResponse<Avis> response = avisService.createAvis(avis);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<Avis>>> getAvisByOrder(@PathVariable Integer orderId) {
        ApiResponse<List<Avis>> response = avisService.getAvisByOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deliveryman/{deliveryManId}")
    public ResponseEntity<ApiResponse<List<Avis>>> getAvisByDeliveryMan(@PathVariable Integer deliveryManId) {
        ApiResponse<List<Avis>> response = avisService.getAvisByDeliveryMan(deliveryManId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")  // New endpoint for getting avis by user
    public ResponseEntity<ApiResponse<List<Avis>>> getAvisByUser(@PathVariable Integer userId) {
        ApiResponse<List<Avis>> response = avisService.getAvisByUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Avis>>> getAllAvis() {
        ApiResponse<List<Avis>> response = avisService.getAllAvis();
        return ResponseEntity.ok(response);
    }
}

