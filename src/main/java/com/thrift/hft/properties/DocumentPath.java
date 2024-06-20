package com.thrift.hft.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;

@Data
@Validated
@ConfigurationProperties("hft.document-path")
@Component
public class DocumentPath {
    @NotEmpty
    private String basePath;
    @NotEmpty
    private String productImages;


    @PostConstruct
    public void setBasePath(){
        productImages = getBasePath().concat(getProductImages());

    }
}
