package com.thrift.hft.security;

import com.thrift.hft.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.thrift.hft.constants.ErrorMsgConstants.ERROR_USER_NOT_FOUND;
import static com.thrift.hft.security.SecurityConstants.CLAIM_AUTHORITIES;
import static com.thrift.hft.security.SecurityConstants.CLAIM_USERID;

@Component
public class SpringSecurityUserUtils  implements UserDetailsService {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        if (token != null) {
            String role = jwtUtils.getClaimFromToken(token, CLAIM_AUTHORITIES);
            String userId = jwtUtils.getClaimFromToken(token, CLAIM_USERID);
            return User.builder().username(userId).password("").authorities(getAuthority(role)).build();
        } else
            throw new NotFoundException(ERROR_USER_NOT_FOUND);
    }

    private Set getAuthority(String roles) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roles));
        return authorities;
    }
}
