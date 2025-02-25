package com.thrift.hft.service.serviceImpl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.thrift.hft.entity.Cart;
import com.thrift.hft.entity.ThriftOrder;
import com.thrift.hft.entity.User;
import com.thrift.hft.exceptions.NotFoundException;
import com.thrift.hft.repository.CartRepository;
import com.thrift.hft.repository.ThriftOrderRepository;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.CreateOrderRequest;
import com.thrift.hft.request.PlaceOrderRequest;
import com.thrift.hft.response.RazorPayResponse;
import com.thrift.hft.response.TokenResponse;
import com.thrift.hft.service.IOrderService;
import com.thrift.hft.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    private CartRepository cartRepository;

    @Autowired
    private ThriftOrderRepository thriftOrderRepository;



    public RazorPayResponse crearteOrder(CreateOrderRequest placeOrderRequest, TokenResponse tokenResponse) throws RazorpayException {
        log.info("OrderServiceImpl - Inside crearteOrder method");
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
                .cartId(placeOrderRequest.getCartId())
                .userId(tokenResponse.getUserId()).build();
        thriftOrderRepository.save(thriftOrder);
       return new RazorPayResponse(razorpayOrder.get("id"),placeOrderRequest.getAmount(),razorpayOrder.get("currency"),razorpayKey);
    }

    @Override
    public void placeOrder(PlaceOrderRequest placeOrderRequest, TokenResponse tokenResponse) throws MessagingException {
        log.info("OrderServiceImpl - Inside placeOrder method");
        Cart cart = cartRepository.findById(placeOrderRequest.getCartId()).orElseThrow(()->new NotFoundException("Cart not found"));
        ThriftOrder thriftOrder = thriftOrderRepository.findByRazorpayOrderId(placeOrderRequest.getOrderId()).orElseThrow(()-> new NotFoundException("Order not found"));

        User user = userRepository.findById(cart.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        MailUtils.send(user.getEmail(),"Order Placed","Hello",null);
        cart.setIsOrdered(Boolean.TRUE);
        thriftOrder.setTransactionId(placeOrderRequest.getTransactionId());
        thriftOrder.setOrderStatus("PAID");
        cartRepository.save(cart);
        thriftOrderRepository.save(thriftOrder);

    }


}
