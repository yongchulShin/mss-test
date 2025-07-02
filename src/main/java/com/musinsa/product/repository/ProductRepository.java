package com.musinsa.product.repository;

import com.musinsa.product.model.Product;
import com.musinsa.brand.model.Brand;
import com.musinsa.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByBrand(Brand brand);
} 