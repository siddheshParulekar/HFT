package com.thrift.hft.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellRequest {

    @NotNull
    Long userId;
    @NotEmpty
    String sellerName;
    @NotNull
    Long mobileNumber;
    @NotEmpty
    String email;
    @NotEmpty
    String sellerAddress;
    @NotEmpty
    List<ProductRequest> productRequestList;
}
