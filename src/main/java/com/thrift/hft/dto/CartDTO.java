package com.thrift.hft.dto;

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
    BigDecimal deliveryCharges;
    BigDecimal total;
    Boolean isOrdered ;
    List<ProductDTO> productList;

}
