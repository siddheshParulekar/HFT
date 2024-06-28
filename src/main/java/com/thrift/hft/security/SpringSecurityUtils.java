package com.thrift.hft.security;


import com.thrift.hft.response.LoginResponse;
import com.thrift.hft.service.serviceImpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SpringSecurityUtils extends WebSecurityConfigurerAdapter {

    public static final String[] PUBLIC_URLS = {
            "/v1/user/register", "/v1/auth/login", "/v1/auth/google", "/v2/api-docs"
            , "/v1/home-page/**","/swagger-ui/", "/swagger-resources/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/v3/**"};

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
/**
                    String redirectUrl = (String) request.getSession().getAttribute("redirect_url");
                    if (redirectUrl == null) {
                        redirectUrl = "http://localhost:4200/";
                    }
                    log.info("Redirecting to: {}", redirectUrl);
                    response.sendRedirect(redirectUrl);
 **/

                   // TODO:Siddhesh need to think about this
                  /**   FE you can remove " + loginResponse.getToken()" this part as of now OR
                            accept the token through queryParam and pass that token further
                   **/
                    String redirectUrl = "http://localhost:4200/" + loginResponse.getToken();
                    response.sendRedirect(redirectUrl);
                }).failureHandler((request, response, exception) -> {
                    log.error("OAuth2 Login Failure: ", exception);
                    response.sendRedirect("/login?error");
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
