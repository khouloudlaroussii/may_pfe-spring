package com.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatisticsResponse {
    private String year;  // This will hold the month names like "Jan", "Feb", etc.
    private long value;   // This will hold the count of orders for the month
}
