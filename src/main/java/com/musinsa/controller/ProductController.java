package com.musinsa.controller;

import com.musinsa.dto.*;
import com.musinsa.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. 카테고리별 최저가 브랜드/상품/총액 조회
    @GetMapping("/summary/lowest")
    public ResponseEntity<?> getLowestByCategory() {
        return ResponseEntity.ok(productService.getLowestByCategory());
    }

    // 2. 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
    @GetMapping("/summary/brand-lowest")
    public ResponseEntity<?> getLowestBrandForAllCategories() {
        return ResponseEntity.ok(productService.getLowestBrandForAllCategories());
    }

}