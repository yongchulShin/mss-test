package com.musinsa.product.controller;

import com.musinsa.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 관련 API")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "카테고리별 최저가 조회", description = "각 카테고리에서 최저가 브랜드와 상품 정보, 총액을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "성공 예시",
                    value = "{\n" +
                            "  \"카테고리\": [\n" +
                            "    {\n" +
                            "      \"카테고리\": \"상의\",\n" +
                            "      \"최저가\": {\n" +
                            "        \"브랜드\": \"Nike\",\n" +
                            "        \"가격\": 10000\n" +
                            "      }\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"카테고리\": \"아우터\",\n" +
                            "      \"최저가\": {\n" +
                            "        \"브랜드\": \"Adidas\",\n" +
                            "        \"가격\": 8000\n" +
                            "      }\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"총액\": 18000\n" +
                            "}"
                )
            )
        )
    })
    @GetMapping("/summary/lowest")
    public ResponseEntity<Map<String, Object>> getLowestByCategory() {
        return ResponseEntity.ok(productService.getLowestByCategory());
    }

    @Operation(summary = "브랜드별 전체 카테고리 최저가 조회", description = "단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격 브랜드를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "성공 예시",
                    value = "{\n" +
                            "  \"최저가\": {\n" +
                            "    \"브랜드\": \"Nike\",\n" +
                            "    \"카테고리\": [\n" +
                            "      {\n" +
                            "        \"카테고리\": \"상의\",\n" +
                            "        \"가격\": 10000\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"카테고리\": \"아우터\",\n" +
                            "        \"가격\": 8000\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"총액\": 18000\n" +
                            "  }\n" +
                            "}"
                )
            )
        )
    })
    @GetMapping("/summary/brand-lowest")
    public ResponseEntity<Map<String, Object>> getLowestBrandForAllCategories() {
        return ResponseEntity.ok(productService.getLowestBrandForAllCategories());
    }

    @Operation(summary = "카테고리별 최저/최고가 조회", description = "특정 카테고리의 최저가/최고가 브랜드와 상품 가격을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "성공 예시",
                    value = "{\n"+
                            "   \"카테고리\": \"상의\",\n"+
                            "   \"최저가\": [\n"+
                            "   \"{\n"+
                            "       \"브랜드\": \"Nike\",\n"+
                            "       \"가격\": 10000\n"+
                            "     }\n"+
                            "   \"],\n"+
                            "   \"최고가\": [\n"+
                            "   \"{\n"+
                            "   \"브랜드\": \"Adidas\",\n"+
                            "   \"가격\": 15000\n"+
                            "     }\n"+
                            "   ]\n"+
                            "}"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "카테고리를 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(
                    name = "실패 예시",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"message\": \"카테고리를 찾을 수 없습니다: 존재하지않는카테고리\"\n" +
                            "}"
                )
            )
        )
    })
    @GetMapping("/summary/category/{categoryName}")
    public ResponseEntity<Map<String, Object>> getMinMaxByCategory(
            @Parameter(description = "카테고리명", example = "상의") @PathVariable String categoryName) {
        return ResponseEntity.ok(productService.getMinMaxByCategory(categoryName));
    }
} 