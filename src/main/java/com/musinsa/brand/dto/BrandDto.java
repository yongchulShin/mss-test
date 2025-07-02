package com.musinsa.brand.dto;

import com.musinsa.product.dto.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "브랜드 정보 DTO")
public class BrandDto {
    //private Long id;
    @Schema(description = "브랜드명", example = "Nike")
    private String name;
    
    @Schema(description = "상품 목록")
    private List<ProductDto> products;
} 