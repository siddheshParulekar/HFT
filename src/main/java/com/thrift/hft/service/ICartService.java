package com.thrift.hft.service;

import com.thrift.hft.dto.CartDTO;
import com.thrift.hft.response.TokenResponse;

public interface ICartService {

    CartDTO addToCart(String productId, TokenResponse tokenResponse);

    void deleteFromCart(String productId,TokenResponse tokenResponse);

    CartDTO viewMyCart(TokenResponse tokenResponse);
}
