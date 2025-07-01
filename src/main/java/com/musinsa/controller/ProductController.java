package com.musinsa.controller;

import com.musinsa.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    // 3. 카테고리 이름으로 최저/최고가 브랜드/상품 가격 조회
    @GetMapping("/summary/category/{categoryName}")
    public ResponseEntity<?> getMinMaxByCategory(@PathVariable String categoryName) {
        System.out.println(categoryName);
        return ResponseEntity.ok(productService.getMinMaxByCategory(categoryName));
    }

}