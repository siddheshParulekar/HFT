package com.thrift.hft.service.serviceImpl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.thrift.hft.entity.ThriftOrder;
import com.thrift.hft.entity.User;
import com.thrift.hft.repository.ThriftOrderRepository;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.PlaceOrderRequest;
import com.thrift.hft.response.TokenResponse;
import com.thrift.hft.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {


    @Autowired
    private UserRepository userRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKey;

    @Value("${razorpay.secret.key}")
    private String razorpaySecret;

    private RazorpayClient client;

    @Autowired
    private ThriftOrderRepository thriftOrderRepository;



    public ThriftOrder placeOrder(PlaceOrderRequest placeOrderRequest, TokenResponse tokenResponse) throws RazorpayException {
        log.info("OrderServiceImpl - Inside placeOrder method");
        User user = userRepository.findById(tokenResponse.getUserId()).get();

        JSONObject orderReq = new JSONObject();
        orderReq.put("amount",placeOrderRequest.getAmount().multiply(new BigDecimal(100)));
        orderReq.put("currency","INR");
        orderReq.put("receipt",user.getEmail());
        this.client = new RazorpayClient(razorpayKey,razorpaySecret);
        Order razorpayOrder = client.orders.create(orderReq);
        ThriftOrder thriftOrder = ThriftOrder.builder().addressId(placeOrderRequest.getAddressId())
                .amount(placeOrderRequest.getAmount())
                .orderStatus(razorpayOrder.get("status"))
                .razorpayOrderId(razorpayOrder.get("id"))
                .userId(tokenResponse.getUserId()).build();
       return thriftOrderRepository.save(thriftOrder);
    }
}
