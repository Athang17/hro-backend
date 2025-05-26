package com.eos.admin.serviceImpl;

import org.springframework.stereotype.Service;

import com.eos.admin.dto.VendorInfoDTO;
import com.eos.admin.entity.VendorInfo;
import com.eos.admin.repository.VendorInfoRepository;
import com.eos.admin.service.VendorInfoService;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorInfoServiceImpl implements VendorInfoService {

    private final VendorInfoRepository vendorInfoRepository;
    private final ModelMapper modelMapper;

    public VendorInfoServiceImpl(VendorInfoRepository vendorInfoRepository, ModelMapper modelMapper) {
        this.vendorInfoRepository = vendorInfoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public VendorInfoDTO saveVendorInfo(VendorInfoDTO vendorInfoDto) {
        try {
            VendorInfo vendorInfo = modelMapper.map(vendorInfoDto, VendorInfo.class);
            VendorInfo savedVendorInfo = vendorInfoRepository.save(vendorInfo);
            return modelMapper.map(savedVendorInfo, VendorInfoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving VendorInfo: " + e.getMessage(), e);
        }
    }

    @Override
    public VendorInfoDTO getVendorInfoById(Long id) {
        try {
            VendorInfo vendorInfo = vendorInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("VendorInfo not found with id: " + id));
            return modelMapper.map(vendorInfo, VendorInfoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching VendorInfo by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<VendorInfoDTO> getAllVendorInfos() {
        try {
            List<VendorInfo> vendorInfos = vendorInfoRepository.findAll();
            return vendorInfos.stream()
                    .map(vendorInfo -> modelMapper.map(vendorInfo, VendorInfoDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching all VendorInfos: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteVendorInfo(Long id) {
        try {
            vendorInfoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting VendorInfo with id: " + id + " - " + e.getMessage(), e);
        }
    }
}
