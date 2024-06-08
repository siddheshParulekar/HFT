package com.thrift.hft.utils;

import com.thrift.hft.entity.User;

import java.util.Optional;

public class UserServiceUtils {


    protected User isUserValid(Long userId) {
        logger.info("UserServiceUtils - Inside isUserValid method");
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new NotFoundException(ERROR_USER_NOT_FOUND);

        Optional<UserRoles> optionalUserRoles = userRolesRepository.findByUserId(userId);
        if (optionalUserRoles.isEmpty())
            throw new NotFoundException(ERROR_USER_ROLE_NOT_FOUND);
        return optionalUser.get();
    }
}
