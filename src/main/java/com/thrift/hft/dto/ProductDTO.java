package com.thrift.hft.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

    Long id;
    String description;
    BigDecimal prize;
    Map<String,String> condition = new HashMap<>();
    Map<String,String> category = new HashMap<>();
    Map<String,String> subCategory = new HashMap<>();
    Map<String,String> brand = new HashMap<>();
    Long sellerId;
    Map<String,String> prodStatus = new HashMap<>();
    Map<String,String> approvalStatus = new HashMap<>();
    Map<String,String> size = new HashMap<>();
    Map<String,String> color = new HashMap<>();
    List<ProdImageDTO> imageList;
}
