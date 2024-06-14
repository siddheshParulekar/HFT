package com.thrift.hft.service;

import com.thrift.hft.request.LoginRequest;
import com.thrift.hft.response.LoginResponse;

public interface ILoginService {

    LoginResponse login(LoginRequest loginRequest) throws Exception;

}
