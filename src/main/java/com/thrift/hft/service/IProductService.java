package com.thrift.hft.service;

import com.thrift.hft.dto.ProductDTO;
import com.thrift.hft.request.GetAllProductRequest;
import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.response.TokenResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface IProductService {

    Page<ProductDTO> getAllProduct(GetAllProductRequest getAllProductRequest) throws IOException;

    ProductDTO viewProduct(String pid) throws IOException;

    ProductDTO createSellRequest(ProductRequest request, TokenResponse tokenResponse) throws IOException;
}
