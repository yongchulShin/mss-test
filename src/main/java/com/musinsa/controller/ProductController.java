package com.musinsa.controller;

import com.musinsa.dto.*;
import com.musinsa.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. 카테고리별 최저가 브랜드/상품/총액 조회
    @GetMapping("/summary/lowest")
    public ResponseEntity<Map<String, Object>> getLowestByCategory() {
        return ResponseEntity.ok(productService.getLowestByCategory());
    }

}