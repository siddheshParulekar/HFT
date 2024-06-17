package com.thrift.hft.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellRequest {

    Long userId;
    String sellerName;
    Long mobileNumber;
    String email;
    String sellerAddress;
    List<ProductRequest> productRequestList;
}
