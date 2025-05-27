package com.eos.admin.controller;

import com.eos.admin.dto.DetailedFormDTO;
import com.eos.admin.dto.VendorInfoDTO;
import com.eos.admin.service.VendorInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/vendorInfo")
//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class VendorInfoController {

    private final VendorInfoService vendorInfoService;

    public VendorInfoController(VendorInfoService vendorInfoService) {
        this.vendorInfoService = vendorInfoService;
    }

    // Updated submit method to handle multipart/form-data
    @PostMapping(value = "/submit", consumes = {"multipart/form-data"})
    public ResponseEntity<?> submitDetailedForm(
            @RequestPart("form") DetailedFormDTO formDto,
            @RequestPart(value = "chequeImage", required = false) MultipartFile chequeImage) {

        log.info("Received form submission: {}", formDto);

        try {
            if (chequeImage != null && !chequeImage.isEmpty()) {
                String savedPath = saveFile(chequeImage);
                if (formDto.getBankDetails() != null) {
                    formDto.getBankDetails().setChequeImagePath(savedPath);
                }
            }

            VendorInfoDTO vendorInfoDto = new VendorInfoDTO();

            // Map core fields
            vendorInfoDto.setId(formDto.getId());
            vendorInfoDto.setCompanyName(formDto.getCompanyName());
            vendorInfoDto.setAddress(formDto.getAddress());
            vendorInfoDto.setCity(formDto.getCity());
            vendorInfoDto.setPinCode(formDto.getPinCode());
            vendorInfoDto.setTelephone(formDto.getTelephone());
            vendorInfoDto.setMobile(formDto.getMobile());
            vendorInfoDto.setEmail(formDto.getEmail());
            vendorInfoDto.setContactPerson(formDto.getContactPerson());
            vendorInfoDto.setPan(formDto.getPan());
            vendorInfoDto.setGst(formDto.getGst());
            vendorInfoDto.setMsme(formDto.getMsme());
            vendorInfoDto.setServiceType(formDto.getServiceType());
            vendorInfoDto.setServiceTypeOther(formDto.getServiceTypeOther());
            vendorInfoDto.setDeclaration(formDto.isDeclaration());

            // Attach directors and bank details
            vendorInfoDto.setDirectors(formDto.getDirectors());
            vendorInfoDto.setBankDetails(formDto.getBankDetails());

            VendorInfoDTO saved = vendorInfoService.saveVendorInfo(vendorInfoDto);
            return ResponseEntity.status(201).body(saved);

        } catch (Exception e) {
            log.error("Form submission error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // Get single vendor by ID
    @GetMapping("/{id}")
    public ResponseEntity<VendorInfoDTO> getVendorInfoById(@PathVariable Long id) {
        try {
            VendorInfoDTO vendorInfo = vendorInfoService.getVendorInfoById(id);
            return ResponseEntity.ok(vendorInfo);
        } catch (Exception e) {
            log.error("Error fetching vendor by ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Get all vendor entries
    @GetMapping("/list")
    public ResponseEntity<List<VendorInfoDTO>> getAllVendorInfos() {
        try {
            List<VendorInfoDTO> vendors = vendorInfoService.getAllVendorInfos();
            return ResponseEntity.ok(vendors);
        } catch (Exception e) {
            log.error("Error fetching vendors: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Delete vendor by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable Long id) {
        try {
            vendorInfoService.deleteVendorInfo(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting vendor with ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body("Delete failed: " + e.getMessage());
        }
    }

    // Helper to save file on local disk
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/cheque_images/";
        Files.createDirectories(Paths.get(uploadDir));

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);
        Files.write(filepath, file.getBytes());

        log.info("Saved cheque image to {}", filepath.toString());

        return filepath.toString();
    }
}
