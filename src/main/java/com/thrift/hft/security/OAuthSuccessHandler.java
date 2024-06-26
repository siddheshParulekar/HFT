package com.thrift.hft.security;

import com.thrift.hft.entity.AccessToken;
import com.thrift.hft.entity.User;
import com.thrift.hft.enums.Role;
import com.thrift.hft.repository.AccessTokenRepository;
import com.thrift.hft.repository.UserRepository;
import com.thrift.hft.request.TokenRequest;
import com.thrift.hft.response.LoginResponse;
import com.thrift.hft.service.serviceImpl.LoginServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.thrift.hft.security.SecurityConstants.BEARER;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LogManager.getLogger(OAuthSuccessHandler.class);

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    logger.info("OAuthSuccessHandler - onAuthenticationSuccess method");
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User)authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        if (userRepository.findByEmail(email).isPresent())
            return;
        User user = userRepository.save(User.builder()
                .email(email)
                .role(Role.USER)
                .name(name)
                .build());
        LoginResponse loginResponse = new LoginResponse(user.getUserDTO(), jwtUtils.generateToken(new TokenRequest(user.getId(),
                user.getEmail(),user.getRole().name(),
                user.getName())));
        accessTokenRepository.save(new AccessToken(loginResponse.getToken().replace(BEARER, ""), user.getId()));
    }
}
