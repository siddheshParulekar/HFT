package com.thrift.hft.security;

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
public class JwtUtils extends UserServiceUtils {

    private static final String SECRET_KEY = "HFT";

    private static final int TOKEN_VALIDITY = 3600 * 5;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String getClaimFromToken(String token, String key) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        if (claims.containsKey(key))
            return claims.get(key).toString();
        return null;
    }

    public String generateToken(TokenRequest tokenRequest) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USERID, tokenRequest.getUserId());
        claims.put(CLAIM_USER_ROLE_ID, tokenRequest.getUserRoleId());
        claims.put(CLAIM_EMAIL, tokenRequest.getEmail());
        claims.put(CLAIM_FULLNAME, tokenRequest.getName());
        claims.put(CLAIM_USERNAME, tokenRequest.getUsername());
        claims.put(CLAIM_AUTHORITIES, tokenRequest.getAuthority());
        claims.put(CLAIM_ROLE_ID, tokenRequest.getRoleId());
        claims.put(CLAIM_USER_TYPE, tokenRequest.getUserType());

        return BEARER + Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token) {

        try {
            Optional<AccessToken> optionalAccessToken = accessTokenRepository.findByTokenAndIsValid(token,Boolean.FALSE);
            if (optionalAccessToken.isEmpty()) {
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);

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
