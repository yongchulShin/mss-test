package com.musinsa.service;

import com.musinsa.domain.Brand;
import com.musinsa.domain.Category;
import com.musinsa.domain.Product;
import com.musinsa.exception.ApiException;
import com.musinsa.repository.BrandRepository;
import com.musinsa.repository.CategoryRepository;
import com.musinsa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

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

    // 2. 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
    public Map<String, Object> getLowestBrandForAllCategories() {
        List<Brand> brands = brandRepo.findAll();
        List<Category> categories = categoryRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    
        int minTotal = Integer.MAX_VALUE;
        Brand minBrand = null;
        List<Map<String, Object>> minList = null;
    
        for (Brand brand : brands) {
            int sum = 0;
            List<Map<String, Object>> tempList = new ArrayList<>();
            boolean allExist = true;
            for (Category cat : categories) {
                Optional<Product> p = productRepo.findByBrand(brand).stream()
                        .filter(prod -> prod.getCategory().getId().equals(cat.getId()))
                        .findFirst();
                if (p.isEmpty()) {
                    allExist = false;
                    break;
                }
                // 순서 보장 위해 LinkedHashMap 사용
                LinkedHashMap<String, Object> item = new LinkedHashMap<>();
                item.put("카테고리", cat.getName());
                item.put("가격", String.format("%,d", p.get().getPrice()));
                tempList.add(item);
                sum += p.get().getPrice();
            }
            if (allExist && sum < minTotal) {
                minTotal = sum;
                minBrand = brand;
                minList = tempList;
            }
        }
        if (minBrand == null) throw new ApiException("모든 카테고리 상품을 가진 브랜드가 없습니다.");
    
        // 응답 포맷 맞추기
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("브랜드", minBrand.getName());
        result.put("카테고리", minList);
        result.put("총액", String.format("%,d", minTotal));
    
        return Map.of("최저가", result);
    }
} 