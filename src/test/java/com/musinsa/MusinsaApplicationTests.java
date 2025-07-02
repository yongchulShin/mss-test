package com.musinsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.brand.dto.BrandDto;
import com.musinsa.product.dto.ProductDto;
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
class MusinsaApplicationTests {
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;

	@Test
	@DisplayName("브랜드 생성-조회-수정-삭제 통합 플로우")
	void brandCrudIntegrationFlow() throws Exception {
		// 1. 브랜드 생성
		BrandDto brandDto = new BrandDto();
		brandDto.setName("통합테스트브랜드");
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
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));

		// 2. 전체 브랜드/상품 목록(카테고리별 최저가) 조회
		mockMvc.perform(get("/product/summary/lowest"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.카테고리").isArray());

		// 3. 브랜드 수정
		brandDto.setName("통합테스트브랜드-수정");
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
		// 브랜드 id를 얻기 위해 카테고리별 최저가 API에서 id 추출(실제 환경에서는 별도 API 필요)
		// 여기서는 테스트 단순화를 위해 1번 id로 가정
		mockMvc.perform(put("/brand/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(brandDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));

		// 4. 브랜드 삭제
		mockMvc.perform(delete("/brand/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}
}
