package com.musinsa.service;

import com.musinsa.domain.Category;
import com.musinsa.domain.Product;
import com.musinsa.exception.ApiException;
import com.musinsa.repository.BrandRepository;
import com.musinsa.repository.CategoryRepository;
import com.musinsa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final BrandRepository brandRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;


    // 1. 카테고리별 최저가 브랜드/상품/총액 조회
    public Map<String, Object> getLowestByCategory() {
        List<Category> categories = categoryRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<Map<String, Object>> result = new ArrayList<>();
        int total = 0;
        for (Category cat : categories) {
            List<Product> products = productRepo.findByCategory(cat);
            Product min = products.stream().min(Comparator.comparingInt(Product::getPrice))
                    .orElseThrow(() -> new ApiException(cat.getName() + " 상품 없음"));
            LinkedHashMap<String, Object> item = new LinkedHashMap<>();
            item.put("카테고리", cat.getName());
            item.put("브랜드", min.getBrand().getName());
            item.put("가격", min.getPrice());
            result.add(item);
            total += min.getPrice();
        }
        return Map.of("카테고리", result, "총액", total);
    }
} 