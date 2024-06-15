package com.thrift.hft.service;

import com.thrift.hft.request.LoginRequest;
import com.thrift.hft.response.LoginResponse;

import javax.servlet.http.HttpServletRequest;

public interface ILoginService {

    LoginResponse login(LoginRequest loginRequest) throws Exception;
    void logout(HttpServletRequest request);


}
