package com.musinsa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.brand.dto.BrandDto;
import com.musinsa.brand.repository.BrandRepository;
import com.musinsa.category.repository.CategoryRepository;
import com.musinsa.product.dto.ProductDto;
import com.musinsa.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired BrandRepository brandRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired ProductRepository productRepository;

    @Test
    @DisplayName("카테고리별 최저가 브랜드/상품/총액 조회")
    void getLowestByCategory() throws Exception {
        mockMvc.perform(get("/product/summary/lowest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.카테고리").isArray())
                .andExpect(jsonPath("$.총액").exists());
    }

    @Test
    @DisplayName("단일 브랜드로 전체 카테고리 상품 구매시 최저가 브랜드/총액/상품가격 조회")
    void getLowestBrandForAllCategories() throws Exception {
        mockMvc.perform(get("/product/summary/brand-lowest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.최저가.브랜드").exists())
                .andExpect(jsonPath("$.최저가.카테고리").isArray())
                .andExpect(jsonPath("$.최저가.총액").exists());
    }

    @Test
    @DisplayName("카테고리 이름으로 최저/최고가 브랜드/상품 가격 조회")
    void getMinMaxByCategory() throws Exception {
        mockMvc.perform(get("/product/summary/category/상의"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.카테고리").value("상의"))
                .andExpect(jsonPath("$.최저가").isArray())
                .andExpect(jsonPath("$.최고가").isArray());
    }

    @Test
    @DisplayName("브랜드 및 상품 추가 성공")
    void addBrandWithProducts() throws Exception {
        BrandDto brandDto = new BrandDto();
        brandDto.setName("J");
        brandDto.setProducts(List.of(
                new ProductDto("상의", 12000),
                new ProductDto("아우터", 6000),
                new ProductDto("바지", 4000),
                new ProductDto("스니커즈", 9000),
                new ProductDto("가방", 2000),
                new ProductDto("모자", 1700),
                new ProductDto("양말", 1800),
                new ProductDto("액세서리", 2300)
        ));

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    @DisplayName("브랜드 및 상품 추가 실패 - 카테고리 누락")
    void addBrandWithProductsFail() throws Exception {
        BrandDto brandDto = new BrandDto();
        brandDto.setName("K");
        brandDto.setProducts(List.of(
                new ProductDto("상의", 12000) // 나머지 카테고리 없음
        ));

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("브랜드 및 상품 추가 실패 - 상품 가격 0 이하")
    void addBrandWithProductsPriceFail() throws Exception {
        BrandDto brandDto = new BrandDto();
        brandDto.setName("PriceFail");
        brandDto.setProducts(List.of(
                new ProductDto("상의", -1000),
                new ProductDto("아우터", 6000),
                new ProductDto("바지", 4000),
                new ProductDto("스니커즈", 9000),
                new ProductDto("가방", 2000),
                new ProductDto("모자", 1700),
                new ProductDto("양말", 1800),
                new ProductDto("액세서리", 2300)
        ));

        mockMvc.perform(post("/brand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("브랜드 및 상품 수정 성공")
    void updateBrandWithProducts() throws Exception {
        // 먼저 브랜드를 생성
        BrandDto brandDto = new BrandDto();
        brandDto.setName("L");
        brandDto.setProducts(List.of(
                new ProductDto("상의", 10000),
                new ProductDto("아우터", 8000),
                new ProductDto("바지", 7000),
                new ProductDto("스니커즈", 6000),
                new ProductDto("가방", 5000),
                new ProductDto("모자", 4000),
                new ProductDto("양말", 3000),
                new ProductDto("액세서리", 2000)
        ));
        String content = objectMapper.writeValueAsString(brandDto);
        String response = mockMvc.perform(post("/brand")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long brandId = brandRepository.findByName("L").get().getId();

        // 수정
        brandDto.setName("L-수정");
        brandDto.setProducts(List.of(
                new ProductDto("상의", 11000),
                new ProductDto("아우터", 9000),
                new ProductDto("바지", 8000),
                new ProductDto("스니커즈", 7000),
                new ProductDto("가방", 6000),
                new ProductDto("모자", 5000),
                new ProductDto("양말", 4000),
                new ProductDto("액세서리", 3000)
        ));
        mockMvc.perform(put("/brand/" + brandId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("브랜드 및 상품 삭제 성공")
    void deleteBrand() throws Exception {
        // 먼저 브랜드를 생성
        BrandDto brandDto = new BrandDto();
        brandDto.setName("M");
        brandDto.setProducts(List.of(
                new ProductDto("상의", 10000),
                new ProductDto("아우터", 8000),
                new ProductDto("바지", 7000),
                new ProductDto("스니커즈", 6000),
                new ProductDto("가방", 5000),
                new ProductDto("모자", 4000),
                new ProductDto("양말", 3000),
                new ProductDto("액세서리", 2000)
        ));
        mockMvc.perform(post("/brand")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDto)))
                .andExpect(status().isOk());
        Long brandId = brandRepository.findByName("M").get().getId();

        // 삭제
        mockMvc.perform(delete("/brand/" + brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}