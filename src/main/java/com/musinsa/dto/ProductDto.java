package com.musinsa.dto;

public class ProductDto {
    private Long id;
    private String brandName;
    private String categoryName;
    private int price;

    public ProductDto() {}
    public ProductDto(Long id, String brandName, String categoryName, int price) {
        this.id = id;
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.price = price;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
} 