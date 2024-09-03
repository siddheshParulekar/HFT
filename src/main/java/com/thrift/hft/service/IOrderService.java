package com.thrift.hft.service;

import com.razorpay.RazorpayException;
import com.thrift.hft.request.CreateOrderRequest;
import com.thrift.hft.request.PlaceOrderRequest;
import com.thrift.hft.response.RazorPayResponse;
import com.thrift.hft.response.TokenResponse;

public interface IOrderService {

    RazorPayResponse crearteOrder(CreateOrderRequest placeOrderRequest, TokenResponse tokenResponse) throws RazorpayException;

    void placeOrder(PlaceOrderRequest placeOrderRequest,TokenResponse tokenResponse);
}
