package com.thrift.hft.utils;

import com.thrift.hft.dto.AddressDTO;
import com.thrift.hft.dto.ProdImageDTO;
import com.thrift.hft.entity.Address;
import com.thrift.hft.entity.ProductImage;
import com.thrift.hft.enums.*;
import com.thrift.hft.exceptions.InvalidException;
import com.thrift.hft.properties.JwtProperties;
import com.thrift.hft.repository.AddressRepository;
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
    private static AddressRepository addressRepository;

    @Autowired
    public CommonUtils(JwtProperties jwtProperties,
                       ProdImageRepository prodImageRepository,
                       AddressRepository addressRepository){
        CommonUtils.properties = jwtProperties;
        CommonUtils.prodImageRepository = prodImageRepository;
        CommonUtils.addressRepository= addressRepository;
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



    public static List<ProdImageDTO> getProductImages(String productId) throws IOException {
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

    public static List<AddressDTO> getUserAddress(Long userId){
        log.info("CommonUtils - Inside getUserAddress method ");

        List<AddressDTO> addressDTOS= new ArrayList<>();
        List<Address> addressList = addressRepository.findByUserId(userId);

        if(!addressList.isEmpty()){
            addressList.forEach(a-> addressDTOS.add(a.getAddressDTO()));
        }

        return addressDTOS;

    }

    public static Map<String,String> getEnumMap(String name, String value)
    {
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("value",value);
        return map;
    }

    public static Condition getCondition(String condition) {
        if (condition == null)
            throw new InvalidException("Condition cannot be null");
        else if (condition.equalsIgnoreCase(Condition.GOOD.name()))
            return Condition.GOOD;
        else if (condition.equalsIgnoreCase(Condition.LIKE_NEW.name()))
            return Condition.LIKE_NEW;
        else if (condition.equalsIgnoreCase(Condition.NEW.name()))
            return Condition.NEW;
        else if (condition.equalsIgnoreCase(Condition.POOR.name()))
            return Condition.POOR;
        else
            throw new InvalidException("Invalid condition type");
    }

    public static Brand getBrand(String brand) {
        if (brand == null)
            return Brand.OTHERS;
          final Map<String, Brand> brandMap = new HashMap<>();
        for (Brand brands : Brand.values()) {
            brandMap.put(brands.name().toLowerCase(), brands);
        }
        String normalizedBrand = brand.trim().toLowerCase();

        Brand resolvedBrand = brandMap.get(normalizedBrand);
        if (resolvedBrand == null) {
            throw new IllegalArgumentException("Invalid brand type");
        }
        return resolvedBrand;
    }

    public static Category getCategory(String category) {
        if (category == null)
            return Category.UNISEX;
        final Map<String, Category> categoryMap = new HashMap<>();
        for (Category category1 : Category.values()) {
            categoryMap.put(category1.name().toLowerCase(), category1);
        }
        String normalizedBrand = category.trim().toLowerCase();

        Category resolvedBrand = categoryMap.get(normalizedBrand);
        if (resolvedBrand == null) {
            throw new IllegalArgumentException("Invalid category type");
        }
        return resolvedBrand;
    }

    public static SubCategory getSubCategory(String subCategory) {
        if (subCategory == null)
            return SubCategory.OTHERS;
        final Map<String, SubCategory> subCategoryMap = new HashMap<>();
        for (SubCategory category1 : SubCategory.values()) {
            subCategoryMap.put(category1.name().toLowerCase(), category1);
        }
        String normalizedBrand = subCategory.trim().toLowerCase();

        SubCategory resolvedBrand = subCategoryMap.get(normalizedBrand);
        if (resolvedBrand == null) {
            throw new IllegalArgumentException("Invalid subCategory type");
        }
        return resolvedBrand;
    }

    public static Size getSize(String size) {
        if (size == null)
            return Size.FREE_SIZE;
        final Map<String, Size> subCategoryMap = new HashMap<>();
        for (Size category1 : Size.values()) {
            subCategoryMap.put(category1.name().toLowerCase(), category1);
        }
        String normalizedBrand = size.trim().toLowerCase();

        Size resolvedBrand = subCategoryMap.get(normalizedBrand);
        if (resolvedBrand == null) {
            throw new IllegalArgumentException("Invalid size type");
        }
        return resolvedBrand;
    }

    public static Colour getColor(String color) {
        if (color == null)
            return Colour.OTHER;
        final Map<String, Colour> subCategoryMap = new HashMap<>();
        for (Colour category1 : Colour.values()) {
            subCategoryMap.put(category1.name().toLowerCase(), category1);
        }
        String normalizedBrand = color.trim().toLowerCase();

        Colour resolvedBrand = subCategoryMap.get(normalizedBrand);
        if (resolvedBrand == null) {
            throw new IllegalArgumentException("Invalid color type");
        }
        return resolvedBrand;
    }

}

