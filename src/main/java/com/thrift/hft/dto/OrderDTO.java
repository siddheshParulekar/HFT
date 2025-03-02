package com.thrift.hft.dto;

import com.thrift.hft.enums.DeliveryStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {

    String id;
    Date creationDate;
    List<ProductDTO> productList;
    String addressId;
    BigDecimal amount;
    String orderStatus;
    Long userId;
    DeliveryStatus deliveryStatus;

    public OrderDTO(String id, Date creationDate, List<ProductDTO> productList) {
        this.id = id;
        this.creationDate= creationDate;
        this.productList = productList;
    }
}
