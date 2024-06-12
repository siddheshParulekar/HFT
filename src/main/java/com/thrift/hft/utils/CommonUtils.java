package com.thrift.hft.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class CommonUtils {


    public static String encodePassword(String password) {
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        return password;
    }

//    public static TokenRespon getTokenResponse(String token) {
//        Claims claims = Jwts.parser().setSigningKey(properties.getSecretKey()).parseClaimsJws(token.substring(7)).getBody();
//        return new TokenResponse(Long.valueOf(claims.get(CLAIM_USERID).toString()), Long.valueOf(claims.get(CLAIM_USER_ROLE_ID).toString()), claims.get(CLAIM_EMAIL).toString(),
//                claims.get(CLAIM_FULLNAME).toString(), claims.get(CLAIM_USERNAME).toString(), claims.get(CLAIM_AUTHORITIES).toString(), Long.valueOf(claims.get(CLAIM_ROLE_ID).toString()), claims.get(CLAIM_USER_TYPE).toString());
//    }
}
