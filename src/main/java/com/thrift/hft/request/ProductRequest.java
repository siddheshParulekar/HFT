package com.thrift.hft.request;

import com.thrift.hft.enums.Size;
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
    MultipartFile[] images;
}
