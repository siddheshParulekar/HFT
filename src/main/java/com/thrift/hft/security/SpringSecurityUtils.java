package com.thrift.hft.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static com.thrift.hft.security.SecurityConstants.ACCESS_CONTROL_EXPOSE_HEADERS;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityUtils extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter requestFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().configurationSource(request -> {
            CorsConfiguration corsConfig = new CorsConfiguration().applyPermitDefaultValues();
            corsConfig.addAllowedMethod(HttpMethod.PUT);
            corsConfig.addAllowedMethod(HttpMethod.DELETE);
            corsConfig.addAllowedMethod(HttpMethod.PATCH);
            return corsConfig;
        });

        httpSecurity
                .headers().addHeaderWriter((request, response) -> response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)).and().csrf().disable().authorizeRequests()
                .antMatchers("/user/register").permitAll()
                .antMatchers(HttpHeaders.ALLOW).permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
