package com.musinsa.brand.controller;

import com.musinsa.brand.dto.BrandDto;
import com.musinsa.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@Tag(name = "Brand", description = "브랜드 관련 API")
public class BrandController {
    private final ProductService productService;
    // 4. 브랜드 및 상품 추가
    @Operation(summary = "브랜드 및 상품 추가", description = "새로운 브랜드와 모든 카테고리의 상품을 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "추가 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "성공 예시",
                    value = "{\n" +
                            "  \"success\": true\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "추가 실패",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "실패 예시",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"message\": \"브랜드명이 중복됩니다: Nike\"\n" +
                            "}"
                )
            )
        )
    })
    @PostMapping("")
    public ResponseEntity<?> addBrandWithProducts(@RequestBody BrandDto brandDto) {
        productService.addBrandWithProducts(brandDto);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // 5. 브랜드 및 상품 수정
    @Operation(summary = "브랜드 및 상품 수정", description = "기존 브랜드 정보와 상품 정보를 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "성공 예시",
                    value = "{\n" +
                            "  \"success\": true\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "수정 실패",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "실패 예시",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"message\": \"브랜드를 찾을 수 없습니다: 999\"\n" +
                            "}"
                )
            )
        )
    })
    @PutMapping("/{brandId}")
    public ResponseEntity<?> updateBrandWithProducts(
            @Parameter(description = "브랜드 ID", example = "1") @PathVariable Long brandId, 
            @RequestBody BrandDto brandDto) {
        productService.updateBrandWithProducts(brandId, brandDto);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // 6. 브랜드 및 상품 삭제
    @Operation(summary = "브랜드 및 상품 삭제", description = "브랜드와 관련된 모든 상품을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "성공 예시",
                    value = "{\n" +
                            "  \"success\": true\n" +
                            "}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "삭제 실패",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "실패 예시",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"message\": \"브랜드를 찾을 수 없습니다: 999\"\n" +
                            "}"
                )
            )
        )
    })
    @DeleteMapping("/{brandId}")
    public ResponseEntity<?> deleteBrand(
            @Parameter(description = "브랜드 ID", example = "1") @PathVariable Long brandId) {
        productService.deleteBrand(brandId);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
