package com.example.LowStockPromotionsAPI.controller;

import com.example.LowStockPromotionsAPI.model.ProductsDto;
import com.example.LowStockPromotionsAPI.model.PromotionsDto;
import com.example.LowStockPromotionsAPI.service.StockPromotionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockPromotionController {

    StockPromotionService stockPromotionService;

    public StockPromotionController(StockPromotionService stockPromotionService) {
        this.stockPromotionService = stockPromotionService;
    }

    @PostMapping("/inventory/promotion")
    public PromotionsDto processStockPromotion(@RequestBody @Valid ProductsDto productsDto) {
        return stockPromotionService.processExcessStock(productsDto);
    }
}
