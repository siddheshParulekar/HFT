package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.dto.MasterDTO;
import com.thrift.hft.enums.*;
import com.thrift.hft.exceptions.InvalidException;
import com.thrift.hft.service.IHomePageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.thrift.hft.enums.Mastertype.BRAND;

@Service
public class HomePageServiceImpl implements IHomePageService {
    private static final Logger logger = LogManager.getLogger(HomePageServiceImpl.class);


    @Override
    public List<MasterDTO> fetchAllMasters(Mastertype masterType) {
        logger.info("HomePageServiceImpl - Inside fetchAllMasters method");
        if (masterType.equals(Mastertype.ROLE)){
            return Arrays.stream(Role.values()).map(role -> new MasterDTO(role.name(), String.valueOf(role.value()))).collect(Collectors.toList());
        } else if (masterType.equals(BRAND)) {
            return Arrays.stream(Brand.values()).map(brand -> new MasterDTO(brand.name(), String.valueOf(brand.value()))).collect(Collectors.toList());
        }else if (masterType.equals(Mastertype.CATEGORY)) {
            return Arrays.stream(Category.values()).map(category -> new MasterDTO(category.name(), String.valueOf(category.value()))).collect(Collectors.toList());
        } else if (masterType.equals(Mastertype.SUBCATEGORYM)) {
             return Arrays.stream(SubCategoryM.values()).map(subCategoryM -> new MasterDTO(subCategoryM.name(), String.valueOf(subCategoryM.value()))).collect(Collectors.toList());
        } else if (masterType.equals(Mastertype.CONDITION)) {
            return Arrays.stream(Condition.values()).map(condition -> new MasterDTO(condition.name(), String.valueOf(condition.value()))).collect(Collectors.toList());
        }else if (masterType.equals(Mastertype.SUBCATEGORYW)) {
            return Arrays.stream(SubCategoryW.values()).map(subCategoryW -> new MasterDTO(subCategoryW.name(), String.valueOf(subCategoryW.value()))).collect(Collectors.toList());
        }
        else
            throw new InvalidException("Invalid Master type");
    }
}
