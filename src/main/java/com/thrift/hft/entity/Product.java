package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import com.thrift.hft.enums.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
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
    @Enumerated(EnumType.STRING)
    Condition condition;
    @Enumerated(EnumType.STRING)
    Category category;
    @Enumerated(EnumType.STRING)
    SubCategory subCategory;
    @Enumerated(EnumType.STRING)
    Brand brand;
    Long sellerId;
    Long batchId;
    @Enumerated(EnumType.STRING)
    ProdStatus prodStatus = ProdStatus.IN_STOCK;
    @Enumerated(EnumType.STRING)
    ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    @Enumerated(EnumType.STRING)
    Size size;



    public Product(String productName, BigDecimal prize, Condition condition, Category category, SubCategory subCategory, Brand brand, Long sellerId, Long batchId, Size size) {
        this.productName = productName;
        this.prize = prize;
        this.condition = condition;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.sellerId = sellerId;
        this.batchId = batchId;
        this.size= size;
    }
}
