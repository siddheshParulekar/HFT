package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.entity.AccessToken;
import com.thrift.hft.entity.User;
import com.thrift.hft.exceptions.IncorrectEmailIdException;
import com.thrift.hft.exceptions.IncorrectPasswordException;
import com.thrift.hft.exceptions.InvalidException;
import com.thrift.hft.exceptions.UnAuthorizedException;
import com.thrift.hft.repository.AccessTokenRepository;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.LoginRequest;
import com.thrift.hft.request.TokenRequest;
import com.thrift.hft.response.LoginResponse;
import com.thrift.hft.security.JwtUtils;
import com.thrift.hft.service.ILoginService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.UserServiceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.thrift.hft.constants.ErrorMsgConstants.ERROR_USER_IS_NOT_ACTIVE;
import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;
import static com.thrift.hft.security.SecurityConstants.BEARER;

@Service
public class LoginServiceImpl implements ILoginService {
    private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);
    @Autowired
    private UserRepository userRepository;



    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private UserServiceUtils userServiceUtils;


    @Override
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        logger.info("LoginServiceImpl - Inside login method");

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(IncorrectEmailIdException::new);

        if (!CommonUtils.checkPassword(loginRequest.getPassword(), user.getPassword()))
            throw new IncorrectPasswordException();

        if (Boolean.FALSE.equals(user.getIsActive()))
            throw new InvalidException(ERROR_USER_IS_NOT_ACTIVE);


       // userServiceUtils.updateAccessToken(user.getId());
        LoginResponse loginResponse = new LoginResponse(user.getUserDTO(), jwtUtils.generateToken(new TokenRequest(user.getId(),
                     user.getEmail(),user.getRole().name(),
                user.getName())));
            accessTokenRepository.save(new AccessToken(loginResponse.getToken().replace(BEARER, ""), user.getId()));
            return loginResponse;

    }

    @Override
    public void logout(HttpServletRequest request) {
        logger.info("LoginServiceImpl - Inside logout method");
        String token;
        token = request.getHeader(AUTHORIZATION).replace(BEARER, "");
        Optional<AccessToken> optionalAccessToken = accessTokenRepository.findByTokenAndIsValid(token, Boolean.TRUE);
        if (optionalAccessToken.isPresent()) {
            AccessToken accessToken = optionalAccessToken.get();
            accessToken.setLogoutAt(LocalDateTime.now());
            accessToken.setIsValid(Boolean.FALSE);
            accessTokenRepository.save(accessToken);
        }
        throw new UnAuthorizedException();
    }

}