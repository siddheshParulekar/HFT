package com.thrift.hft.request;

import com.thrift.hft.enums.*;
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

    String productName;
    BigDecimal prize;
    Condition condition;
    Category category;
    SubCategoryM subCategoryM;
    SubCategoryW subCategoryW;
    Brand brand;
    MultipartFile[] images;
}
