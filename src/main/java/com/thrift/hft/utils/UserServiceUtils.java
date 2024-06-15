package com.thrift.hft.utils;

import com.thrift.hft.entity.AccessToken;
import com.thrift.hft.entity.User;
import com.thrift.hft.exceptions.NotFoundException;
import com.thrift.hft.repository.AccessTokenRepository;
import com.thrift.hft.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.thrift.hft.constants.ErrorMsgConstants.ERROR_USER_NOT_FOUND;

public class UserServiceUtils {

    private static final Logger logger = LogManager.getLogger(UserServiceUtils.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccessTokenRepository accessTokenRepository;


    protected User isUserValid(Long userId) {
        logger.info("UserServiceUtils - Inside isUserValid method");
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new NotFoundException(ERROR_USER_NOT_FOUND);

        return optionalUser.get();
    }

    public void updateAccessToken(Long userId){
        List<AccessToken> accessTokenList = accessTokenRepository.findByUserIdAndIsValidTrue(userId);
        if (accessTokenList !=null && !accessTokenList.isEmpty()){
            for (AccessToken accessToken : accessTokenList) {
                accessToken.setIsValid(Boolean.FALSE);
                accessTokenRepository.save(accessToken);
            }
        }
    }
}
