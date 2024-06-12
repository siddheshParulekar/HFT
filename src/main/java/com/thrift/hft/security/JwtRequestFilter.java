package com.thrift.hft.security;

import com.thrift.hft.exceptions.UnAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.thrift.hft.constants.ErrorMsgConstants.ERROR_UNABLE_TO_GET_TOKEN;
import static com.thrift.hft.security.SecurityConstants.*;
import static com.thrift.hft.security.SecurityConstants.FILTER_LOGIN_URL;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SpringSecurityUserUtils springSecurityUserUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (Boolean.TRUE.equals(skipAuth(request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader(AUTHORIZATION);

        if (Objects.isNull(requestTokenHeader))
            throw new UnAuthorizedException();

        String email = null;
        String jwtToken;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                Claims claims = jwtUtils.getAllClaimsFromToken(jwtToken);
                email = claims.get(EMAIL).toString();
            } catch (IllegalArgumentException e) {
                log.error(ERROR_UNABLE_TO_GET_TOKEN);
            }  catch (Exception e) {
                throw new UnAuthorizedException();
            }
        } else throw new UnAuthorizedException();

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = springSecurityUserUtils.loadUserByUsername(jwtToken);
            if (Boolean.TRUE.equals(jwtUtils.validateToken(jwtToken))) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else throw new UnAuthorizedException();
        }

        filterChain.doFilter(request, response);

    }


    private Boolean skipAuth(String requestUri) {
        if (requestUri.contains(FILTER_REGISTER_URL))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
