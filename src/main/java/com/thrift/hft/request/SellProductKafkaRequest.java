package com.thrift.hft.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellProductKafkaRequest {


    String description;
    BigDecimal prize;
    String condition;
    String category;
    String subCategory;
    String brand;
    String size;
    String color;
    List<String> filePaths;
    Long userId;

}
