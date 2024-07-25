package com.thrift.hft.request;

import com.thrift.hft.enums.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllProductRequest {

    Pageable pageable;
    Category category;
    SubCategory subCategory;
    Brand brand;
    ProdStatus prodStatus;
    ApprovalStatus approvalStatus;
    Size size;
    Colour colour;
    Condition condition;
}
