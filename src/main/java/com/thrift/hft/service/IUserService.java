package com.thrift.hft.service;

import com.thrift.hft.dto.UserDTO;
import com.thrift.hft.entity.User;
import com.thrift.hft.request.UpdateUserRequest;
import com.thrift.hft.request.UserRequest;
import com.thrift.hft.response.TokenResponse;

public interface IUserService {

    UserDTO addUser(UserRequest userRequest) ;

    UserDTO updateUser(Long userId, UpdateUserRequest updateUserRequest, TokenResponse tokenResponse);

}
