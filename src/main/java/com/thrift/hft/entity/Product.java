package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import com.thrift.hft.enums.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Product extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String productName;
    BigDecimal prize;
    Condition condition;
    Category category;
    SubCategoryM subCategoryM;
    SubCategoryW subCategoryW;
    Brand brand;
    Long sellerId;


    public Product(String productName, BigDecimal prize, Condition condition, Category category, SubCategoryM subCategoryM, SubCategoryW subCategoryW, Brand brand, Long sellerId) {
        this.productName = productName;
        this.prize = prize;
        this.condition = condition;
        this.category = category;
        this.subCategoryM = subCategoryM;
        this.subCategoryW = subCategoryW;
        this.brand = brand;
        this.sellerId = sellerId;
    }
}
