package com.thrift.hft.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    String description;
    BigDecimal prize;
    String condition;
    String category;
    String subCategory;
    String brand;
    String size;
    String color;
    MultipartFile[] files;
    Long userId;

    public ProductRequest(String description, BigDecimal prize, String condition, String category, String subCategory, String brand, String size, String color, MultipartFile[] files) {
        this.description = description;
        this.prize = prize;
        this.condition = condition;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.files = files;
    }
}
