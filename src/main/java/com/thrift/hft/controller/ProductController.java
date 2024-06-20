package com.thrift.hft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.request.ProductRequest;
import com.thrift.hft.request.SellRequest;
import com.thrift.hft.service.IProductService;
import com.thrift.hft.utils.CommonUtils;
import com.thrift.hft.utils.ResponseEntityUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.thrift.hft.security.SecurityConstants.AUTHORIZATION;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;


    @PostMapping(value = "/sell-request",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> sellRequest(HttpServletRequest request) throws IOException, ServletException {
        logger.info("ProductController - Inside sellRequest method");
        SellRequest productRequest = new ObjectMapper().readValue(request.getParameter("productRequest"),SellRequest.class);
        int i = 0;
        List<Part> parts = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals("productRequest")) {
                continue;
            }
            if (part.getName().equals("productRequests[" + i + "]")) {
                parts.add(part);
            } else {
                // image set
                if (!parts.isEmpty()) {
                    productRequest.getProductRequests().get(i).setImages(new ArrayList<>(parts));
                    parts.clear();  // Clear the parts list for the next set of images
                }
                i++;
                parts.add(part);  // Add the current part to the new list
            }
        }
// Set images for the last product request
        if (!parts.isEmpty()) {
            productRequest.getProductRequests().get(i).setImages(new ArrayList<>(parts));
        }
        List<ProductRequest> productRequestList =productRequest.getProductRequests();

        return ResponseEntityUtils.get(productService.createSellRequest(productRequestList, CommonUtils.getTokenResponse(request.getHeader(AUTHORIZATION))), "Selling request created successfully");
    }

}
