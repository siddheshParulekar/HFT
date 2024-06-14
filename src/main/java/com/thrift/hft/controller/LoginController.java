package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.request.LoginRequest;
import com.thrift.hft.response.LoginResponse;
import com.thrift.hft.security.JwtUtils;
import com.thrift.hft.service.ILoginService;
import com.thrift.hft.utils.ResponseEntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/auth")
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private ILoginService loginService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
        logger.info("LoginController - Inside login method");
        return loginService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request) {
        logger.info("LoginController - Inside logout method");
        loginService.logout(request);
        return ResponseEntityUtils.get(null, "Logout Successfully");
    }
}