package com.eos.admin.serviceImpl;

import org.springframework.stereotype.Service;

import com.eos.admin.dto.BankDetailsDTO;
import com.eos.admin.entity.BankDetails;
import com.eos.admin.repository.BankDetailsRepository;
import com.eos.admin.service.BankDetailsService;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankDetailsServiceImpl implements BankDetailsService {

    private final BankDetailsRepository bankDetailRepository;
    private final ModelMapper modelMapper;

    public BankDetailsServiceImpl(BankDetailsRepository bankDetailRepository, ModelMapper modelMapper) {
        this.bankDetailRepository = bankDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BankDetailsDTO saveBankDetail(BankDetailsDTO bankDetailDto) {
        try {
            BankDetails bankDetail = modelMapper.map(bankDetailDto, BankDetails.class);
            BankDetails savedBankDetail = bankDetailRepository.save(bankDetail);
            return modelMapper.map(savedBankDetail, BankDetailsDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving BankDetail: " + e.getMessage(), e);
        }
    }

    @Override
    public BankDetailsDTO getBankDetailById(Long id) {
        try {
            BankDetails bankDetail = bankDetailRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("BankDetail not found with id: " + id));
            return modelMapper.map(bankDetail, BankDetailsDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching BankDetail by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BankDetailsDTO> getAllBankDetails() {
        try {
            List<BankDetails> bankDetails = bankDetailRepository.findAll();
            return bankDetails.stream()
                    .map(bankDetail -> modelMapper.map(bankDetail, BankDetailsDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching all BankDetails: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteBankDetail(Long id) {
        try {
            bankDetailRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting BankDetail with id: " + id + " - " + e.getMessage(), e);
        }
    }
}
