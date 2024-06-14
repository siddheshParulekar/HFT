package com.thrift.hft.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties("hft.jwt")
@Component
public class JwtProperties {
    @NotEmpty
    private String secretKey;

    @NotNull
    private Long expiryTime;
}