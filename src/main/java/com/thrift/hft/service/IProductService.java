package com.thrift.hft.service;

import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.response.TokenResponse;

import java.util.List;

public interface IProductService {

    Long createSellRequest(List<ProductRequest> request, TokenResponse tokenResponse);
}
