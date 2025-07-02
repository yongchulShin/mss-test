package com.musinsa.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 정보 DTO")
public class ProductDto {
    @Schema(description = "카테고리명", example = "상의")
    private String categoryName;
    
    @Schema(description = "가격", example = "10000")
    private int price;
} 