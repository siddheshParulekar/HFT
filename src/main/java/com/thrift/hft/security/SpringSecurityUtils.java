package com.thrift.hft.security;


import com.thrift.hft.response.LoginResponse;
import com.thrift.hft.service.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

import static com.thrift.hft.security.SecurityConstants.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityUtils extends WebSecurityConfigurerAdapter {

    public static final String[] PUBLIC_URLS = {
            "/v1/user/register", "/v1/auth/login", "/v1/auth/google", "/v2/api-docs"
            , "/v1/home-page/**"};

    @Value("${custom.security.username}")
    private String username;

    @Value("${custom.security.password}")
    private String password;

    @Autowired
    private JwtRequestFilter requestFilter;
    @Value("${app.cors.origins}")
    private String origins;

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;
    @Autowired
    private UserServiceImpl userService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().configurationSource(request -> {
            CorsConfiguration corsConfig = new CorsConfiguration().applyPermitDefaultValues();
            List<String> originList = Arrays.asList(origins.split(","));
            corsConfig.setAllowedOrigins(originList);
            corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
            corsConfig.addAllowedHeader("*");
            return corsConfig;
        });

        httpSecurity
                .headers().addHeaderWriter((request, response) -> response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)).and().csrf().disable().authorizeRequests()
                .antMatchers(PUBLIC_URLS).permitAll()
                .antMatchers(URL_SWAGGER_UI, URL_API_DOCS_ALL, URL_SWAGGER_RESOURCES)
                .authenticated()
                .antMatchers(HttpHeaders.ALLOW).permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login().successHandler((request, response, authentication) -> {
                        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                        String email = oauthUser.getAttribute("email");
                        String name = oauthUser.getAttribute("name");
                    LoginResponse loginResponse = userService.processOAuthPostLog(email, name);
                    //TODO:Siddhesh need to think about this
                     //TODO: FE you can remove " + loginResponse.getToken()" this part as of now
                    String redirectUrl = "http://localhost:4200/" + loginResponse.getToken();
                    // Redirect to the home page
                    response.sendRedirect(redirectUrl);
                });


        httpSecurity.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    //Below Method is used for applying security on swagger urls
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username).password(passwordEncoder().encode(password)).roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
