package com.thrift.hft.service;

import com.thrift.hft.dto.CartDTO;
import com.thrift.hft.dto.OrderDTO;
import com.thrift.hft.dto.UserDTO;
import com.thrift.hft.request.AddAddressRequest;
import com.thrift.hft.request.UpdateUserRequest;
import com.thrift.hft.request.UserRequest;
import com.thrift.hft.response.TokenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {

    UserDTO addUser(UserRequest userRequest) ;

    UserDTO updateUser(Long userId, UpdateUserRequest updateUserRequest, TokenResponse tokenResponse);

    UserDTO addAddress(AddAddressRequest updateAddressRequest, TokenResponse tokenResponse);

    UserDTO fetchUserProfile(TokenResponse tokenResponse);

    Page<OrderDTO> fetchMyOrder(TokenResponse tokenResponse, Pageable pageable);

}
