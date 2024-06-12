package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.request.UserRequest;
import com.thrift.hft.service.IUserService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.thrift.hft.constants.GeneralMsgConstant.MSG_USER_ADDED;
import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);


    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserRequest userRequest) {
        logger.info("UserController- inside registerUser method");
        return ResponseEntityUtils.get(userService.addUser(userRequest), MSG_USER_ADDED);
    }
}
