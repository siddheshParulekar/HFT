package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.dto.UserDTO;
import com.thrift.hft.entity.User;
import com.thrift.hft.exceptions.AlreadyExistsException;
import com.thrift.hft.exceptions.NotFoundException;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.UpdateUserRequest;
import com.thrift.hft.request.UserRequest;
import com.thrift.hft.response.TokenResponse;
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
    public UserDTO addUser(UserRequest userRequest) {
        logger.info("UserServiceImpl - Inside addUser method");
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
        if (optionalUser.isPresent() && (optionalUser.get().getEmail().equals(userRequest.getEmail())))
            throw new AlreadyExistsException("Error user with same email already exists");

        return userRepository.save( User.builder()
                .email(userRequest.getEmail())
               .role(userRequest.getRole())
                .name(userRequest.getName())
                .mobileNumber(Long.parseLong(userRequest.getMobileNumber()))
                .password(CommonUtils.encodePassword(userRequest.getPassword())).build()).getUserDTO();
    }

    @Override
    public UserDTO updateUser(Long userId, UpdateUserRequest updateUserRequest, TokenResponse tokenResponse) {
        logger.info("UserServiceImpl - Inside updateUser method");

        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException("User Not Found with this userId"));

        if (updateUserRequest != null){
            if (updateUserRequest.getAddress() != null)
                user.setAddress(updateUserRequest.getAddress());

            if (updateUserRequest.getEmail() != null)
                user.setEmail(updateUserRequest.getEmail());

            if (updateUserRequest.getMobileNumber() != null)
                user.setMobileNumber(Long.parseLong(updateUserRequest.getMobileNumber()));

            userRepository.save(user);
        }

        return user.getUserDTO();
    }
}
