package com.thrift.hft.service;

import com.thrift.hft.request.SellRequest;
import com.thrift.hft.response.TokenResponse;

public interface IProductService {

    String createSellRequest(SellRequest request, TokenResponse tokenResponse);
}
