package com.thrift.hft.utils;

import com.thrift.hft.properties.JwtProperties;
import com.thrift.hft.response.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import static com.thrift.hft.security.SecurityConstants.*;

@Component
public class CommonUtils {

    private static JwtProperties properties;

    @Autowired
    public CommonUtils(JwtProperties jwtProperties){
        CommonUtils.properties = jwtProperties;
    }

    public static String encodePassword(String password) {
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        return password;
    }

    public static TokenResponse getTokenResponse(String token) {
        Claims claims = Jwts.parser().setSigningKey(properties.getSecretKey()).parseClaimsJws(token.substring(7)).getBody();
        return new TokenResponse(Long.valueOf(claims.get(CLAIM_USERID).toString()), claims.get(CLAIM_EMAIL).toString(),
                claims.get(CLAIM_FULLNAME).toString(), claims.get(CLAIM_AUTHORITIES).toString());
    }


    public static boolean checkPassword(String password, String dbPassword) {
        return BCrypt.checkpw(password, dbPassword);
    }

    public static String getName(String firstName, String lastName) {
        return firstName + " " + lastName;
    }
}

