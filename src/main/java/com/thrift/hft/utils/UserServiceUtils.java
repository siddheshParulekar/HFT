package com.thrift.hft.utils;

import com.thrift.hft.entity.User;
import com.thrift.hft.entity.UserRoles;
import com.thrift.hft.exceptions.NotFoundException;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.repository.UserRoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.thrift.hft.constants.ErrorMsgConstants.ERROR_USER_NOT_FOUND;
import static com.thrift.hft.constants.ErrorMsgConstants.ERROR_USER_ROLE_NOT_FOUND;

public class UserServiceUtils {

    private static final Logger logger = LogManager.getLogger(UserServiceUtils.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRolesRepository;


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
