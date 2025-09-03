package com.example.LowStockPromotionsAPI.model;

public record PromotionDto(Integer id,
                           String name,
                           Integer excessQuantity,
                           String discount) {
}
