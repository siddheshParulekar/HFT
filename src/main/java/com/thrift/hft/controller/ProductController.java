package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.enums.*;
import com.thrift.hft.request.GetAllProductRequest;
import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;

    @PostMapping("/sell-request")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> createSellRequest(@RequestPart(name = "amount") String amount,
                                                                            @RequestPart(name = "description") String description,
                                                                            @RequestPart(name = "condition") String condition,
                                                                            @RequestPart(name = "category",required = false) String category,
                                                                            @RequestPart(name = "subCategory",required = false) String subCategory,
                                                                            @RequestPart(name = "brand",required = false) String brand,
                                                                            @RequestPart(name = "size",required = false) String size,
                                                                             @RequestPart(name = "color",required = false) String color,
                                                                            @RequestPart(name = "files") MultipartFile[] files,
                                                                            HttpServletRequest request
                                                                            ) throws IOException {
        productService.createSellRequest(new ProductRequest(description,new BigDecimal(amount),condition,category,subCategory,brand,size,color,files),CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION)));
        return ResponseEntityUtils.get(null,"Request for product listing created successfully");
    }


    @GetMapping(value = "/get-products")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> getAllProducts(Pageable pageable,
                                                      @RequestParam(required = false, name = "category") Category category,
                                                      @RequestParam(required = false, name = "subCategory") SubCategory subCategory,
                                                      @RequestParam(required = false, name = "brand") Brand brand,
                                                      @RequestParam(required = false, name = "prodStatus") ProdStatus prodStatus,
                                                      @RequestParam(required = false, name = "approvalStatus") ApprovalStatus approvalStatus,
                                                      @RequestParam(required = false, name = "fit") Size size,
                                                      @RequestParam(required = false, name = "color") Colour colour,
                                                      @RequestParam(required = false,name = "condition") Condition condition) throws IOException {
        logger.info("ProductController - Inside getAllProducts method");

        return ResponseEntityUtils.get(productService.getAllProduct(new GetAllProductRequest(pageable,category,subCategory,brand,prodStatus,approvalStatus,size,colour,condition)),"Products fetched ");

    }

    @GetMapping("/view-product/{pid}")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> viewProduct(@PathVariable("pid") String pid) throws IOException {
        logger.info("ProductController - Inside viewProduct method");

        return ResponseEntityUtils.get(productService.viewProduct(pid),"Product fetched ");
    }
}
