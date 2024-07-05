package com.thrift.hft.utils;

import com.thrift.hft.dto.ProdImageDTO;
import com.thrift.hft.entity.ProductImage;
import com.thrift.hft.properties.JwtProperties;
import com.thrift.hft.repository.ProdImageRepository;
import com.thrift.hft.response.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thrift.hft.security.SecurityConstants.*;

@Component
@Slf4j
public class CommonUtils {

    private static JwtProperties properties;
    private static ProdImageRepository prodImageRepository;

    @Autowired
    public CommonUtils(JwtProperties jwtProperties,
                       ProdImageRepository prodImageRepository){
        CommonUtils.properties = jwtProperties;
        CommonUtils.prodImageRepository = prodImageRepository;
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



    public static List<ProdImageDTO> getProductImages(Long productId) throws IOException {
        log.info("CommonUtils - Inside getProductImages method");
        List<ProdImageDTO> prodImageList= new ArrayList<>();
        List<ProductImage> imageList = prodImageRepository.findByProductId(productId);
        for (ProductImage productImage : imageList){
            byte[] fileByte = Files.readAllBytes(Paths.get(productImage.getFilePath()));
            String[] parts = productImage.getFilePath().split("/");
            String filename = parts[parts.length - 1];
            prodImageList.add(new ProdImageDTO(filename,fileByte));
        }

        return prodImageList;

    }

    public static Map<String,String> getEnumMap(String name, String value)
    {
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("value",value);
        return map;
    }

}

