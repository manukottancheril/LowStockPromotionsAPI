package com.example.LowStockPromotionsAPI.model;

import jakarta.validation.Valid;
import java.util.List;

public record ProductsDto(@Valid List<ProductDto> products) {

}
