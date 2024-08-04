package com.thrift.hft.entity;

import com.thrift.hft.audit.Auditable;
import com.thrift.hft.dto.ProductDTO;
import com.thrift.hft.enums.*;
import com.thrift.hft.utils.CommonUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Product extends Auditable<String> {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(unique = true)
    String id;
    String description;
    BigDecimal prize;
    @Enumerated(EnumType.STRING)
    Condition condition ;
    @Enumerated(EnumType.STRING)
    Category category = Category.UNISEX;
    @Enumerated(EnumType.STRING)
    SubCategory subCategory = SubCategory.OTHERS;
    @Enumerated(EnumType.STRING)
    Brand brand;
    Long sellerId;
    @Enumerated(EnumType.STRING)
    ProdStatus prodStatus = ProdStatus.IN_STOCK;
    @Enumerated(EnumType.STRING)
    ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    @Enumerated(EnumType.STRING)
    Size size  = Size.FREE_SIZE;
    @Enumerated(EnumType.STRING)
    Colour colour = Colour.OTHER;




    public Product(String description, BigDecimal prize, Condition condition, Category category, SubCategory subCategory, Brand brand, Long sellerId, Size size,Colour colour) {
        this.description = description;
        this.prize = prize;
        this.condition = condition;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.sellerId = sellerId;
        this.size= size;
        this.colour = colour;
    }

    public ProductDTO getProductDTO() throws IOException {
        return  new ProductDTO(id, description,prize,CommonUtils.getEnumMap(condition.name(),condition.value()),CommonUtils.getEnumMap(category.name(),category.value()) ,CommonUtils.getEnumMap(subCategory.name(),subCategory.value()),CommonUtils.getEnumMap(brand.name(),brand.value()),sellerId,CommonUtils.getEnumMap(prodStatus.name(),prodStatus.value()),CommonUtils.getEnumMap(approvalStatus.name(),approvalStatus.value()) ,CommonUtils.getEnumMap(size.name(),size.value()), CommonUtils.getEnumMap(colour.name(),colour.value()),CommonUtils.getProductImages(id));
    }
}
