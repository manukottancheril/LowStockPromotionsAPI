package com.example.LowStockPromotionsAPI.model;

import jakarta.validation.constraints.NotNull;

public record ProductDto(@NotNull(message = "Id cannot be null") Integer id,
                         @NotNull(message = "Name cannot be null") String name,
                         @NotNull(message = "CurrentStock cannot be null") Integer currentStock,
                         @NotNull(message = "MinRequiredStock cannot be null") Integer minRequiredStock) {
}
