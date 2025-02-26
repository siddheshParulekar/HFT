package com.thrift.hft.service.serviceImpl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.thrift.hft.entity.Cart;
import com.thrift.hft.entity.Product;
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
import com.thrift.hft.service.IEmailService;
import com.thrift.hft.service.IOrderService;
import com.thrift.hft.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    IEmailService emailService;



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
       // String body = MailUtils.generateEmailContent(user.getName(), thriftOrder.getId(), thriftOrder.getCreationDate().toString(), cart.getCartAmount(), cart.getProductList());
        processOrder(cart,thriftOrder);
       // MailUtils.send(user.getEmail(),"Order Placed",body,null);
        cart.setIsOrdered(Boolean.TRUE);
        thriftOrder.setTransactionId(placeOrderRequest.getTransactionId());
        thriftOrder.setOrderStatus("PAID");
        cartRepository.save(cart);
        thriftOrderRepository.save(thriftOrder);

    }

    private void processOrder(Cart cart,ThriftOrder thriftOrder) throws MessagingException {
        User user = userRepository.findById(cart.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> model = new HashMap<>();
        model.put("userName", user.getName());
        model.put("orderId", thriftOrder.getId());
        model.put("orderDate", LocalDateTime.now().toString());
        model.put("totalAmount", cart.getCartAmount().toString());

        List<Product> productList = cart.getProductList();

        List<Map<String, String>> items = new ArrayList<>();
        for (Product product : productList) {
            Map<String, String> item = new HashMap<>();
            item.put("product", product.getDescription());
            item.put("brand", product.getBrand().toString());
            item.put("price", product.getPrize().toString());
            items.add(item);
        }

        model.put("items", items);

        emailService.sendOrderConfirmationEmail(user.getEmail(), model);
    }


}
