package com.musinsa.brand.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "브랜드 엔티티")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "브랜드 ID")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "브랜드명", example = "Nike")
    private String name;
} 