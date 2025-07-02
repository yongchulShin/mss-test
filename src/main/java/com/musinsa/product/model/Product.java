package com.musinsa.product.model;

import com.musinsa.brand.model.Brand;
import com.musinsa.category.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 엔티티")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @Schema(description = "브랜드 정보")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "카테고리 정보")
    private Category category;

    @Column(nullable = false)
    @Schema(description = "상품 가격", example = "10000")
    private int price;
} 