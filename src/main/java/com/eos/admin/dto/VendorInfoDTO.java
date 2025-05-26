package com.eos.admin.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorInfoDTO {

    private String companyName;
    private String address;
    private String city;
    private String pinCode;
    private String telephone;
    private String mobile;
    private String email;
    private String contactPerson;

    private String pan;
    private String gst;
    private String msme;

    private String serviceType;
    private String serviceTypeOther;

    private boolean declaration;
    
    private List<DirectorDTO> directors;
    private BankDetailsDTO bankDetails;
}

