package com.thrift.hft.service;

import com.razorpay.RazorpayException;
import com.thrift.hft.entity.ThriftOrder;
import com.thrift.hft.request.PlaceOrderRequest;
import com.thrift.hft.response.TokenResponse;

public interface IOrderService {

    ThriftOrder placeOrder(PlaceOrderRequest placeOrderRequest, TokenResponse tokenResponse) throws RazorpayException;
}
