package com.thrift.hft.service.serviceImpl.utils.async;

import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.request.SellRequest;
import com.thrift.hft.response.TokenResponse;
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
public class SendSellRequestInQueue {

    List<ProductRequest> productRequestList;
    TokenResponse tokenResponse;
}
