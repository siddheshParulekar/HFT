package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.entity.User;
import com.thrift.hft.exceptions.AlreadyExistsException;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.UserRequest;
import com.thrift.hft.service.IUserService;
import com.thrift.hft.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;


    @Override
    public User addUser(UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
        if (optionalUser.isPresent() && (optionalUser.get().getEmail().equals(userRequest.getEmail())))
            throw new AlreadyExistsException("ERROR_USER_WITH_SAME_EMAIL_ALREADY_EXISTS");

        return userRepository.save( User.builder()
                .email(userRequest.getEmail())
//                .role(userRequest.getRole())
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .mobileNumber(Long.parseLong(userRequest.getMobileNumber()))
                        .username(userRequest.getUsername())
                .password(CommonUtils.encodePassword(userRequest.getPassword())).build());
    }
}
