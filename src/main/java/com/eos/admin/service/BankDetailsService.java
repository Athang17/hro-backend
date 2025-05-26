package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.BankDetailsDTO;

public interface BankDetailsService {
    BankDetailsDTO saveBankDetail(BankDetailsDTO bankDetailDto);
    BankDetailsDTO getBankDetailById(Long id);
    List<BankDetailsDTO> getAllBankDetails();
    void deleteBankDetail(Long id);
}

