package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.VendorInfoDTO;


public interface VendorInfoService {
    VendorInfoDTO saveVendorInfo(VendorInfoDTO vendorInfoDto);
    VendorInfoDTO getVendorInfoById(Long id);
    List<VendorInfoDTO> getAllVendorInfos();
    void deleteVendorInfo(Long id);
}
