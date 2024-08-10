package com.thrift.hft.controller;

import com.razorpay.RazorpayException;
import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.request.PlaceOrderRequest;
import com.thrift.hft.service.IOrderService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/v1/order")
@Slf4j
public class OrderController {

    @Autowired
    IOrderService orderService;


    @PostMapping()
    public ResponseEntity<ResponseDTO> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest,
                                                         HttpServletRequest request) throws RazorpayException {
        log.info("OrderController - Inside placeOrder request");
        return ResponseEntityUtils.get(orderService.placeOrder(placeOrderRequest, CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))),"Order placed successfully");
    }

}
