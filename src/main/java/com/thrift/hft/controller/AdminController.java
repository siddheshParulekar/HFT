package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.request.UserRequest;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;

import static com.thrift.hft.constants.GeneralMsgConstant.MSG_USER_ADDED;
import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/v1/user")
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @Autowired
    IProductService productService;

    @PutMapping("/approve-sell-request/{pId}")
    public ResponseEntity<ResponseDTO> registerUser(@PathVariable("pId") String productId, HttpServletRequest request ) throws IOException {
        logger.info("UserController- inside registerUser method");
        return ResponseEntityUtils.get(productService.approveSellRequest(productId, CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))),"approved Successfully");
    }

}
