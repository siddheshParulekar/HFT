package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.enums.Mastertype;
import com.thrift.hft.request.SellRequest;
import com.thrift.hft.utils.ResponseEntityUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);



    @GetMapping("/sell-request")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> fetchAllMasters(@RequestBody SellRequest request) {
        logger.info("DashboardController - Inside fetchAllMasters method");
        return ResponseEntityUtils.get(dashboardService.fetchAllMasters(masterType), "Master fetch successfully");
    }

}
