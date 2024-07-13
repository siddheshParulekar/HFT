package com.thrift.hft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.enums.*;
import com.thrift.hft.request.GetAllProductRequest;
import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.request.SellRequest;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;

    @PostMapping("/create-manual-match-request")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> createPaymentAccountStatementRequest(@RequestPart(value = "amount", required = false) String amount,
                                                                            @RequestPart(value = "description", required = false) String description,
                                                                            @RequestPart(value = "condition", required = false) String condition,
                                                                            @RequestPart(value = "category", required = false) String category,
                                                                            @RequestPart(value = "subCategory", required = false) String subCategory,
                                                                            @RequestPart(value = "brand", required = false) String brand,
                                                                            @RequestPart(value = "size", required = false) String size,
                                                                            @RequestPart(name = "files", required = false) MultipartFile[] files,
                                                                            HttpServletRequest request
                                                                            ){
        return ResponseEntityUtils.get(productService.createSellRequest(new ProductRequest(description,new BigDecimal(amount),condition,category,subCategory,brand,size,files),CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))),"Product added successfully");
    }


    @GetMapping(value = "/get-products")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> getAllProducts(Pageable pageable,
                                                      @RequestParam(required = false, name = "category") Category category,
                                                      @RequestParam(required = false, name = "subCategory") SubCategory subCategory,
                                                      @RequestParam(required = false, name = "brand") Brand brand,
                                                      @RequestParam(required = false, name = "prodStatus") ProdStatus prodStatus,
                                                      @RequestParam(required = false, name = "approvalStatus") ApprovalStatus approvalStatus,
                                                      @RequestParam(required = false, name = "size") Size size,
                                                      @RequestParam(required = false,name = "condition") Condition condition) throws IOException {
        logger.info("ProductController - Inside getAllProducts method");

        return ResponseEntityUtils.get(productService.getAllProduct(new GetAllProductRequest(pageable,category,subCategory,brand,prodStatus,approvalStatus,size,condition)),"Products fetched ");

    }

    @GetMapping("/view-product/{pid}")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> viewProduct(@PathVariable("pid") Long pid) throws IOException {
        logger.info("ProductController - Inside viewProduct method");

        return ResponseEntityUtils.get(productService.viewProduct(pid),"Product fetched ");
    }
}
