package com.thrift.hft.request;

import com.thrift.hft.enums.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.servlet.http.Part;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {

    String productName;
    BigDecimal prize;
    Condition condition;
    Category category;
    SubCategory subCategory;
    Brand brand;
    Size size;
    List<Part> images;
}
