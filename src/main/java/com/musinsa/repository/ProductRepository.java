package com.musinsa.repository;

import com.musinsa.domain.Product;
import com.musinsa.domain.Brand;
import com.musinsa.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByBrand(Brand brand);
} 