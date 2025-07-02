package com.musinsa.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공통 API 응답 DTO")
public class ApiResponseDto {
    @Schema(description = "성공 여부", example = "true")
    private boolean success;
    
    @Schema(description = "응답 메시지", example = "처리되었습니다")
    private String message;
    
    public static ApiResponseDto success() {
        return new ApiResponseDto(true, "처리되었습니다");
    }
    
    public static ApiResponseDto success(String message) {
        return new ApiResponseDto(true, message);
    }
    
    public static ApiResponseDto error(String message) {
        return new ApiResponseDto(false, message);
    }
} 