package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.dto.MasterDTO;
import com.thrift.hft.enums.Mastertype;
import com.thrift.hft.enums.Role;
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

        List<MasterDTO> masterDTOList = new ArrayList<>();
        if (masterType.equals(Mastertype.ROLE)){
            masterDTOList.add(new MasterDTO(Role.ADMIN.name(),Role.ADMIN.value()));
            masterDTOList.add(new MasterDTO(Role.USER.name(),Role.USER.value()));
            return masterDTOList;
        } else if (masterType.equals(BRAND)) {
            return Arrays.stream(BRAND.values()).map(assetType -> new MasterDTO(assetType.name(), String.valueOf(assetType.values()))).collect(Collectors.toList());

        }
        return masterDTOList;
    }
}
