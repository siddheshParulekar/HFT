package com.thrift.hft.security;

import com.thrift.hft.properties.JwtProperties;
import com.thrift.hft.request.TokenRequest;
import com.thrift.hft.entity.AccessToken;
import com.thrift.hft.exceptions.UnAuthorizedException;
import com.thrift.hft.repository.AccessTokenRepository;
import com.thrift.hft.utils.UserServiceUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.thrift.hft.security.SecurityConstants.*;
import static com.thrift.hft.security.SecurityConstants.CLAIM_USER_ROLE_ID;

@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class JwtUtils extends UserServiceUtils {

    private static final String SECRET_KEY = "9y$B&E)H@McQeThWmZq4t7w!z%C*F-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVmYp3";

    private static final int TOKEN_VALIDITY = 3600 * 5;
    private final JwtProperties jwtProperties;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
    }

    public String getClaimFromToken(String token, String key) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
        if (claims.containsKey(key))
            return claims.get(key).toString();
        return null;
    }

    public String generateToken(TokenRequest tokenRequest) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USERID, tokenRequest.getUserId());
        claims.put(CLAIM_EMAIL, tokenRequest.getEmail());
        claims.put(CLAIM_FULLNAME, tokenRequest.getName());
        claims.put(CLAIM_AUTHORITIES, tokenRequest.getAuthority());

        return BEARER + Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiryTime()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey()).compact();
    }

    public Boolean validateToken(String token) {

        try {
            Optional<AccessToken> optionalAccessToken = accessTokenRepository.findByTokenAndIsValid(token,Boolean.FALSE);
            if (optionalAccessToken.isEmpty()) {
                Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token);

                isUserValid(Long.valueOf(getClaimFromToken(token, CLAIM_USERID)));
                return true;
            }

            throw new UnAuthorizedException();
        } catch (Exception ex) {
            AccessToken accessToken = new AccessToken(token,Boolean.FALSE);
            accessTokenRepository.save(accessToken);
            throw ex;
        }
    }
}
