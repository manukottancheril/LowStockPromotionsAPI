package com.example.LowStockPromotionsAPI.service;

import com.example.LowStockPromotionsAPI.exception.InvalidInputException;
import com.example.LowStockPromotionsAPI.model.ProductDto;
import com.example.LowStockPromotionsAPI.model.ProductsDto;
import com.example.LowStockPromotionsAPI.model.PromotionDto;
import com.example.LowStockPromotionsAPI.model.PromotionsDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.LowStockPromotionsAPI.utils.StockPromotionConstants.*;

public class StockPromotionServiceTest {

    StockPromotionService stockPromotionService;

    @BeforeEach
    public void setUp() {
        stockPromotionService = new StockPromotionService();
    }

    @Test
    public void isPromotionEligiblePass() {
        ProductDto productDto = new ProductDto(1, "Laptop Bag", 200, 80);
        boolean response = stockPromotionService.isPromotionEligible(productDto);
        Assertions.assertTrue(response);
    }

    @Test
    public void isPromotionEligibleFail() {
        ProductDto productDto = new ProductDto(2, "Mouse", 20, 10);
        boolean response = stockPromotionService.isPromotionEligible(productDto);
        Assertions.assertFalse(response);
    }

    //productDto is null
    @Test
    public void isPromotionEligible_productIsNull() {
        String message = Assertions.assertThrows(InvalidInputException.class, () -> stockPromotionService.isPromotionEligible(null)).getMessage();
        Assertions.assertEquals("Invalid input", message);
    }

    @Test
    public void isPromotionEligible_minRequiredStockZero() {
        ProductDto productDto = new ProductDto(2, "Mouse", 20, 0);
        String message = Assertions.assertThrows(InvalidInputException.class, () -> stockPromotionService.isPromotionEligible(productDto)).getMessage();
        Assertions.assertEquals("Invalid input", message);
    }

    @Test
    public void isPromotionEligible_minRequiredStockLessThanZero() {
        ProductDto productDto = new ProductDto(2, "Mouse", 20, -1);
        String message = Assertions.assertThrows(InvalidInputException.class, () -> stockPromotionService.isPromotionEligible(productDto)).getMessage();
        Assertions.assertEquals("Invalid input", message);
    }

    @Test
    public void findDiscount_excessQuantityGreaterThan100(){
        Integer excessQuantity = 120;
        String discount = stockPromotionService.findDiscount(excessQuantity);
        Assertions.assertEquals(DISCOUNT30, discount);
    }

    @Test
    public void findDiscount_excessQuantityBetween50and99(){
        Integer excessQuantity = 55;
        String discount = stockPromotionService.findDiscount(excessQuantity);
        Assertions.assertEquals(DISCOUNT20, discount);
    }

    @Test
    public void findDiscount_excessQuantityEquals50(){
        Integer excessQuantity = 50;
        String discount = stockPromotionService.findDiscount(excessQuantity);
        Assertions.assertEquals(DISCOUNT20, discount);
    }

    @Test
    public void findDiscount_excessQuantityEquals99(){
        Integer excessQuantity = 99;
        String discount = stockPromotionService.findDiscount(excessQuantity);
        Assertions.assertEquals(DISCOUNT20, discount);
    }

    @Test
    public void findDiscount_excessQuantityLessThan50(){
        Integer excessQuantity = 40;
        String discount = stockPromotionService.findDiscount(excessQuantity);
        Assertions.assertEquals(DISCOUNT10, discount);
    }

    @Test
    public void findDiscount_excessQuantityisNull(){
        String response = Assertions.assertThrows(InvalidInputException.class, () -> stockPromotionService.findDiscount(null)).getMessage();
        Assertions.assertEquals("Invalid input", response);
    }

    @Test
    public void findDiscount_excessQuantityLessThanZero(){
        Integer excessQuantity = -1;
        String response = Assertions.assertThrows(InvalidInputException.class, () -> stockPromotionService.findDiscount(excessQuantity)).getMessage();
        Assertions.assertEquals("Invalid input", response);
    }

    @Test
    public void processExcessStockPass(){

        //Input
        ProductDto productDto1 = new ProductDto(1,"Laptop Bag",200,80);
        ProductDto productDto2 = new ProductDto(2,"Mouse",20,10);
        ProductDto productDto3 = new ProductDto(3,"Keyboard",60,20);
        ProductDto productDto4 = new ProductDto(4,"Charger",25,15);
        ProductDto productDto5 = new ProductDto(5,"USB",70,10);
        List<ProductDto> productList = List.of(productDto1,productDto2,productDto3,productDto4,productDto5);
        ProductsDto productsDto = new ProductsDto(productList);

        //Expected response
        PromotionDto promotionDto1 = new PromotionDto(1, "Laptop Bag", 120, "30%");
        PromotionDto promotionDto2 = new PromotionDto(5, "USB", 60, "20%");
        PromotionDto promotionDto3 = new PromotionDto(3, "Keyboard", 40, "10%");
        List<PromotionDto> promotionList = List.of(promotionDto1,promotionDto2, promotionDto3);
        PromotionsDto promotionsDtoExpected = new PromotionsDto(promotionList);

        PromotionsDto promotionsDtoActual = stockPromotionService.processExcessStock(productsDto);

        //assertions
        Assertions.assertEquals(promotionsDtoExpected.promotionList().size(),promotionsDtoActual.promotionList().size());
        Assertions.assertEquals(promotionsDtoExpected.promotionList(),promotionsDtoActual.promotionList());
    }

    @Test
    public void processExcessStock_productIsNull(){
        String response = Assertions.assertThrows(InvalidInputException.class, () -> stockPromotionService.processExcessStock(null)).getMessage();
        Assertions.assertEquals("Invalid input", response);
    }

    @Test
    public void processExcessStock_productIsEmpty(){
        List<ProductDto> productList = List.of();
        ProductsDto productsDto = new ProductsDto(productList);
        String response = Assertions.assertThrows(InvalidInputException.class, () -> stockPromotionService.processExcessStock(productsDto)).getMessage();
        Assertions.assertEquals("Invalid input", response);
    }

}
