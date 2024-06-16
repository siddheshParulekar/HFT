package com.thrift.hft.service;

import com.thrift.hft.dto.MasterDTO;
import com.thrift.hft.enums.Mastertype;

import java.util.List;

public interface IHomePageService {
    List<MasterDTO> fetchAllMasters(Mastertype masterType);
}
