package com.example.LowStockPromotionsAPI.service;

import com.example.LowStockPromotionsAPI.exception.InvalidInputException;
import com.example.LowStockPromotionsAPI.model.ProductDto;
import com.example.LowStockPromotionsAPI.model.ProductsDto;
import com.example.LowStockPromotionsAPI.model.PromotionDto;
import com.example.LowStockPromotionsAPI.model.PromotionsDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.LowStockPromotionsAPI.utils.StockPromotionConstants.*;

@Service
public class StockPromotionService {

    public PromotionsDto processExcessStock(ProductsDto productsDto) {

        if (productsDto == null || productsDto.products().isEmpty()) {
            throw new InvalidInputException("Invalid input");
        }
        List<PromotionDto> promotionList = productsDto.products().stream().filter(this::isPromotionEligible)
                .map(productDto -> {
                    Integer excessQuantity = productDto.currentStock() - productDto.minRequiredStock();
                    String discount = findDiscount(excessQuantity);
                    return new PromotionDto(productDto.id(), productDto.name(), excessQuantity, discount);
                }).sorted(Comparator.comparing((PromotionDto p) -> Double.parseDouble(p.discount().replace("%", ""))).reversed().thenComparing(p->p.name().toLowerCase()))
                .collect(Collectors.toList());
        return new PromotionsDto(promotionList);
    }

    //filter condition for promotion
    boolean isPromotionEligible(ProductDto productDto) {
        if (productDto == null || productDto.minRequiredStock() <= 0) {
            throw new InvalidInputException("Invalid input");
        }
        return productDto.currentStock() > STOCK_MULTIPLICATION_FACTOR * productDto.minRequiredStock();
    }

    //find discount for exxcessQuantity
    String findDiscount(Integer excessQuantity) {
        if (excessQuantity == null || excessQuantity < 0) {
            throw new InvalidInputException("Invalid input");
        }
        String discount = DEFAULT_DISCOUNT0;
        if (excessQuantity > EXCESS_QUANTITY100) {
            discount = DISCOUNT30;
        } else if (EXCESS_QUANTITY50 <= excessQuantity && excessQuantity <= EXCESS_QUANTITY99) {
            discount = DISCOUNT20;
        } else if (excessQuantity < EXCESS_QUANTITY50) {
            discount = DISCOUNT10;
        }
        return discount;
    }
}
