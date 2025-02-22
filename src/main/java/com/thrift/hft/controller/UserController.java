package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.request.AddAddressRequest;
import com.thrift.hft.request.EditAddressRequest;
import com.thrift.hft.request.UpdateUserRequest;
import com.thrift.hft.request.UserRequest;
import com.thrift.hft.service.IUserService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.thrift.hft.constants.GeneralMsgConstant.MSG_USER_ADDED;
import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);


    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserRequest userRequest) {
        logger.info("UserController- inside registerUser method");
        return ResponseEntityUtils.get(userService.addUser(userRequest), MSG_USER_ADDED);
    }

    @PutMapping("update-user/{userId}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable("userId") Long userId, @RequestBody @Valid UpdateUserRequest updateUserRequest,
                                                  HttpServletRequest request) {
        logger.info("UserController- inside registerUser method");
        return ResponseEntityUtils.get(userService.updateUser(userId,updateUserRequest, CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))), MSG_USER_ADDED);
    }

    @PostMapping("/add-address")
    public ResponseEntity<ResponseDTO> addAddress(@RequestBody @Valid AddAddressRequest addAddressRequest,
                                                     HttpServletRequest request) {
        logger.info("UserController- inside addAddress method");
        return ResponseEntityUtils.get(userService.addAddress(addAddressRequest, CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))), "Address updated successfully");
    }

        @GetMapping("/fetch-my-profile")
    public ResponseEntity<ResponseDTO> fetchMyProfile(HttpServletRequest request){
        logger.info("UserController - inside fetchMyProfile method");
        return ResponseEntityUtils.get(userService.fetchUserProfile(CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))), "User fetch successfully");
    }

    @GetMapping("/fetch-my-orders")
    public ResponseEntity<ResponseDTO> fetchMyOrders(final Pageable pageable,
                                                        HttpServletRequest request){
        logger.info("USerController - Inside fetchMyOrders method");
        return ResponseEntityUtils.get(userService.fetchMyOrder(CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION)),pageable),"Orders Fetched successfully");
    }

    @PutMapping("edit-address/{addressId}")
    public ResponseEntity<ResponseDTO> updateAddress(@PathVariable("addressId") String addressId, @RequestBody @Valid EditAddressRequest editAddressRequest,
                                                  HttpServletRequest request) {
        logger.info("UserController- inside updateAddress method");
        return ResponseEntityUtils.get(userService.editAddress(addressId,editAddressRequest, CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))), MSG_USER_ADDED);
    }

}
