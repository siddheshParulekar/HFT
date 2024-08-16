package com.thrift.hft.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thrift.hft.entity.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GellAllProductResponse {

    List<Product> productList;
}
