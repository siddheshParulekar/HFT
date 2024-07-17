package com.thrift.hft.service;

import com.thrift.hft.dto.CartDTO;
import com.thrift.hft.response.TokenResponse;

public interface ICartService {

    CartDTO addToCart(Long productId, TokenResponse tokenResponse);

    void deleteFromCart(Long productId,TokenResponse tokenResponse);
}
