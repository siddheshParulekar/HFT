package com.thrift.hft.controller;

import com.thrift.hft.dto.ResponseDTO;
import com.thrift.hft.enums.Mastertype;
import com.thrift.hft.service.IHomePageService;
import com.thrift.hft.utils.ResponseEntityUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/home-page")
public class HomePageController {

    private static final Logger logger = LogManager.getLogger(HomePageController.class);

    @Autowired
    IHomePageService dashboardService;


    @GetMapping("/wel")
    @ApiOperation(value = "Auth - Access to all Users")
    public String welcomePage(@RequestParam("token") String token) {
        logger.info("DashboardController - Inside logPage method");
        return "Login Page" + token;
    }


    @GetMapping("/fetch-all-masters/{masterType}")
    @ApiOperation(value = "Auth - Access to all Users")
    public ResponseEntity<ResponseDTO> fetchAllMasters(@PathVariable Mastertype masterType) {
        logger.info("DashboardController - Inside fetchAllMasters method");
        return ResponseEntityUtils.get(dashboardService.fetchAllMasters(masterType), "Master fetch successfully");
    }
}
