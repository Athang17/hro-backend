package com.eos.admin.service;

import com.eos.admin.dto.VendorInfoDTO;

import java.util.List;

public interface VendorInfoService {

    // Save VendorInfo and return the corresponding DTO
    VendorInfoDTO saveVendorInfo(VendorInfoDTO vendorInfoDto);

    // Get VendorInfo by ID and return the corresponding DTO
    VendorInfoDTO getVendorInfoById(Long vendorId);

    // Get all VendorInfos and return a list of DTOs
    List<VendorInfoDTO> getAllVendorInfos();

    // Delete VendorInfo by ID
    void deleteVendorInfo(Long id);
}
