package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.enums.Mastertype;
import com.thrift.hft.service.ICartService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/v1/home-page")
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    @Autowired
    ICartService cartService;


    @PostMapping("/add-to-cart/{pid}")
    public ResponseEntity<ResponseDTO> addToCart(@PathVariable("pid") Long productId,
                                                 HttpServletRequest request) {
        logger.info("CartController - Inside addToCart method");
        return ResponseEntityUtils.get(cartService.addToCart(productId, CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))), "Added to cart");
    }

    @PostMapping("/delete-from-cart/{pid}")
    public ResponseEntity<ResponseDTO> deleteFromCart(@PathVariable("pid") Long productId,
                                                 HttpServletRequest request) {
        logger.info("CartController - Inside deleteFromCart method");
        cartService.deleteFromCart(productId,CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION)));
        return ResponseEntityUtils.get(null, "deleted from cart");
    }

}
