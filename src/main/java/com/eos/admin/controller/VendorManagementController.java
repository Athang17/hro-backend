package com.eos.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eos.admin.dto.ReqRes;
import com.eos.admin.dto.VendorReqRep;
import com.eos.admin.serviceImpl.VendorManagementServiceImpl;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth/vendor")
public class VendorManagementController {
	
	@Autowired
	private VendorManagementServiceImpl vendorManagementServiceImpl;
	
	@PostMapping("/register")
	public ResponseEntity<VendorReqRep> register(@RequestBody VendorReqRep reqRes){
		VendorReqRep response = vendorManagementServiceImpl.register(reqRes);
		return ResponseEntity.ok(response);
	}


	@PostMapping("/login")
	public ResponseEntity<VendorReqRep> login(@RequestBody VendorReqRep req){
		VendorReqRep response = vendorManagementServiceImpl.login(req);
		return ResponseEntity.ok(response);
	}

}