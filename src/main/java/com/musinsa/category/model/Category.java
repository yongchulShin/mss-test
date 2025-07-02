package com.musinsa.category.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카테고리 엔티티")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "카테고리 ID")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "카테고리명", example = "상의")
    private String name;
} 