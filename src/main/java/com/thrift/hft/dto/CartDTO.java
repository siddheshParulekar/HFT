package com.thrift.hft.dto;

import com.thrift.hft.entity.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDTO {

    String id;
    Long userId;
    BigDecimal cartAmount;
    Boolean isOrdered ;
    List<ProductDTO> productList;

}
