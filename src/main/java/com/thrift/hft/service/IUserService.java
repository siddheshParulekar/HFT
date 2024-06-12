package com.thrift.hft.service;

import com.thrift.hft.entity.User;
import com.thrift.hft.request.UserRequest;

public interface IUserService {

    User addUser(UserRequest userRequest) ;

}
