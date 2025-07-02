package com.musinsa.product.service;

import com.musinsa.brand.model.Brand;
import com.musinsa.category.model.Category;
import com.musinsa.product.model.Product;
import com.musinsa.common.exception.ApiException;
import com.musinsa.brand.dto.BrandDto;
import com.musinsa.product.dto.ProductDto;
import com.musinsa.brand.repository.BrandRepository;
import com.musinsa.category.repository.CategoryRepository;
import com.musinsa.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    // 3. 카테고리 이름으로 최저/최고가 브랜드/상품 가격 조회
    public Map<String, Object> getMinMaxByCategory(String categoryName) {
        // 카테고리 조회
        Category cat = categoryRepo.findAll().stream()
                .filter(c -> c.getName().equals(categoryName))
                .findFirst()
                .orElseThrow(() -> new ApiException("카테고리 없음"));

        List<Product> products = productRepo.findByCategory(cat);
        if (products.isEmpty()) throw new ApiException("해당 카테고리 상품 없음");

        // 최저/최고가 계산
        int min = products.stream().mapToInt(Product::getPrice).min().orElseThrow();
        int max = products.stream().mapToInt(Product::getPrice).max().orElseThrow();

        // 순서 보장 위해 LinkedHashMap 사용
        List<Map<String, Object>> minList = products.stream()
                .filter(p -> p.getPrice() == min)
                .map(p -> {
                    LinkedHashMap<String, Object> m = new LinkedHashMap<>();
                    m.put("브랜드", p.getBrand().getName());
                    m.put("가격", String.format("%,d", p.getPrice()));
                    return m;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> maxList = products.stream()
                .filter(p -> p.getPrice() == max)
                .map(p -> {
                    LinkedHashMap<String, Object> m = new LinkedHashMap<>();
                    m.put("브랜드", p.getBrand().getName());
                    m.put("가격", String.format("%,d", p.getPrice()));
                    return m;
                })
                .collect(Collectors.toList());

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("카테고리", cat.getName());
        result.put("최저가", minList);
        result.put("최고가", maxList);

        return result;
    }

    // 모든 카테고리에 1개의 상품이 있는지 체크
    private void checkAllCategoriesHasOneProduct(List<ProductDto> products) {
        List<Category> allCategories = categoryRepo.findAll();
        List<String> productCategories = products.stream()
                .map(ProductDto::getCategoryName)
                .collect(Collectors.toList());
        Map<String, Long> categoryCount = productCategories.stream()
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        for (Category category : allCategories) {
            long count = categoryCount.getOrDefault(category.getName(), 0L);
            if (count == 0) {
                throw new ApiException("카테고리 [" + category.getName() + "]에 상품이 없습니다.");
            }
            if (count > 1) {
                throw new ApiException("카테고리 [" + category.getName() + "]에 상품이 2개 이상입니다.");
            }
        }
    }

    // 상품 리스트를 받아 브랜드와 함께 저장
    private void saveProductsForBrand(Brand brand, List<ProductDto> products) {
        List<Category> allCategories = categoryRepo.findAll();
        for (ProductDto pd : products) {
            Category cat = allCategories.stream()
                    .filter(c -> c.getName().equals(pd.getCategoryName()))
                    .findFirst().orElseThrow(() -> new ApiException("카테고리 없음: " + pd.getCategoryName()));
            Product p = new Product();
            p.setBrand(brand);
            p.setCategory(cat);
            p.setPrice(pd.getPrice());
            productRepo.save(p);
        }
    }

    @Transactional
    public void addBrandWithProducts(BrandDto brandDto) {
        // 브랜드 이름 중복 체크
        if (brandRepo.findByName(brandDto.getName()).isPresent()) {
            throw new ApiException("이미 존재하는 브랜드입니다.");
        }
        // 모든 카테고리에 1개의 상품이 있는지 체크
        checkAllCategoriesHasOneProduct(brandDto.getProducts());
        // 브랜드 생성
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brand = brandRepo.save(brand);
        // 상품 저장
        saveProductsForBrand(brand, brandDto.getProducts());
    }

    @Transactional
    public void updateBrandWithProducts(Long brandId, BrandDto brandDto) {
        // 브랜드 조회
        Brand brand = brandRepo.findById(brandId).orElseThrow(() -> new ApiException("브랜드 없음"));
        // 브랜드 이름 수정
        brand.setName(brandDto.getName());
        // 브랜드 저장
        brandRepo.save(brand);
        
        // 모든 카테고리에 1개의 상품이 있는지 체크
        checkAllCategoriesHasOneProduct(brandDto.getProducts());
        // 기존 상품 삭제
        List<Product> oldProducts = productRepo.findByBrand(brand);
        productRepo.deleteAll(oldProducts);
        // 새로운 상품 저장
        saveProductsForBrand(brand, brandDto.getProducts());
    }

    @Transactional
    public void deleteBrand(Long brandId) {
        // 브랜드 조회
        Brand brand = brandRepo.findById(brandId).orElseThrow(() -> new ApiException("브랜드 없음"));
        // 브랜드에 속한 상품 조회
        List<Product> products = productRepo.findByBrand(brand);
        // 상품 삭제
        productRepo.deleteAll(products);
        // 브랜드 삭제
        brandRepo.delete(brand);
    }
} 