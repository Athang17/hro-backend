package com.eos.admin.serviceImpl;

import com.eos.admin.dto.VendorInfoDTO;
import com.eos.admin.entity.VendorInfo;
import com.eos.admin.repository.VendorInfoRepository;
import com.eos.admin.service.VendorInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorInfoServiceImpl implements VendorInfoService {

    private final VendorInfoRepository vendorInfoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public VendorInfoServiceImpl(VendorInfoRepository vendorInfoRepository, ModelMapper modelMapper) {
        this.vendorInfoRepository = vendorInfoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public VendorInfoDTO saveVendorInfo(VendorInfoDTO vendorInfoDto) {
        try {
            // Mapping DTO to entity using ModelMapper
            VendorInfo vendorInfo = modelMapper.map(vendorInfoDto, VendorInfo.class);
            
            if (vendorInfo.getDirectors() != null) {
                vendorInfo.getDirectors().forEach(director -> director.setVendorInfo(vendorInfo));
            }
            
            if (vendorInfo.getBankDetails() != null) {
                vendorInfo.getBankDetails().setVendorInfo(vendorInfo);
            }

            // Saving the vendor info entity to the database
            VendorInfo savedVendorInfo = vendorInfoRepository.save(vendorInfo);

            // Mapping saved entity back to DTO and returning
            return modelMapper.map(savedVendorInfo, VendorInfoDTO.class);
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while saving VendorInfo: " + e.getMessage(), e);
        }
    }

    @Override
    public VendorInfoDTO getVendorInfoById(Long id) {
        try {
            // Fetching VendorInfo by ID from the repository
            VendorInfo vendorInfo = vendorInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("VendorInfo not found with id: " + id));

            // Mapping entity to DTO and returning
            return modelMapper.map(vendorInfo, VendorInfoDTO.class);
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while fetching VendorInfo by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<VendorInfoDTO> getAllVendorInfos() {
        try {
            // Fetching all VendorInfo records from the repository
            List<VendorInfo> vendorInfos = vendorInfoRepository.findAll();

            // Mapping each entity to DTO
            return vendorInfos.stream()
                    .map(vendorInfo -> modelMapper.map(vendorInfo, VendorInfoDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while fetching all VendorInfos: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteVendorInfo(Long id) {
        try {
            // Deleting VendorInfo by ID from the repository
            vendorInfoRepository.deleteById(id);
        } catch (Exception e) {
            // Handle exceptions and log appropriately
            throw new RuntimeException("Error occurred while deleting VendorInfo with id: " + id + " - " + e.getMessage(), e);
        }
    }
}
